package com.imby.server.business.server.monitor;

import com.imby.common.constant.AlarmLevelEnums;
import com.imby.common.constant.AlarmTypeEnums;
import com.imby.common.constant.DateTimeStylesEnums;
import com.imby.common.domain.Alarm;
import com.imby.common.dto.AlarmPackage;
import com.imby.common.exception.NetException;
import com.imby.server.business.server.core.PackageConstructor;
import com.imby.server.business.server.domain.Cpu;
import com.imby.server.business.server.pool.CpuPool;
import com.imby.server.business.server.service.IAlarmService;
import com.imby.server.inf.IServerMonitoringListener;
import com.imby.server.property.MonitoringServerWebProperties;
import lombok.extern.slf4j.Slf4j;
import org.hyperic.sigar.SigarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
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
     * 监控配置属性
     */
    @Autowired
    private MonitoringServerWebProperties monitoringServerWebProperties;

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
        String key = String.valueOf(obj[0]);
        Cpu cpu = this.cpuPool.get(key);
        // 最终确认CPU过载的阈值
        long threshold = this.monitoringServerWebProperties.getThreshold();
        // 平均CPU使用率
        double avgCpuCombined = cpu.getAvgCpuCombined();
        // 平均CPU使用率大于90%
        if (avgCpuCombined > 90) {
            boolean isOverLoad90 = cpu.isOverLoad90();
            // 没有标记平均CPU使用率大于90%
            if (!isOverLoad90) {
                int num90 = cpu.getNum90();
                cpu.setNum90(num90 + 1);
                // 大于两倍监控阈值才认为过载90%是确认的
                if (num90 > threshold * 2) {
                    // 处理CPU过载90%
                    this.dealCpuOverLoad90(cpu);
                }
            }
        } else {
            // 处理从过载90%恢复CPU正常
            this.dealCpuNotOverLoad90(cpu);
        }
        // 平均CPU使用率大于100%
        if (avgCpuCombined > 100) {
            boolean isOverLoad100 = cpu.isOverLoad100();
            // 没有标记平均CPU使用率大于90%
            if (!isOverLoad100) {
                int num100 = cpu.getNum100();
                cpu.setNum100(num100 + 1);
                if (num100 > threshold) {
                    // 处理CPU过载100%
                    this.dealCpuOverLoad100(cpu);
                }
            }
        } else {
            // 处理从过载100%恢复CPU正常
            this.dealCpuNotOverLoad100(cpu);
        }
        log.info("CPU信息池大小：{}，CPU过载90%：{}，CPU过载100%：{}，详细信息：{}",
                this.cpuPool.size(),
                this.cpuPool.entrySet().stream().filter(e -> e.getValue().isOverLoad90()).count(),
                this.cpuPool.entrySet().stream().filter(e -> e.getValue().isOverLoad100()).count(),
                this.cpuPool.toJsonString());
    }

    /**
     * <p>
     * 处理从过载100%恢复CPU正常
     * </p>
     *
     * @param cpu 服务器CPU
     * @author 皮锋
     * @custom.date 2020/3/30 10:48
     */
    private void dealCpuNotOverLoad100(Cpu cpu) {
        cpu.setAlarm100(false);
        cpu.setOverLoad100(false);
        cpu.setNum100(0);
    }

    /**
     * <p>
     * 处理从过载90%恢复CPU正常
     * </p>
     *
     * @param cpu 服务器CPU
     * @author 皮锋
     * @custom.date 2020/3/30 10:48
     */
    private void dealCpuNotOverLoad90(Cpu cpu) {
        cpu.setAlarm90(false);
        cpu.setOverLoad90(false);
        cpu.setNum90(0);
    }

    /**
     * <p>
     * 处理CPU过载100%
     * </p>
     *
     * @param cpu 服务器CPU
     * @throws NetException   获取网络信息异常
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020/3/30 10:38
     */
    private void dealCpuOverLoad100(Cpu cpu) throws NetException, SigarException {
        cpu.setOverLoad100(true);
        // 是否已经发送过CPU过载100%告警消息
        boolean isAlarm = cpu.isAlarm100();
        if (!isAlarm) {
            this.sendAlarmInfo("CPU过载100%", AlarmLevelEnums.FATAL, cpu);
            cpu.setAlarm100(true);
        }
    }

    /**
     * <p>
     * 处理CPU过载90%
     * </p>
     *
     * @param cpu 服务器CPU
     * @throws NetException   获取网络信息异常
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020/3/30 10:38
     */
    private void dealCpuOverLoad90(Cpu cpu) throws NetException, SigarException {
        cpu.setOverLoad90(true);
        // 是否已经发送过CPU过载90%告警消息
        boolean isAlarm = cpu.isAlarm90();
        if (!isAlarm) {
            this.sendAlarmInfo("CPU过载90%", AlarmLevelEnums.WARN, cpu);
            cpu.setAlarm90(true);
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
    @Async
    public void sendAlarmInfo(String title, AlarmLevelEnums alarmLevelEnums, Cpu cpu) throws NetException, SigarException {
        String dateTime = DateTimeFormatter.ofPattern(DateTimeStylesEnums.YYYY_MM_DD_HH_MM_SS.getValue()).format(LocalDateTime.now());
        String msg = "IP地址：" + cpu.getIp()
                + "，<br>服务器：" + cpu.getComputerName()
                + "，<br>CPU使用率：" + cpu.getAvgCpuCombined()
                + "%，<br>时间：" + dateTime;
        Alarm alarm = Alarm.builder()
                .title(title)
                .msg(msg)
                .alarmLevel(alarmLevelEnums)
                .alarmType(AlarmTypeEnums.SERVER)
                .build();
        AlarmPackage alarmPackage = new PackageConstructor().structureAlarmPackage(alarm);
        this.alarmService.dealAlarmPackage(alarmPackage);
    }

}
