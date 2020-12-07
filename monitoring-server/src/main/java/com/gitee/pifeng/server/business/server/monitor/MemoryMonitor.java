package com.gitee.pifeng.server.business.server.monitor;

import com.gitee.pifeng.common.constant.AlarmLevelEnums;
import com.gitee.pifeng.common.constant.DateTimeStylesEnums;
import com.gitee.pifeng.common.constant.MonitorTypeEnums;
import com.gitee.pifeng.common.domain.Alarm;
import com.gitee.pifeng.common.dto.AlarmPackage;
import com.gitee.pifeng.common.exception.NetException;
import com.gitee.pifeng.server.business.server.core.MonitoringConfigPropertiesLoader;
import com.gitee.pifeng.server.business.server.core.PackageConstructor;
import com.gitee.pifeng.server.business.server.domain.Memory;
import com.gitee.pifeng.server.business.server.pool.MemoryPool;
import com.gitee.pifeng.server.business.server.service.IAlarmService;
import com.gitee.pifeng.server.inf.IServerMonitoringListener;
import com.gitee.pifeng.server.util.AlarmUtils;
import lombok.extern.slf4j.Slf4j;
import org.hyperic.sigar.SigarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <p>
 * 在容器启动后，定时扫描所有服务器内存信息，实时更新状态，发送告警
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/26 16:28
 */
@Component
@Slf4j
public class MemoryMonitor implements IServerMonitoringListener {

    /**
     * 服务器内存信息池
     */
    @Autowired
    private MemoryPool memoryPool;

    /**
     * 告警服务接口
     */
    @Autowired
    private IAlarmService alarmService;

    /**
     * <p>
     * 监控内存使用率，发送内存过载告警信息
     * </p>
     *
     * @param obj 回调参数
     * @throws NetException   获取网络信息异常
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020/3/30 22:01
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
        Memory memory = this.memoryPool.get(key);
        // 物理内存使用率
        double usedPercent = memory.getUsedPercent();
        // 过载阈值
        double overloadThreshold = MonitoringConfigPropertiesLoader.getMonitoringProperties().getServerProperties().getServerMemoryProperties().getOverloadThreshold();
        // 物理内存占用率大于等于配置的过载阈值
        if (usedPercent >= overloadThreshold) {
            // 是否确认内存过载
            boolean isOverLoad = memory.isOverLoad();
            if (!isOverLoad) {
                memory.setNum(memory.getNum() + 1);
                // 最终确认内存过载的阈值（次数）
                int threshold = MonitoringConfigPropertiesLoader.getMonitoringProperties().getThreshold();
                if (memory.getNum() >= threshold) {
                    // 处理物理内存过载
                    this.dealMemoryOverLoad(memory);
                }
            }
        } else {
            // 处理物理内存正常
            this.dealMemoryNotOverLoad(memory);
        }
        log.info("内存信息池大小：{}，内存过载：{}，详细信息：{}",
                this.memoryPool.size(),
                this.memoryPool.entrySet().stream().filter(e -> e.getValue().isOverLoad()).count(),
                this.memoryPool.toJsonString());
    }

    /**
     * <p>
     * 处理物理内存正常
     * </p>
     *
     * @param memory 服务器内存
     * @author 皮锋
     * @custom.date 2020/3/27 10:22
     */
    private void dealMemoryNotOverLoad(Memory memory) {
        memory.setOverLoad(false);
        memory.setAlarm(false);
        // 过载次数恢复为0
        memory.setNum(0);
    }

    /**
     * <p>
     * 处理物理内存过载
     * </p>
     *
     * @param memory 服务器内存
     * @throws NetException   获取网络信息异常
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020/3/27 10:20
     */
    private void dealMemoryOverLoad(Memory memory) throws NetException, SigarException {
        memory.setOverLoad(true);
        // 是否已经发送过告警消息
        boolean isAlarm = memory.isAlarm();
        if (!isAlarm) {
            // 监控级别
            String level = MonitoringConfigPropertiesLoader.getMonitoringProperties().getServerProperties().getServerMemoryProperties().getLevel();
            // 告警级别字符串转告警级别枚举
            AlarmLevelEnums alarmLevel = AlarmUtils.str2Enum(level);
            this.sendAlarmInfo("服务器内存过载", alarmLevel, memory);
            memory.setAlarm(true);
        }
    }

    /**
     * <p>
     * 发送告警信息
     * </p>
     *
     * @param title           告警标题
     * @param alarmLevelEnums 告警级别
     * @param memory          内存信息
     * @throws NetException   获取网络信息异常
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020/3/25 14:46
     */
    private void sendAlarmInfo(String title, AlarmLevelEnums alarmLevelEnums, Memory memory) throws NetException, SigarException {
        String dateTime = DateTimeFormatter.ofPattern(DateTimeStylesEnums.YYYY_MM_DD_HH_MM_SS.getValue()).format(LocalDateTime.now());
        String msg = "IP地址：" + memory.getIp()
                + "，<br>服务器：" + memory.getComputerName()
                + "，<br>内存使用率：" + memory.getUsedPercent()
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
