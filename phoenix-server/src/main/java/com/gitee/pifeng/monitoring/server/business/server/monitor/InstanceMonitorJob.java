package com.gitee.pifeng.monitoring.server.business.server.monitor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmLevelEnums;
import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmReasonEnums;
import com.gitee.pifeng.monitoring.common.constant.MonitorTypeEnums;
import com.gitee.pifeng.monitoring.common.constant.ZeroOrOneConstants;
import com.gitee.pifeng.monitoring.common.domain.Alarm;
import com.gitee.pifeng.monitoring.common.dto.AlarmPackage;
import com.gitee.pifeng.monitoring.common.exception.NetException;
import com.gitee.pifeng.monitoring.common.util.DateTimeUtils;
import com.gitee.pifeng.monitoring.server.business.server.core.MonitoringConfigPropertiesLoader;
import com.gitee.pifeng.monitoring.server.business.server.core.PackageConstructor;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorInstance;
import com.gitee.pifeng.monitoring.server.business.server.service.IAlarmService;
import com.gitee.pifeng.monitoring.server.business.server.service.IInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hyperic.sigar.SigarException;
import org.joda.time.DateTime;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 在项目启动后，定时扫描“MONITOR_INSTANCE”表中的所有应用实例，更新应用实例状态，发送告警。
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/12 12:07
 */
@Slf4j
@Component
@Order(1)
public class InstanceMonitorJob extends QuartzJobBean implements CommandLineRunner {

    /**
     * 告警服务接口
     */
    @Autowired
    private IAlarmService alarmService;

    /**
     * 应用实例服务接口
     */
    @Autowired
    private IInstanceService instanceService;

    /**
     * <p>
     * 项目启动后，先把之前为在线状态的应用“更新时间”设置为当前时间，继续保证在线状态。
     * </p>
     *
     * @param args 传入的主方法参数
     * @author 皮锋
     * @custom.date 2020/4/7 22:00
     */
    @Override
    public void run(String... args) {
        List<MonitorInstance> initMonitorInstances = this.instanceService.list(new LambdaQueryWrapper<>());
        initMonitorInstances.forEach(instance -> {
            boolean isOnline = StringUtils.equals(instance.getIsOnline(), ZeroOrOneConstants.ONE);
            // 在线
            if (isOnline) {
                this.instanceService.updateById(MonitorInstance.builder().id(instance.getId()).updateTime(new Date()).build());
            }
        });
    }

    /**
     * <p>
     * 扫描“MONITOR_INSTANCE”表中的所有应用实例，实时更新应用实例状态，发送告警。
     * </p>
     *
     * @param jobExecutionContext 作业执行上下文
     * @author 皮锋
     * @custom.date 2021/12/3 13:02
     */
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        try {
            // 查询数据库中的所有应用实例信息
            List<MonitorInstance> monitorInstances = this.instanceService.list(new LambdaQueryWrapper<>());
            // 循环所有应用实例
            for (MonitorInstance monitorInstance : monitorInstances) {
                // 允许的误差时间
                int thresholdSecond = monitorInstance.getConnFrequency() * MonitoringConfigPropertiesLoader.getMonitoringProperties().getThreshold();
                // 最后一次通过心跳包更新的时间
                Date dateTime = monitorInstance.getUpdateTime() == null ? monitorInstance.getInsertTime() : monitorInstance.getUpdateTime();
                // 判决时间（在允许的误差时间内，再增加30秒误差）
                DateTime judgeDateTime = new DateTime(dateTime).plusSeconds(thresholdSecond).plusSeconds(30);
                // 注册上来的服务失去响应
                if (judgeDateTime.isBeforeNow()) {
                    // 离线
                    this.offLine(monitorInstance);
                }
                // 注册上来的服务恢复响应
                else {
                    // 恢复在线
                    this.onLine(monitorInstance);
                }
            }
        } catch (Exception e) {
            log.error("定时扫描“MONITOR_INSTANCE”表中的所有应用实例异常！", e);
        }
    }

    /**
     * <p>
     * 处理恢复在线
     * </p>
     *
     * @param instance 实例
     * @author 皮锋
     * @custom.date 2020/3/23 11:58
     */
    private void onLine(MonitorInstance instance) {
        // 是否在线
        boolean isOnline = StringUtils.equals(instance.getIsOnline(), ZeroOrOneConstants.ONE);
        // 离线
        if (!isOnline) {
            try {
                if (StringUtils.isBlank(instance.getIsOnline())) {
                    // 发送发现新的应用程序通知信息
                    this.sendAlarmInfo("发现新应用程序", AlarmLevelEnums.INFO, AlarmReasonEnums.DISCOVERY, instance);
                } else {
                    // 发送在线通知信息
                    this.sendAlarmInfo("应用程序上线", AlarmLevelEnums.INFO, AlarmReasonEnums.ABNORMAL_2_NORMAL, instance);
                }
            } catch (Exception e) {
                log.error("应用实例告警异常！", e);
            }
            instance.setIsOnline(ZeroOrOneConstants.ONE);
            // 更新数据库
            this.instanceService.updateById(instance);
        }
    }

    /**
     * <p>
     * 处理离线
     * </p>
     *
     * @param instance 实例
     * @author 皮锋
     * @custom.date 2020/3/23 11:40
     */
    private void offLine(MonitorInstance instance) {
        // 是否在线
        String isOnlineStr = instance.getIsOnline();
        boolean isOnline = StringUtils.equals(isOnlineStr, ZeroOrOneConstants.ONE);
        // 在线
        if (isOnline) {
            try {
                // 发送离线告警信息
                this.sendAlarmInfo("应用程序离线", AlarmLevelEnums.FATAL, AlarmReasonEnums.NORMAL_2_ABNORMAL, instance);
            } catch (Exception e) {
                log.error("应用实例告警异常！", e);
            }
            // 离线次数 +1
            int offlineCount = instance.getOfflineCount() == null ? 0 : instance.getOfflineCount();
            instance.setOfflineCount(offlineCount + 1);
            instance.setIsOnline(ZeroOrOneConstants.ZERO);
            // 更新数据库
            this.instanceService.updateById(instance);
        }
    }

    /**
     * <p>
     * 发送告警信息
     * </p>
     *
     * @param title           告警标题
     * @param alarmLevelEnum  告警级别
     * @param alarmReasonEnum 告警原因
     * @param instance        应用实例详情
     * @throws NetException   获取网络信息异常
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020/3/13 11:20
     */
    private void sendAlarmInfo(String title, AlarmLevelEnums alarmLevelEnum, AlarmReasonEnums alarmReasonEnum, MonitorInstance instance) throws NetException, SigarException {
        StringBuilder builder = new StringBuilder();
        builder.append("应用ID：").append(instance.getInstanceId())
                .append("，<br>应用名称：").append(instance.getInstanceName());
        // 应用实例描述
        String instanceDesc = instance.getInstanceDesc();
        if (StringUtils.isNotBlank(instanceDesc)) {
            // 应用实例摘要
            String instanceSummary = instance.getInstanceSummary();
            // 如果应用实例摘要不为空，则把摘要当做描述。因为：摘要是用户通过UI界面设置的，优先级比描述高。
            if (StringUtils.isNotBlank(instanceSummary)) {
                builder.append("，<br>应用描述：").append(instanceSummary);
            } else {
                builder.append("，<br>应用描述：").append(instanceDesc);
            }
        }
        builder.append("，<br>应用端点：").append(instance.getEndpoint())
                .append("，<br>IP地址：").append(instance.getIp())
                .append("，<br>时间：").append(DateTimeUtils.dateToString(new Date()));
        Alarm alarm = Alarm.builder()
                // 保证code的唯一性
                .code(instance.getInstanceId())
                .title(title)
                .msg(builder.toString())
                .alarmLevel(alarmLevelEnum)
                .alarmReason(alarmReasonEnum)
                .monitorType(MonitorTypeEnums.INSTANCE)
                .build();
        AlarmPackage alarmPackage = new PackageConstructor().structureAlarmPackage(alarm);
        this.alarmService.dealAlarmPackage(alarmPackage);
    }

}
