package com.gitee.pifeng.monitoring.server.business.server.monitor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gitee.pifeng.monitoring.common.constant.MonitorTypeEnums;
import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmLevelEnums;
import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmReasonEnums;
import com.gitee.pifeng.monitoring.common.domain.Alarm;
import com.gitee.pifeng.monitoring.common.dto.AlarmPackage;
import com.gitee.pifeng.monitoring.common.exception.NetException;
import com.gitee.pifeng.monitoring.common.util.DateTimeUtils;
import com.gitee.pifeng.monitoring.common.util.Md5Utils;
import com.gitee.pifeng.monitoring.server.business.server.core.MonitoringConfigPropertiesLoader;
import com.gitee.pifeng.monitoring.server.business.server.core.ServerPackageConstructor;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServer;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServerLoadAverage;
import com.gitee.pifeng.monitoring.server.business.server.service.IAlarmService;
import com.gitee.pifeng.monitoring.server.business.server.service.IServerLoadAverageService;
import com.gitee.pifeng.monitoring.server.business.server.service.IServerService;
import com.gitee.pifeng.monitoring.server.inf.IServerMonitoringListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * <p>
 * 监控服务器平均负载信息，实时更新状态，发送告警
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/6/21 21:24
 */
@Component
@Slf4j
public class ServerLoadAverageMonitor implements IServerMonitoringListener {

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
     * 服务器平均负载服务层接口
     */
    @Autowired
    private IServerLoadAverageService serverLoadAverageService;

    /**
     * 服务器信息服务层接口
     */
    @Autowired
    private IServerService serverService;

    /**
     * <p>
     * 监控服务器平均负载，发送过载告警信息
     * </p>
     *
     * @param obj 回调参数
     * @author 皮锋
     * @custom.date 2020/3/30 20:18
     */
    @Override
    public void wakeUpMonitor(Object... obj) {
        // 是否监控服务器
        boolean isEnable = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getServerProperties().isEnable();
        // 不需要监控服务器
        if (!isEnable) {
            return;
        }
        String ip = String.valueOf(obj[0]);
        // 查询此IP的服务器平均负载信息
        MonitorServerLoadAverage monitorServerLoadAverage = this.serverLoadAverageService.getOne(new LambdaQueryWrapper<MonitorServerLoadAverage>().eq(MonitorServerLoadAverage::getIp, ip));
        if (monitorServerLoadAverage == null) {
            return;
        }
        // 15分钟过载阈值
        double overloadThreshold15minutes = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getServerProperties().getServerLoadAverageProperties().getOverloadThreshold15minutes();
        // CPU逻辑核数量
        Integer logicalProcessorCount = monitorServerLoadAverage.getLogicalProcessorCount();
        // 1分钟负载平均值
        Double loadAverage1minutes = monitorServerLoadAverage.getOne();
        // 5分钟负载平均值
        Double loadAverage5minutes = monitorServerLoadAverage.getFive();
        // 15分钟负载平均值
        double loadAverage15minutes = monitorServerLoadAverage.getFifteen();
        // 平均负载大于等于配置的过载阈值
        if (loadAverage15minutes >= overloadThreshold15minutes * logicalProcessorCount) {
            // 处理平均负载过载
            this.dealLoadAverageOverLoad(ip, logicalProcessorCount, loadAverage1minutes, loadAverage5minutes, loadAverage15minutes);
        } else {
            // 处理平均负载正常
            this.dealLoadAverageNotOverLoad(ip, logicalProcessorCount, loadAverage1minutes, loadAverage5minutes, loadAverage15minutes);
        }
    }

    /**
     * <p>
     * 处理平均负载正常
     * </p>
     *
     * @param ip                    IP地址
     * @param loadAverage1minutes   1分钟平均负载
     * @param loadAverage5minutes   5分钟平均负载
     * @param loadAverage15minutes  15分钟平均负载
     * @param logicalProcessorCount CPU逻辑核数量
     * @author 皮锋
     * @custom.date 2022/6/21 22:06
     */
    private void dealLoadAverageNotOverLoad(String ip, Integer logicalProcessorCount,
                                            Double loadAverage1minutes, Double loadAverage5minutes, double loadAverage15minutes) {
        MonitorServer monitorServer = this.serverService.getOne(new LambdaQueryWrapper<MonitorServer>().eq(MonitorServer::getIp, ip));
        if (monitorServer == null) {
            return;
        }
        String serverName = monitorServer.getServerName();
        String serverSummary = monitorServer.getServerSummary();
        // 发送告警信息
        try {
            this.sendAlarmInfo("服务器15分钟负载恢复正常",
                    ip,
                    serverName,
                    serverSummary,
                    logicalProcessorCount,
                    loadAverage1minutes,
                    loadAverage5minutes,
                    loadAverage15minutes,
                    AlarmLevelEnums.INFO,
                    AlarmReasonEnums.ABNORMAL_2_NORMAL);
        } catch (Exception e) {
            log.error("服务器15分钟负载恢复正常告警异常！", e);
        }
    }

    /**
     * <p>
     * 处理平均负载过载
     * </p>
     *
     * @param ip                    IP地址
     * @param loadAverage1minutes   1分钟平均负载
     * @param loadAverage5minutes   5分钟平均负载
     * @param loadAverage15minutes  15分钟平均负载
     * @param logicalProcessorCount CPU逻辑核数量
     * @author 皮锋
     * @custom.date 2022/6/21 21:47
     */
    private void dealLoadAverageOverLoad(String ip, Integer logicalProcessorCount,
                                         double loadAverage1minutes, double loadAverage5minutes, double loadAverage15minutes) {
        MonitorServer monitorServer = this.serverService.getOne(new LambdaQueryWrapper<MonitorServer>().eq(MonitorServer::getIp, ip));
        if (monitorServer == null) {
            return;
        }
        String serverName = monitorServer.getServerName();
        String serverSummary = monitorServer.getServerSummary();
        // 告警级别
        AlarmLevelEnums alarmLevelEnum = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getServerProperties().getServerLoadAverageProperties().getLevelEnum15minutes();
        // 发送告警信息
        try {
            this.sendAlarmInfo("服务器15分钟负载过载",
                    ip,
                    serverName,
                    serverSummary,
                    logicalProcessorCount,
                    loadAverage1minutes,
                    loadAverage5minutes,
                    loadAverage15minutes,
                    alarmLevelEnum,
                    AlarmReasonEnums.NORMAL_2_ABNORMAL);
        } catch (Exception e) {
            log.error("服务器15分钟负载过载告警异常！", e);
        }
    }

    /**
     * <p>
     * 发送告警信息
     * </p>
     *
     * @param title                 告警标题
     * @param ip                    IP地址
     * @param serverName            服务器名
     * @param serverSummary         服务器摘要
     * @param loadAverage1minutes   1分钟平均负载
     * @param loadAverage5minutes   5分钟平均负载
     * @param loadAverage15minutes  15分钟平均负载
     * @param alarmLevelEnum        告警级别
     * @param alarmReasonEnum       告警原因
     * @param logicalProcessorCount CPU逻辑核数量
     * @throws NetException 获取网络信息异常
     * @author 皮锋
     * @custom.date 2020/3/25 14:46
     */
    private void sendAlarmInfo(String title, String ip, String serverName, String serverSummary, Integer logicalProcessorCount,
                               double loadAverage1minutes, double loadAverage5minutes, double loadAverage15minutes,
                               AlarmLevelEnums alarmLevelEnum, AlarmReasonEnums alarmReasonEnum) throws NetException {
        StringBuilder msgBuilder = new StringBuilder();
        msgBuilder.append("IP地址：").append(ip).append("，<br>服务器：").append(serverName);
        if (StringUtils.isNotBlank(serverSummary)) {
            msgBuilder.append("，<br>服务器描述：").append(serverSummary);
        }
        msgBuilder.append("，<br>CPU逻辑核数：").append(logicalProcessorCount)
                .append("，<br>1分钟平均负载：").append(loadAverage1minutes)
                .append("，<br>5分钟平均负载：").append(loadAverage5minutes)
                .append("，<br>15分钟平均负载：").append(loadAverage15minutes)
                .append("，<br>时间：").append(DateTimeUtils.dateToString(new Date()));
        Alarm alarm = Alarm.builder()
                // 保证code的唯一性
                .code(Md5Utils.encrypt32(ip + serverName + ServerLoadAverageMonitor.class.getName()))
                .title(title)
                .msg(msgBuilder.toString())
                .alarmLevel(alarmLevelEnum)
                .alarmReason(alarmReasonEnum)
                .monitorType(MonitorTypeEnums.SERVER)
                .build();
        AlarmPackage alarmPackage = this.serverPackageConstructor.structureAlarmPackage(alarm);
        this.alarmService.dealAlarmPackage(alarmPackage);
    }

}
