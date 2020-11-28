package com.imby.server.business.server.monitor;

import com.imby.common.constant.AlarmLevelEnums;
import com.imby.common.constant.DateTimeStylesEnums;
import com.imby.common.constant.MonitorTypeEnums;
import com.imby.common.domain.Alarm;
import com.imby.common.dto.AlarmPackage;
import com.imby.common.exception.NetException;
import com.imby.server.business.server.core.MonitoringConfigPropertiesLoader;
import com.imby.server.business.server.core.PackageConstructor;
import com.imby.server.business.server.domain.Cpu;
import com.imby.server.business.server.pool.CpuPool;
import com.imby.server.business.server.service.IAlarmService;
import com.imby.server.inf.IServerMonitoringListener;
import com.imby.server.util.AlarmUtils;
import lombok.extern.slf4j.Slf4j;
import org.hyperic.sigar.SigarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <p>
 * 服务器CPU信息监控，实时更新状态，发送告警
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/27 16:07
 */
@Component
@Slf4j
public class CpuMonitor implements IServerMonitoringListener {

    /**
     * 服务器CPU信息池
     */
    @Autowired
    private CpuPool cpuPool;

    /**
     * 告警服务接口
     */
    @Autowired
    private IAlarmService alarmService;

    /**
     * <p>
     * 监控CPU使用率，发送CPU过载告警信息
     * </p>
     *
     * @param obj 回调参数
     * @throws NetException   获取网络信息异常
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020/3/30 21:56
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
        Cpu cpu = this.cpuPool.get(key);
        // 过载阈值
        double overloadThreshold = MonitoringConfigPropertiesLoader.getMonitoringProperties().getServerProperties().getServerCpuProperties().getOverloadThreshold();
        // 平均CPU使用率
        double avgCpuCombined = cpu.getAvgCpuCombined();
        // 平均CPU使用率大于等于配置的过载阈值
        if (avgCpuCombined >= overloadThreshold) {
            boolean isOverLoad = cpu.isOverLoad();
            // 没有标记平均CPU使用率大于配置的过载阈值
            if (!isOverLoad) {
                cpu.setNum(cpu.getNum() + 1);
                // 最终确认CPU过载的阈值（次数）
                int threshold = MonitoringConfigPropertiesLoader.getMonitoringProperties().getThreshold();
                if (cpu.getNum() >= threshold) {
                    // 处理CPU过载
                    this.dealCpuOverLoad(cpu);
                }
            }
        } else {
            // 处理从过载恢复CPU正常
            this.dealCpuNotOverLoad(cpu);
        }
        log.info("CPU信息池大小：{}，CPU过载：{}，详细信息：{}",
                this.cpuPool.size(),
                this.cpuPool.entrySet().stream().filter(e -> e.getValue().isOverLoad()).count(),
                this.cpuPool.toJsonString());
    }

    /**
     * <p>
     * 处理从过载恢复CPU正常
     * </p>
     *
     * @param cpu 服务器CPU
     * @author 皮锋
     * @custom.date 2020/3/30 10:48
     */
    private void dealCpuNotOverLoad(Cpu cpu) {
        cpu.setAlarm(false);
        cpu.setOverLoad(false);
        cpu.setNum(0);
    }

    /**
     * <p>
     * 处理CPU过载
     * </p>
     *
     * @param cpu 服务器CPU
     * @throws NetException   获取网络信息异常
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020/3/30 10:38
     */
    private void dealCpuOverLoad(Cpu cpu) throws NetException, SigarException {
        cpu.setOverLoad(true);
        // 是否已经发送过CPU过载告警消息
        boolean isAlarm = cpu.isAlarm();
        if (!isAlarm) {
            // 监控级别
            String level = MonitoringConfigPropertiesLoader.getMonitoringProperties().getServerProperties().getServerCpuProperties().getLevel();
            // 告警级别字符串转告警级别枚举
            AlarmLevelEnums alarmLevel = AlarmUtils.str2Enum(level);
            this.sendAlarmInfo("CPU过载", alarmLevel, cpu);
            cpu.setAlarm(true);
        }
    }

    /**
     * <p>
     * 发送告警信息
     * </p>
     *
     * @param title           告警标题
     * @param alarmLevelEnums 告警级别
     * @param cpu             CPU信息
     * @throws NetException   获取网络信息异常
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020/3/30 10:40
     */
    private void sendAlarmInfo(String title, AlarmLevelEnums alarmLevelEnums, Cpu cpu) throws NetException, SigarException {
        String dateTime = DateTimeFormatter.ofPattern(DateTimeStylesEnums.YYYY_MM_DD_HH_MM_SS.getValue()).format(LocalDateTime.now());
        String msg = "IP地址：" + cpu.getIp()
                + "，<br>服务器：" + cpu.getComputerName()
                + "，<br>CPU使用率：" + cpu.getAvgCpuCombined()
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
