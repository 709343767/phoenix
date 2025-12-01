package com.gitee.pifeng.monitoring.server.business.server.monitor.instance;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gitee.pifeng.monitoring.common.constant.ZeroOrOneConstants;
import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmLevelEnums;
import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmReasonEnums;
import com.gitee.pifeng.monitoring.common.constant.monitortype.MonitorSubTypeEnums;
import com.gitee.pifeng.monitoring.common.constant.monitortype.MonitorTypeEnums;
import com.gitee.pifeng.monitoring.common.domain.Alarm;
import com.gitee.pifeng.monitoring.common.dto.AlarmPackage;
import com.gitee.pifeng.monitoring.common.dto.OfflinePackage;
import com.gitee.pifeng.monitoring.common.exception.NetException;
import com.gitee.pifeng.monitoring.common.util.DateTimeUtils;
import com.gitee.pifeng.monitoring.plug.core.InstanceGenerator;
import com.gitee.pifeng.monitoring.server.business.server.component.OfflineAspect;
import com.gitee.pifeng.monitoring.server.business.server.core.MonitoringConfigPropertiesLoader;
import com.gitee.pifeng.monitoring.server.business.server.core.ServerPackageConstructor;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorInstance;
import com.gitee.pifeng.monitoring.server.business.server.service.IAlarmService;
import com.gitee.pifeng.monitoring.server.business.server.service.IInstanceService;
import com.gitee.pifeng.monitoring.server.constant.ComponentOrderConstants;
import com.gitee.pifeng.monitoring.server.inf.IOfflineListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.joda.time.DateTime;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
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
@Order(ComponentOrderConstants.INSTANCE + 1)
@DisallowConcurrentExecution
public class InstanceMonitorJob extends QuartzJobBean implements CommandLineRunner, DisposableBean, IOfflineListener {

    /**
     * 监控配置属性加载器
     */
    @Autowired
    private MonitoringConfigPropertiesLoader monitoringConfigPropertiesLoader;

    /**
     * 服务端包构造器
     */
    @Autowired
    private ServerPackageConstructor serverPackageConstructor;

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
     * {@link InstanceMonitorJob#run(String...)}这个方法是否已经运行，<br>
     * 静态变量是类级别的变量，因此所有该类的实例对象共享。
     */
    private static volatile boolean commandLineRunnerHasRun = false;

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
                MonitorInstance monitorInstance = MonitorInstance.builder()
                        .id(instance.getId())
                        .updateTime(new Date())
                        .isOfflineNotice(ZeroOrOneConstants.ZERO)
                        .build();
                this.instanceService.updateById(monitorInstance);
            }
        });
        commandLineRunnerHasRun = true;
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
    protected void executeInternal(@NonNull JobExecutionContext jobExecutionContext) {
        if (!commandLineRunnerHasRun) {
            return;
        }
        // 是否监控应用实例
        boolean isEnable = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getInstanceProperties().isEnable();
        if (!isEnable) {
            return;
        }
        // 是否监控应用实例状态
        boolean isStatusEnable = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getInstanceProperties().getInstanceStatusProperties().isEnable();
        if (!isStatusEnable) {
            return;
        }
        synchronized (InstanceMonitorJob.class) {
            try {
                // 查询数据库中的所有应用实例信息
                List<MonitorInstance> monitorInstances = this.instanceService.list(new LambdaQueryWrapper<>());
                // 循环所有应用实例
                for (MonitorInstance monitorInstance : monitorInstances) {
                    // 是否开启监控（0：不开启监控；1：开启监控）
                    String isEnableMonitor = monitorInstance.getIsEnableMonitor();
                    // 没有开启监控，直接跳过
                    if (!StringUtils.equals(ZeroOrOneConstants.ONE, isEnableMonitor)) {
                        continue;
                    }
                    // 允许的误差时间
                    int thresholdSecond = monitorInstance.getConnFrequency() * this.monitoringConfigPropertiesLoader.getMonitoringProperties().getThreshold();
                    // 最后一次通过心跳包更新的时间
                    Date dateTime = monitorInstance.getUpdateTime() == null ? monitorInstance.getInsertTime() : monitorInstance.getUpdateTime();
                    // 判决时间（在允许的误差时间内，再增加30秒误差）
                    DateTime judgeDateTime = new DateTime(dateTime).plusSeconds(thresholdSecond).plusSeconds(30);
                    // 注册上来的服务失去响应
                    if (judgeDateTime.isBeforeNow()) {
                        // 离线
                        this.offLine(monitorInstance, false);
                    }
                    // 注册上来的服务恢复响应
                    else {
                        // 没有收到离线通知（对于收到下线信息包而离线的应用，其最后心跳时间可能还小于判决时间）
                        if (StringUtils.equals(monitorInstance.getIsOfflineNotice(), ZeroOrOneConstants.ZERO)) {
                            // 恢复在线
                            this.onLine(monitorInstance);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("定时扫描“MONITOR_INSTANCE”表中的所有应用实例异常！", e);
            }
        }
    }

    /**
     * <p>
     * 在bean被销毁之前，通知此应用实例离线
     * </p>
     *
     * @author 皮锋
     * @custom.date 2023/6/5 14:50
     */
    @Override
    public void destroy() {
        if (!commandLineRunnerHasRun) {
            return;
        }
        // 是否监控应用实例
        boolean isEnable = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getInstanceProperties().isEnable();
        if (!isEnable) {
            return;
        }
        // 是否监控应用实例状态
        boolean isStatusEnable = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getInstanceProperties().getInstanceStatusProperties().isEnable();
        if (!isStatusEnable) {
            return;
        }
        // 应用ID
        String instanceId = InstanceGenerator.getInstanceId();
        // 从数据库中查询到此实例
        LambdaQueryWrapper<MonitorInstance> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MonitorInstance::getInstanceId, instanceId);
        MonitorInstance monitorInstance = this.instanceService.getOne(lambdaQueryWrapper);
        if (monitorInstance == null) {
            return;
        }
        // 是否开启监控（0：不开启监控；1：开启监控）
        String isEnableMonitor = monitorInstance.getIsEnableMonitor();
        // 没有开启监控，直接跳过
        if (!StringUtils.equals(ZeroOrOneConstants.ONE, isEnableMonitor)) {
            return;
        }
        // 离线
        this.offLine(monitorInstance, true);
        log.info("应用程序下线！");
    }

    /**
     * <p>
     * 收到下线信息包时，唤醒执行监控回调方法，通知下线。
     * </p>
     * 此方法在{@link OfflineAspect#beforeWakeUp(JoinPoint)}中被注册监听。
     *
     * @param offlinePackage 下线信息包
     * @author 皮锋
     * @custom.date 2023/6/1 8:50
     */
    @Override
    public void notifyOffline(OfflinePackage offlinePackage) {
        if (!commandLineRunnerHasRun) {
            return;
        }
        // 是否监控应用实例
        boolean isEnable = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getInstanceProperties().isEnable();
        if (!isEnable) {
            return;
        }
        // 是否监控应用实例状态
        boolean isStatusEnable = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getInstanceProperties().getInstanceStatusProperties().isEnable();
        if (!isStatusEnable) {
            return;
        }
        // 离线类型
        List<MonitorTypeEnums> monitorTypes = offlinePackage.getMonitorTypes();
        // 是应用实例离线
        if (CollectionUtil.contains(monitorTypes, MonitorTypeEnums.INSTANCE)) {
            // 应用实例ID
            String instanceId = offlinePackage.getInstanceId();
            // 从数据库中查询到此实例
            LambdaQueryWrapper<MonitorInstance> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(MonitorInstance::getInstanceId, instanceId);
            MonitorInstance monitorInstance = this.instanceService.getOne(lambdaQueryWrapper);
            if (monitorInstance == null) {
                return;
            }
            // 是否开启监控（0：不开启监控；1：开启监控）
            String isEnableMonitor = monitorInstance.getIsEnableMonitor();
            // 没有开启监控，直接跳过
            if (!StringUtils.equals(ZeroOrOneConstants.ONE, isEnableMonitor)) {
                return;
            }
            // 离线
            this.offLine(monitorInstance, true);
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
                    // 发送发现新的应用实例通知信息
                    this.sendAlarmInfo("发现新应用程序", AlarmLevelEnums.INFO, AlarmReasonEnums.DISCOVERY, instance);
                } else {
                    // 发送在线通知信息
                    this.sendAlarmInfo("应用程序上线", AlarmLevelEnums.INFO, AlarmReasonEnums.ABNORMAL_2_NORMAL, instance);
                }
            } catch (Exception e) {
                log.error("应用程序告警异常！", e);
            }
            instance.setIsOnline(ZeroOrOneConstants.ONE);
            instance.setIsOfflineNotice(ZeroOrOneConstants.ZERO);
            // 更新数据库
            this.instanceService.updateById(instance);
        }
    }

    /**
     * <p>
     * 处理离线
     * </p>
     *
     * @param instance        实例
     * @param isOfflineNotice 是否收到离线通知
     * @author 皮锋
     * @custom.date 2020/3/23 11:40
     */
    private void offLine(MonitorInstance instance, boolean isOfflineNotice) {
        // 是否在线
        String isOnlineStr = instance.getIsOnline();
        boolean isOnline = StringUtils.equals(isOnlineStr, ZeroOrOneConstants.ONE);
        // 在线
        if (isOnline) {
            try {
                // 发送离线告警信息
                this.sendAlarmInfo("应用程序离线", AlarmLevelEnums.FATAL, AlarmReasonEnums.NORMAL_2_ABNORMAL, instance);
            } catch (Exception e) {
                log.error("应用程序告警异常！", e);
            }
            // 离线次数 +1
            int offlineCount = instance.getOfflineCount() == null ? 0 : instance.getOfflineCount();
            instance.setOfflineCount(offlineCount + 1);
            instance.setIsOnline(ZeroOrOneConstants.ZERO);
            instance.setIsOfflineNotice(isOfflineNotice ? ZeroOrOneConstants.ONE : ZeroOrOneConstants.ZERO);
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
     * @throws NetException 获取网络信息异常
     * @author 皮锋
     * @custom.date 2020/3/13 11:20
     */
    private void sendAlarmInfo(String title, AlarmLevelEnums alarmLevelEnum, AlarmReasonEnums alarmReasonEnum, MonitorInstance instance) throws NetException {
        // 告警是否打开
        boolean alarmEnable = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getInstanceProperties().getInstanceStatusProperties().isAlarmEnable();
        if (!alarmEnable) {
            return;
        }
        // 是否开启告警（0：不开启告警；1：开启告警）
        String isEnableAlarm = instance.getIsEnableAlarm();
        // 没有开启告警，直接结束
        if (!StringUtils.equals(ZeroOrOneConstants.ONE, isEnableAlarm)) {
            return;
        }
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
                .monitorSubType(MonitorSubTypeEnums.SERVICE_STATUS)
                .alertedEntityId(String.valueOf(instance.getId()))
                .build();
        AlarmPackage alarmPackage = this.serverPackageConstructor.structureAlarmPackage(alarm);
        this.alarmService.dealAlarmPackage(alarmPackage);
    }

}
