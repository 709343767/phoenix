package com.gitee.pifeng.monitoring.server.business.server.monitor.server;

import cn.hutool.core.io.unit.DataSizeUtil;
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
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServerMemory;
import com.gitee.pifeng.monitoring.server.business.server.service.IAlarmService;
import com.gitee.pifeng.monitoring.server.business.server.service.IServerMemoryService;
import com.gitee.pifeng.monitoring.server.business.server.service.IServerService;
import com.gitee.pifeng.monitoring.server.inf.IServerMonitoringListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * <p>
 * 监控服务器内存信息，实时更新状态，发送告警
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/2/4 14:11
 */
@Component
@Slf4j
public class ServerMemoryMonitor implements IServerMonitoringListener {

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
     * 服务器内存信息服务层接口
     */
    @Autowired
    private IServerMemoryService serverMemoryService;

    /**
     * 服务器信息服务层接口
     */
    @Autowired
    private IServerService serverService;

    /**
     * <p>
     * 监控内存使用率，发送内存过载告警信息
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
        // 是否监控服务器内存
        boolean isMemoryEnable = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getServerProperties().getServerMemoryProperties().isEnable();
        if (!isMemoryEnable) {
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
        // 查询此IP的服务器内存信息
        MonitorServerMemory monitorServerMemory = this.serverMemoryService.getOne(new LambdaQueryWrapper<MonitorServerMemory>().eq(MonitorServerMemory::getIp, ip));
        if (monitorServerMemory == null) {
            return;
        }
        // 过载阈值
        double overloadThreshold = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getServerProperties().getServerMemoryProperties().getOverloadThreshold();
        // 物理内存总量
        String menTotal = DataSizeUtil.format(monitorServerMemory.getMenTotal());
        // 物理内存使用量
        String menUsed = DataSizeUtil.format(monitorServerMemory.getMenUsed());
        // 物理内存使用率
        double menUsedPercent = NumberUtil.round(monitorServerMemory.getMenUsedPercent() * 100D, 2).doubleValue();
        // 物理内存使用率大于等于配置的过载阈值
        if (menUsedPercent >= overloadThreshold) {
            // 处理物理内存过载
            this.dealMemoryOverLoad(monitorServer, menUsedPercent, menTotal, menUsed);
        } else {
            // 处理物理内存正常
            this.dealMemoryNotOverLoad(monitorServer, menUsedPercent, menTotal, menUsed);
        }
    }

    /**
     * <p>
     * 处理物理内存正常
     * </p>
     *
     * @param monitorServer  服务器信息
     * @param menUsedPercent 物理内存使用率
     * @param menTotal       物理内存总量
     * @param menUsed        物理内存使用量
     * @author 皮锋
     * @custom.date 2021/2/4 14:34
     */
    private void dealMemoryNotOverLoad(MonitorServer monitorServer, double menUsedPercent, String menTotal, String menUsed) {
        // 发送告警信息
        try {
            // 不用担心头次检测到内存正常（非异常转正常）会发送告警，最终是否发送告警由“实时监控服务”决定
            this.sendAlarmInfo("服务器内存恢复正常", monitorServer, menUsedPercent, menTotal, menUsed, AlarmLevelEnums.INFO, AlarmReasonEnums.ABNORMAL_2_NORMAL);
        } catch (Exception e) {
            log.error("服务器内存恢复正常告警异常！", e);
        }
    }

    /**
     * <p>
     * 处理物理内存过载
     * </p>
     *
     * @param monitorServer  服务器信息
     * @param menUsedPercent 物理内存使用率
     * @param menTotal       物理内存总量
     * @param menUsed        物理内存使用量
     * @author 皮锋
     * @custom.date 2021/2/4 14:26
     */
    private void dealMemoryOverLoad(MonitorServer monitorServer, double menUsedPercent, String menTotal, String menUsed) {
        // 告警级别
        AlarmLevelEnums alarmLevelEnum = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getServerProperties().getServerMemoryProperties().getLevelEnum();
        // 发送告警信息
        try {
            this.sendAlarmInfo("服务器内存过载", monitorServer, menUsedPercent, menTotal, menUsed, alarmLevelEnum, AlarmReasonEnums.NORMAL_2_ABNORMAL);
        } catch (Exception e) {
            log.error("服务器内存过载告警异常！", e);
        }
    }

    /**
     * <p>
     * 发送告警信息
     * </p>
     *
     * @param title           告警标题
     * @param monitorServer   服务器信息
     * @param menUsedPercent  物理内存使用率
     * @param alarmLevelEnum  告警级别
     * @param alarmReasonEnum 告警原因
     * @param menTotal        物理内存总量
     * @param menUsed         物理内存使用量
     * @throws NetException 获取网络信息异常
     * @author 皮锋
     * @custom.date 2020/3/25 14:46
     */
    private void sendAlarmInfo(String title, MonitorServer monitorServer, double menUsedPercent, String menTotal,
                               String menUsed, AlarmLevelEnums alarmLevelEnum, AlarmReasonEnums alarmReasonEnum)
            throws NetException {
        // 告警是否打开
        boolean alarmEnable = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getServerProperties().getServerMemoryProperties().isAlarmEnable();
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
        String monitorEnv = monitorServer.getMonitorEnv();
        String monitorGroup = monitorServer.getMonitorGroup();
        StringBuilder msgBuilder = new StringBuilder();
        msgBuilder.append("IP地址：").append(ip).append("，<br>服务器：").append(serverName);
        if (StringUtils.isNotBlank(serverSummary)) {
            msgBuilder.append("，<br>描述：").append(serverSummary);
        }
        if (StringUtils.isNotBlank(monitorEnv)) {
            msgBuilder.append("，<br>环境：").append(monitorEnv);
        }
        if (StringUtils.isNotBlank(monitorGroup)) {
            msgBuilder.append("，<br>分组：").append(monitorGroup);
        }
        msgBuilder.append("，<br>内存使用量：").append(menUsed).append(" / ").append(menTotal);
        msgBuilder.append("，<br>内存使用率：").append(menUsedPercent).append("%");
        msgBuilder.append("，<br>时间：").append(DateTimeUtils.dateToString(new Date()));
        Alarm alarm = Alarm.builder()
                // 保证code的唯一性
                .code(Md5Utils.encrypt32(ip + serverName + ServerMemoryMonitor.class.getName()))
                .title(title)
                .msg(msgBuilder.toString())
                .alarmLevel(alarmLevelEnum)
                .alarmReason(alarmReasonEnum)
                .monitorType(MonitorTypeEnums.SERVER)
                .monitorSubType(MonitorSubTypeEnums.SERVER__MEMORY)
                .alertedEntityId(String.valueOf(monitorServer.getId()))
                .build();
        AlarmPackage alarmPackage = this.serverPackageConstructor.structureAlarmPackage(alarm);
        this.alarmService.dealAlarmPackage(alarmPackage);
    }
}
