package com.gitee.pifeng.monitoring.server.business.server.monitor;

import cn.hutool.core.util.NumberUtil;
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
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServerCpu;
import com.gitee.pifeng.monitoring.server.business.server.service.IAlarmService;
import com.gitee.pifeng.monitoring.server.business.server.service.IServerCpuService;
import com.gitee.pifeng.monitoring.server.business.server.service.IServerService;
import com.gitee.pifeng.monitoring.server.inf.IServerMonitoringListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 监控服务器CPU信息，实时更新状态，发送告警
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/27 16:07
 */
@Component
@Slf4j
public class ServerCpuMonitor implements IServerMonitoringListener {

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
     * 服务器CPU信息服务层接口
     */
    @Autowired
    private IServerCpuService serverCpuService;

    /**
     * 服务器信息服务层接口
     */
    @Autowired
    private IServerService serverService;

    /**
     * <p>
     * 监控CPU使用率，发送CPU过载告警信息
     * </p>
     *
     * @param obj 回调参数
     * @author 皮锋
     * @custom.date 2020/3/30 21:56
     */
    @Override
    public void wakeUpMonitor(Object... obj) {
        // 是否监控服务器
        boolean isEnable = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getServerProperties().isEnable();
        // 不需要监控服务器
        if (!isEnable) {
            return;
        }
        // 是否监控服务器CPU
        boolean isCpuEnable = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getServerProperties().getServerCpuProperties().isEnable();
        if (!isCpuEnable) {
            return;
        }
        String ip = String.valueOf(obj[0]);
        MonitorServer monitorServer = this.serverService.getOne(new LambdaQueryWrapper<MonitorServer>().eq(MonitorServer::getIp, ip));
        if (monitorServer == null) {
            return;
        }
        // 是否开启监控（0：不开启监控；1：开启监控）
        String isEnableMonitor = monitorServer.getIsEnableMonitor();
        // 没有开启监控，直接结束
        if (!StringUtils.equals(ZeroOrOneConstants.ONE, isEnableMonitor)) {
            return;
        }
        // 查询此IP的服务器CPU信息
        List<MonitorServerCpu> monitorServerCpus = this.serverCpuService.list(new LambdaQueryWrapper<MonitorServerCpu>().eq(MonitorServerCpu::getIp, ip));
        if (CollectionUtils.isEmpty(monitorServerCpus)) {
            return;
        }
        // 过载阈值
        double overloadThreshold = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getServerProperties().getServerCpuProperties().getOverloadThreshold();
        // 计算CPU平均使用率
        double cpuAvgCombined = this.calculateCpuAvgCombined(monitorServerCpus);
        // 平均CPU使用率大于等于配置的过载阈值
        if (cpuAvgCombined >= overloadThreshold) {
            // 处理CPU过载
            this.dealCpuOverLoad(monitorServer, cpuAvgCombined);
        } else {
            // 处理CPU正常
            this.dealCpuNotOverLoad(monitorServer, cpuAvgCombined);
        }
    }

    /**
     * <p>
     * 处理CPU正常
     * </p>
     *
     * @param monitorServer  服务器信息
     * @param cpuAvgCombined CPU平均使用率
     * @author 皮锋
     * @custom.date 2021/2/4 11:28
     */
    private void dealCpuNotOverLoad(MonitorServer monitorServer, double cpuAvgCombined) {
        // 发送告警信息
        try {
            // 不用担心头次检测到CPU正常（非异常转正常）会发送告警，最终是否发送告警由“实时监控服务”决定
            this.sendAlarmInfo("服务器CPU恢复正常", monitorServer, cpuAvgCombined, AlarmLevelEnums.INFO, AlarmReasonEnums.ABNORMAL_2_NORMAL);
        } catch (Exception e) {
            log.error("服务器CPU恢复正常告警异常！", e);
        }
    }

    /**
     * <p>
     * 处理CPU过载
     * </p>
     *
     * @param monitorServer  服务器信息
     * @param cpuAvgCombined CPU平均使用率
     * @author 皮锋
     * @custom.date 2021/2/4 9:44
     */
    private void dealCpuOverLoad(MonitorServer monitorServer, double cpuAvgCombined) {
        // 告警级别
        AlarmLevelEnums alarmLevelEnum = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getServerProperties().getServerCpuProperties().getLevelEnum();
        // 发送告警信息
        try {
            this.sendAlarmInfo("服务器CPU过载", monitorServer, cpuAvgCombined, alarmLevelEnum, AlarmReasonEnums.NORMAL_2_ABNORMAL);
        } catch (Exception e) {
            log.error("服务器CPU过载告警异常！", e);
        }
    }

    /**
     * <p>
     * 计算CPU平均使用率
     * </p>
     *
     * @param monitorServerCpus 服务器CPU
     * @return CPU平均使用率
     * @author 皮锋
     * @custom.date 2021/2/4 9:31
     */
    private double calculateCpuAvgCombined(List<MonitorServerCpu> monitorServerCpus) {
        if (CollectionUtils.isEmpty(monitorServerCpus)) {
            return 0D;
        }
        return NumberUtil.round(monitorServerCpus.stream()
                // 过滤掉为空的元素
                .filter(cpu -> cpu != null && cpu.getCpuCombined() != null)
                .mapToDouble(MonitorServerCpu::getCpuCombined)
                .average()
                .orElse(0D) * 100D, 2)
                .doubleValue();
    }

    /**
     * <p>
     * 发送告警信息
     * </p>
     *
     * @param title           告警标题
     * @param monitorServer   服务器信息
     * @param cpuAvgCombined  CPU平均使用率
     * @param alarmLevelEnum  告警级别
     * @param alarmReasonEnum 告警原因
     * @throws NetException 获取网络信息异常
     * @author 皮锋
     * @custom.date 2021/2/4 10:04
     */
    private void sendAlarmInfo(String title, MonitorServer monitorServer, double cpuAvgCombined,
                               AlarmLevelEnums alarmLevelEnum, AlarmReasonEnums alarmReasonEnum) throws NetException {
        // 告警是否打开
        boolean alarmEnable = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getServerProperties().getServerCpuProperties().isAlarmEnable();
        if (!alarmEnable) {
            return;
        }
        // 是否开启告警（0：不开启告警；1：开启告警）
        String isEnableAlarm = monitorServer.getIsEnableAlarm();
        // 没有开启告警，直接结束
        if (!StringUtils.equals(ZeroOrOneConstants.ONE, isEnableAlarm)) {
            return;
        }
        String ip = monitorServer.getIp();
        String serverName = monitorServer.getServerName();
        String serverSummary = monitorServer.getServerSummary();
        StringBuilder msgBuilder = new StringBuilder();
        msgBuilder.append("IP地址：").append(ip).append("，<br>服务器：").append(serverName);
        if (StringUtils.isNotBlank(serverSummary)) {
            msgBuilder.append("，<br>服务器描述：").append(serverSummary);
        }
        msgBuilder.append("，<br>CPU使用率：").append(cpuAvgCombined).append("%，<br>时间：").append(DateTimeUtils.dateToString(new Date()));
        Alarm alarm = Alarm.builder()
                // 保证code的唯一性
                .code(Md5Utils.encrypt32(ip + serverName + ServerCpuMonitor.class.getName()))
                .title(title)
                .msg(msgBuilder.toString())
                .alarmLevel(alarmLevelEnum)
                .alarmReason(alarmReasonEnum)
                .monitorType(MonitorTypeEnums.SERVER)
                .monitorSubType(MonitorSubTypeEnums.SERVER__CPU)
                .alertedEntityId(String.valueOf(monitorServer.getId()))
                .build();
        AlarmPackage alarmPackage = this.serverPackageConstructor.structureAlarmPackage(alarm);
        this.alarmService.dealAlarmPackage(alarmPackage);
    }

}
