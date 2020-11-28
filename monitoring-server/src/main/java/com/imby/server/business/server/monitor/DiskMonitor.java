package com.imby.server.business.server.monitor;

import com.imby.common.constant.AlarmLevelEnums;
import com.imby.common.constant.DateTimeStylesEnums;
import com.imby.common.constant.MonitorTypeEnums;
import com.imby.common.domain.Alarm;
import com.imby.common.dto.AlarmPackage;
import com.imby.common.exception.NetException;
import com.imby.server.business.server.core.MonitoringConfigPropertiesLoader;
import com.imby.server.business.server.core.PackageConstructor;
import com.imby.server.business.server.domain.Disk;
import com.imby.server.business.server.pool.DiskPool;
import com.imby.server.business.server.service.IAlarmService;
import com.imby.server.inf.IServerMonitoringListener;
import com.imby.server.util.AlarmUtils;
import lombok.extern.slf4j.Slf4j;
import org.hyperic.sigar.SigarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * <p>
 * 服务器磁盘监控器，更新磁盘状态，发送告警
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/30 15:06
 */
@Component
@Slf4j
public class DiskMonitor implements IServerMonitoringListener {

    /**
     * 服务器磁盘信息池
     */
    @Autowired
    private DiskPool diskPool;

    /**
     * 告警服务接口
     */
    @Autowired
    private IAlarmService alarmService;

    /**
     * <p>
     * 监控磁盘使用率，在磁盘过载时发送告警信息
     * </p>
     *
     * @param obj 回调参数
     * @throws NetException   获取网络信息异常
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020/3/30 15:35
     */
    @Override
    public void wakeUpMonitor(Object... obj) throws NetException, SigarException {
        // 是否监控服务器
        boolean isEnable = MonitoringConfigPropertiesLoader.getMonitoringProperties().getServerProperties().isEnable();
        // 不需要监控服务器
        if (!isEnable) {
            return;
        }
        String key = String.valueOf(obj[0]);
        Disk disk = this.diskPool.get(key);
        Map<String, Disk.Subregion> subregionMap = disk.getSubregionMap();
        for (Map.Entry<String, Disk.Subregion> entry : subregionMap.entrySet()) {
            Disk.Subregion subregion = entry.getValue();
            // 分区的盘符资源利用率
            double usePercent = subregion.getUsePercent();
            //  过载阈值
            double overloadThreshold = MonitoringConfigPropertiesLoader.getMonitoringProperties().getServerProperties().getServerDiskProperties().getOverloadThreshold();
            // 利用率大于等于配置的过载阈值
            if (usePercent >= overloadThreshold) {
                // 是否已经发送过告警
                boolean isAlarm = subregion.isAlarm();
                subregion.setOverLoad(true);
                if (!isAlarm) {
                    // 监控级别
                    String level = MonitoringConfigPropertiesLoader.getMonitoringProperties().getServerProperties().getServerDiskProperties().getLevel();
                    // 告警级别字符串转告警级别枚举
                    AlarmLevelEnums alarmLevel = AlarmUtils.str2Enum(level);
                    // 发送告警
                    this.sendAlarmInfo("磁盘分区过载", alarmLevel, disk, subregion);
                    subregion.setAlarm(true);
                }
            }
            // 利用率正常
            else {
                subregion.setAlarm(false);
                subregion.setOverLoad(false);
            }
        }
        log.info("磁盘信息池大小：{}，详细信息：{}",
                this.diskPool.size(),
                this.diskPool.toJsonString());
    }

    /**
     * <p>
     * 发送告警信息
     * </p>
     *
     * @param title           告警标题
     * @param alarmLevelEnums 告警级别
     * @param disk            磁盘
     * @param subregion       磁盘分区
     * @throws NetException   获取网络信息异常
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020/3/25 14:46
     */
    private void sendAlarmInfo(String title, AlarmLevelEnums alarmLevelEnums, Disk disk, Disk.Subregion subregion) throws NetException, SigarException {
        String dateTime = DateTimeFormatter.ofPattern(DateTimeStylesEnums.YYYY_MM_DD_HH_MM_SS.getValue()).format(LocalDateTime.now());
        String msg = "IP地址：" + disk.getIp()
                + "，<br>服务器：" + disk.getComputerName()
                + "，<br>磁盘分区：" + subregion.getDevName()
                + "，<br>磁盘分区使用率：" + subregion.getUsePercent()
                + "%，<br>时间：" + dateTime;
        Alarm alarm = Alarm.builder()
                .title(title)
                .msg(msg)
                .alarmLevel(alarmLevelEnums)
                .monitorType(MonitorTypeEnums.SERVER)
                .build();
        AlarmPackage alarmPackage = new PackageConstructor().structureAlarmPackage(alarm);
        this.alarmService.dealAlarmPackage(alarmPackage);
    }

}
