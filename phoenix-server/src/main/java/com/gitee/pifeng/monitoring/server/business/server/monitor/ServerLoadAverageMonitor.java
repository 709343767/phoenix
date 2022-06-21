package com.gitee.pifeng.monitoring.server.business.server.monitor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gitee.pifeng.monitoring.common.constant.AlarmLevelEnums;
import com.gitee.pifeng.monitoring.common.constant.AlarmReasonEnums;
import com.gitee.pifeng.monitoring.common.constant.MonitorTypeEnums;
import com.gitee.pifeng.monitoring.common.domain.Alarm;
import com.gitee.pifeng.monitoring.common.dto.AlarmPackage;
import com.gitee.pifeng.monitoring.common.exception.NetException;
import com.gitee.pifeng.monitoring.common.util.DateTimeUtils;
import com.gitee.pifeng.monitoring.common.util.Md5Utils;
import com.gitee.pifeng.monitoring.server.business.server.core.MonitoringConfigPropertiesLoader;
import com.gitee.pifeng.monitoring.server.business.server.core.PackageConstructor;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServer;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServerLoadAverage;
import com.gitee.pifeng.monitoring.server.business.server.service.IAlarmService;
import com.gitee.pifeng.monitoring.server.business.server.service.IServerLoadAverageService;
import com.gitee.pifeng.monitoring.server.business.server.service.IServerService;
import com.gitee.pifeng.monitoring.server.inf.IServerMonitoringListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hyperic.sigar.SigarException;
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
        boolean isEnable = MonitoringConfigPropertiesLoader.getMonitoringProperties().getServerProperties().isEnable();
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
        // 过载阈值
        double overloadThreshold = MonitoringConfigPropertiesLoader.getMonitoringProperties().getServerProperties().getServerLoadAverageProperties().getOverloadThreshold();
        // 1分钟负载平均值
        Double loadAverage1minute = monitorServerLoadAverage.getOne();
        // 5分钟负载平均值
        Double loadAverage5minute = monitorServerLoadAverage.getFive();
        // 15分钟负载平均值
        double loadAverage15minute = monitorServerLoadAverage.getFifteen();
        // 平均负载大于等于配置的过载阈值
        if (loadAverage15minute >= overloadThreshold) {
            // 处理平均负载过载
            this.dealLoadAverageOverLoad(ip, loadAverage1minute, loadAverage5minute, loadAverage15minute);
        } else {
            // 处理平均负载正常
            this.dealLoadAverageNotOverLoad(ip, loadAverage1minute, loadAverage5minute, loadAverage15minute);
        }
    }

    /**
     * <p>
     * 处理平均负载正常
     * </p>
     *
     * @param ip                  IP地址
     * @param loadAverage1minute  1分钟平均负载
     * @param loadAverage5minute  5分钟平均负载
     * @param loadAverage15minute 15分钟平均负载
     * @author 皮锋
     * @custom.date 2022/6/21 22:06
     */
    private void dealLoadAverageNotOverLoad(String ip, Double loadAverage1minute, Double loadAverage5minute, double loadAverage15minute) {
        MonitorServer monitorServer = this.serverService.getOne(new LambdaQueryWrapper<MonitorServer>().eq(MonitorServer::getIp, ip));
        String serverName = monitorServer.getServerName();
        String serverSummary = monitorServer.getServerSummary();
        // 发送告警信息
        try {
            this.sendAlarmInfo("服务器15分钟负载恢复正常",
                    ip,
                    serverName,
                    serverSummary,
                    loadAverage1minute,
                    loadAverage5minute,
                    loadAverage15minute,
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
     * @param ip                  IP地址
     * @param loadAverage1minute  1分钟平均负载
     * @param loadAverage5minute  5分钟平均负载
     * @param loadAverage15minute 15分钟平均负载
     * @author 皮锋
     * @custom.date 2022/6/21 21:47
     */
    private void dealLoadAverageOverLoad(String ip, double loadAverage1minute, double loadAverage5minute, double loadAverage15minute) {
        MonitorServer monitorServer = this.serverService.getOne(new LambdaQueryWrapper<MonitorServer>().eq(MonitorServer::getIp, ip));
        String serverName = monitorServer.getServerName();
        String serverSummary = monitorServer.getServerSummary();
        // 告警级别
        AlarmLevelEnums alarmLevelEnum = MonitoringConfigPropertiesLoader.getMonitoringProperties().getServerProperties().getServerLoadAverageProperties().getLevelEnum();
        // 发送告警信息
        try {
            this.sendAlarmInfo("服务器15分钟负载过载",
                    ip,
                    serverName,
                    serverSummary,
                    loadAverage1minute,
                    loadAverage5minute,
                    loadAverage15minute,
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
     * @param title               告警标题
     * @param ip                  IP地址
     * @param serverName          服务器名
     * @param serverSummary       服务器摘要
     * @param loadAverage1minute  1分钟平均负载
     * @param loadAverage5minute  5分钟平均负载
     * @param loadAverage15minute 15分钟平均负载
     * @param alarmLevelEnum      告警级别
     * @param alarmReasonEnum     告警原因
     * @throws NetException   获取网络信息异常
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020/3/25 14:46
     */
    private void sendAlarmInfo(String title, String ip, String serverName, String serverSummary,
                               double loadAverage1minute, double loadAverage5minute, double loadAverage15minute,
                               AlarmLevelEnums alarmLevelEnum, AlarmReasonEnums alarmReasonEnum) throws NetException, SigarException {
        StringBuilder msgBuilder = new StringBuilder();
        msgBuilder.append("IP地址：").append(ip).append("，<br>服务器：").append(serverName);
        if (StringUtils.isNotBlank(serverSummary)) {
            msgBuilder.append("，<br>服务器描述：").append(serverSummary);
        }
        msgBuilder.append("，<br>1分钟平均负载：").append(loadAverage1minute)
                .append("，<br>5分钟平均负载：").append(loadAverage5minute)
                .append("，<br>15分钟平均负载：").append(loadAverage15minute)
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
        AlarmPackage alarmPackage = new PackageConstructor().structureAlarmPackage(alarm);
        this.alarmService.dealAlarmPackage(alarmPackage);
    }

}
