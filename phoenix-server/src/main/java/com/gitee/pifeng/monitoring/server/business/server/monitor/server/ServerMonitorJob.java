package com.gitee.pifeng.monitoring.server.business.server.monitor.server;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gitee.pifeng.monitoring.common.constant.ZeroOrOneConstants;
import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmLevelEnums;
import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmReasonEnums;
import com.gitee.pifeng.monitoring.common.constant.monitortype.MonitorSubTypeEnums;
import com.gitee.pifeng.monitoring.common.constant.monitortype.MonitorTypeEnums;
import com.gitee.pifeng.monitoring.common.domain.Alarm;
import com.gitee.pifeng.monitoring.common.dto.AlarmPackage;
import com.gitee.pifeng.monitoring.common.exception.NetException;
import com.gitee.pifeng.monitoring.common.util.DateTimeUtils;
import com.gitee.pifeng.monitoring.common.util.Md5Utils;
import com.gitee.pifeng.monitoring.server.business.server.core.MonitoringConfigPropertiesLoader;
import com.gitee.pifeng.monitoring.server.business.server.core.ServerPackageConstructor;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServer;
import com.gitee.pifeng.monitoring.server.business.server.service.IAlarmService;
import com.gitee.pifeng.monitoring.server.business.server.service.IServerService;
import com.gitee.pifeng.monitoring.server.constant.ComponentOrderConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
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
 * 在项目启动后，定时扫描“MONITOR_SERVER”表中的所有服务器，更新应用服务器状态，发送告警。
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/12/17 20:24
 */
@Component
@Slf4j
@Order(ComponentOrderConstants.SERVER + 1)
@DisallowConcurrentExecution
public class ServerMonitorJob extends QuartzJobBean implements CommandLineRunner {

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
     * 服务器信息服务层接口
     */
    @Autowired
    private IServerService serverService;

    /**
     * {@link ServerMonitorJob#run(String...)}这个方法是否已经运行，<br>
     * 静态变量是类级别的变量，因此所有该类的实例对象共享。
     */
    private static volatile boolean commandLineRunnerHasRun = false;

    /**
     * <p>
     * 项目启动后，先把之前为在线状态的服务器“更新时间”设置为当前时间，继续保证在线状态。
     * </p>
     *
     * @param args 传入的主方法参数
     * @author 皮锋
     * @custom.date 2020/4/7 22:00
     */
    @Override
    public void run(String... args) {
        List<MonitorServer> initMonitorServers = this.serverService.list(new LambdaQueryWrapper<>());
        initMonitorServers.forEach(initMonitorServer -> {
            // 在线
            boolean isOnline = StringUtils.equals(initMonitorServer.getIsOnline(), ZeroOrOneConstants.ONE);
            if (isOnline) {
                this.serverService.updateById(MonitorServer.builder().id(initMonitorServer.getId()).updateTime(new Date()).build());
            }
        });
        commandLineRunnerHasRun = true;
    }

    /**
     * <p>
     * 扫描“MONITOR_SERVER”表中的所有服务器，实时更新服务器状态，发送告警。
     * </p>
     *
     * @param jobExecutionContext 作业执行上下文
     * @author 皮锋
     * @custom.date 2021/12/3 13:13
     */
    @Override
    protected void executeInternal(@NonNull JobExecutionContext jobExecutionContext) {
        if (!commandLineRunnerHasRun) {
            return;
        }
        // 是否监控服务器
        boolean isEnable = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getServerProperties().isEnable();
        // 不需要监控服务器
        if (!isEnable) {
            return;
        }
        // 是否监控服务器状态
        boolean isStatusEnable = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getServerProperties().getServerStatusProperties().isEnable();
        if (!isStatusEnable) {
            return;
        }
        synchronized (ServerMonitorJob.class) {
            try {
                // 查询数据库中的所有服务器
                List<MonitorServer> monitorServers = this.serverService.list(new LambdaQueryWrapper<>());
                // 循环所有服务器
                for (MonitorServer monitorServer : monitorServers) {
                    // 是否开启监控（0：不开启监控；1：开启监控）
                    String isEnableMonitor = monitorServer.getIsEnableMonitor();
                    // 没有开启监控，直接跳过
                    if (!StringUtils.equals(ZeroOrOneConstants.ONE, isEnableMonitor)) {
                        continue;
                    }
                    // 允许的误差时间
                    int thresholdSecond = monitorServer.getConnFrequency() * this.monitoringConfigPropertiesLoader.getMonitoringProperties().getThreshold();
                    // 最后一次通过服务器信息包更新的时间
                    Date dateTime = monitorServer.getUpdateTime() == null ? monitorServer.getInsertTime() : monitorServer.getUpdateTime();
                    // 判决时间（在允许的误差时间内，再增加30秒误差）
                    DateTime judgeDateTime = new DateTime(dateTime).plusSeconds(thresholdSecond).plusSeconds(30);
                    // 注册上来的服务器失去响应
                    if (judgeDateTime.isBeforeNow()) {
                        // 离线
                        this.offLine(monitorServer);
                    }
                    // 注册上来的服务器恢复响应
                    else {
                        // 恢复在线
                        this.onLine(monitorServer);
                    }
                }
            } catch (Exception e) {
                log.error("定时扫描“MONITOR_SERVER”表中的所有服务器异常！", e);
            }
        }
    }

    /**
     * <p>
     * 处理恢复在线
     * </p>
     *
     * @param server 服务器
     * @author 皮锋
     * @custom.date 2020/3/23 11:58
     */
    private void onLine(MonitorServer server) {
        // 是否在线
        boolean isOnline = StringUtils.equals(server.getIsOnline(), ZeroOrOneConstants.ONE);
        // 离线
        if (!isOnline) {
            try {
                if (StringUtils.isBlank(server.getIsOnline())) {
                    // 发送发现新的服务器通知信息
                    this.sendAlarmInfo("发现新服务器", AlarmLevelEnums.INFO, AlarmReasonEnums.DISCOVERY, server);
                } else {
                    // 发送在线通知信息
                    this.sendAlarmInfo("服务器上线", AlarmLevelEnums.INFO, AlarmReasonEnums.ABNORMAL_2_NORMAL, server);
                }
            } catch (Exception e) {
                log.error("服务器告警异常！", e);
            }
            server.setIsOnline(ZeroOrOneConstants.ONE);
            // 更新数据库
            this.serverService.updateById(server);
        }
    }

    /**
     * <p>
     * 处理离线
     * </p>
     *
     * @param server 服务器
     * @author 皮锋
     * @custom.date 2020/3/23 11:40
     */
    private void offLine(MonitorServer server) {
        // 是否在线
        String isOnlineStr = server.getIsOnline();
        boolean isOnline = StringUtils.equals(isOnlineStr, ZeroOrOneConstants.ONE);
        // 在线
        if (isOnline) {
            try {
                // 发送离线告警信息
                this.sendAlarmInfo("服务器离线", AlarmLevelEnums.FATAL, AlarmReasonEnums.NORMAL_2_ABNORMAL, server);
            } catch (Exception e) {
                log.error("服务器告警异常！", e);
            }
            // 离线次数 +1
            int offlineCount = server.getOfflineCount() == null ? 0 : server.getOfflineCount();
            server.setOfflineCount(offlineCount + 1);
            server.setIsOnline(ZeroOrOneConstants.ZERO);
            // 更新数据库
            this.serverService.updateById(server);
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
     * @param server          服务器
     * @throws NetException 自定义获取网络信息异常
     * @author 皮锋
     * @custom.date 2020/12/17 20:32
     */
    private void sendAlarmInfo(String title, AlarmLevelEnums alarmLevelEnum, AlarmReasonEnums alarmReasonEnum, MonitorServer server) throws NetException {
        // 告警是否打开
        boolean alarmEnable = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getServerProperties().getServerStatusProperties().isAlarmEnable();
        if (!alarmEnable) {
            return;
        }
        // 是否开启告警（0：不开启告警；1：开启告警）
        String isEnableAlarm = server.getIsEnableAlarm();
        // 没有开启告警，直接结束
        if (!StringUtils.equals(ZeroOrOneConstants.ONE, isEnableAlarm)) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("IP地址：").append(server.getIp()).append("，<br>服务器：").append(server.getServerName());
        String serverSummary = server.getServerSummary();
        if (StringUtils.isNotBlank(serverSummary)) {
            stringBuilder.append("，<br>服务器描述：").append(serverSummary);
        }
        stringBuilder.append("，<br>时间：").append(DateTimeUtils.dateToString(new Date()));
        Alarm alarm = Alarm.builder()
                // 保证code的唯一性
                .code(Md5Utils.encrypt32(server.getIp() + server.getServerName() + ServerMonitorJob.class.getName()))
                .title(title)
                .msg(stringBuilder.toString())
                .alarmLevel(alarmLevelEnum)
                .alarmReason(alarmReasonEnum)
                .monitorType(MonitorTypeEnums.SERVER)
                .monitorSubType(MonitorSubTypeEnums.SERVICE_STATUS)
                .alertedEntityId(String.valueOf(server.getId()))
                .build();
        AlarmPackage alarmPackage = this.serverPackageConstructor.structureAlarmPackage(alarm);
        this.alarmService.dealAlarmPackage(alarmPackage);
    }

}
