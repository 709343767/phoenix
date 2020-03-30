package com.transfar.server.business.server.core;

import com.transfar.common.constant.AlarmLevelEnums;
import com.transfar.common.domain.Alarm;
import com.transfar.common.dto.AlarmPackage;
import com.transfar.server.business.server.domain.Cpu;
import com.transfar.server.business.server.service.IAlarmService;
import com.transfar.server.property.MonitoringServerWebProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * 在容器启动后，定时扫描所有服务器CPU信息，实时更新状态，发送告警
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/27 16:07
 */
@Component
@Slf4j
@Order(4)
public class CpuMonitorTask implements CommandLineRunner, DisposableBean {

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
     * 延迟/周期执行线程池
     */
    private final ScheduledExecutorService seService = Executors.newScheduledThreadPool(5, new ThreadFactory() {
        AtomicInteger atomic = new AtomicInteger();

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "monitoring-cpu-pool-thread-" + this.atomic.getAndIncrement());
        }
    });

    @Override
    public void run(String... args) {
        // 重新开启线程，让他单独去做我们想要做的操作，此时CommandLineRunner执行的操作和主线程是相互独立的，抛出异常并不会影响到主线程
        Thread thread = new Thread(() -> this.seService.scheduleAtFixedRate(() -> {
            // 循环所有服务器CPU信息
            for (Map.Entry<String, Cpu> entry : this.cpuPool.entrySet()) {
                String key = entry.getKey();
                Cpu cpu = entry.getValue();
                // 最终确认CPU过载的阈值
                long threshold = (int) (cpu.getRate() / 30) * this.monitoringServerWebProperties.getThreshold();
                // 平均CPU使用率
                double avgCpuCombined = cpu.getAvgCpuCombined();
                // 平均CPU使用率大于90%
                if (avgCpuCombined > 90) {
                    int num90 = cpu.getNum90();
                    cpu.setNum90(num90 + 1);
                    if (num90 > threshold) {
                        // 处理CPU过载90%
                        this.dealCpuOverLoad90(key, cpu);
                    }
                } else {
                    //处理从过载90%恢复CPU正常
                    this.dealCpuNotOverLoad90(key, cpu);
                }
                // 平均CPU使用率大于100%
                if (avgCpuCombined > 100) {
                    int num100 = cpu.getNum100();
                    cpu.setNum100(num100 + 1);
                    if (num100 > threshold) {
                        // 处理CPU过载100%
                        this.dealCpuOverLoad100(key, cpu);
                    }
                } else {
                    // 处理从过载100%恢复CPU正常
                    this.dealCpuNotOverLoad100(key, cpu);
                }
            }
            log.info("当前服务器CPU信息池大小：{}，CPU过载90%：{}，CPU过载100%：{}，详细信息：{}",//
                    this.cpuPool.size(),//
                    this.cpuPool.entrySet().stream().filter((e) -> e.getValue().isOverLoad90()).count(),//
                    this.cpuPool.entrySet().stream().filter((e) -> e.getValue().isOverLoad100()).count(),//
                    this.cpuPool.toJsonString()
            );
        }, 20, 30, TimeUnit.SECONDS));
        // 设置守护线程
        thread.setDaemon(true);
        // 开始执行分进程
        thread.start();
    }

    /**
     * <p>
     * 处理从过载100%恢复CPU正常
     * </p>
     *
     * @param key 服务器CPU键
     * @param cpu 服务器CPU
     * @author 皮锋
     * @custom.date 2020/3/30 10:48
     */
    private void dealCpuNotOverLoad100(String key, Cpu cpu) {
        cpu.setAlarm100(false);
        cpu.setOverLoad100(false);
        cpu.setNum100(0);
        this.cpuPool.replace(key, cpu);
    }

    /**
     * <p>
     * 处理从过载90%恢复CPU正常
     * </p>
     *
     * @param key 服务器CPU键
     * @param cpu 服务器CPU
     * @author 皮锋
     * @custom.date 2020/3/30 10:48
     */
    private void dealCpuNotOverLoad90(String key, Cpu cpu) {
        cpu.setAlarm90(false);
        cpu.setOverLoad90(false);
        cpu.setNum90(0);
        this.cpuPool.replace(key, cpu);
    }

    /**
     * <p>
     * 处理CPU过载100%
     * </p>
     *
     * @param key 服务器CPU键
     * @param cpu 服务器CPU
     * @author 皮锋
     * @custom.date 2020/3/30 10:38
     */
    private void dealCpuOverLoad100(String key, Cpu cpu) {
        cpu.setOverLoad100(true);
        // 是否已经发送过CPU过载100%告警消息
        boolean isAlarm = cpu.isAlarm100();
        if (!isAlarm) {
            this.sendAlarmInfo("CPU过载100%", AlarmLevelEnums.FATAL, cpu);
            cpu.setAlarm100(true);
        }
        this.cpuPool.replace(key, cpu);
    }

    /**
     * <p>
     * 处理CPU过载90%
     * </p>
     *
     * @param key 服务器CPU键
     * @param cpu 服务器CPU
     * @author 皮锋
     * @custom.date 2020/3/30 10:38
     */
    private void dealCpuOverLoad90(String key, Cpu cpu) {
        cpu.setOverLoad90(true);
        // 是否已经发送过CPU过载90%告警消息
        boolean isAlarm = cpu.isAlarm90();
        if (!isAlarm) {
            this.sendAlarmInfo("CPU过载90%", AlarmLevelEnums.WARN, cpu);
            cpu.setAlarm90(true);
        }
        this.cpuPool.replace(key, cpu);
    }

    /**
     * <p>
     * 发送告警信息
     * </p>
     *
     * @param title           告警标题
     * @param alarmLevelEnums 告警级别
     * @param cpu             CPU信息
     * @author 皮锋
     * @custom.date 2020/3/30 10:40
     */
    private synchronized void sendAlarmInfo(String title, AlarmLevelEnums alarmLevelEnums, Cpu cpu) {
        new Thread(() -> {
            String msg = "IP地址：" + cpu.getIp() + "，CPU使用率：" + cpu.getAvgCpuCombined() + "%";
            Alarm alarm = Alarm.builder()//
                    .title(title)//
                    .msg(msg)//
                    .alarmLevel(alarmLevelEnums)//
                    .build();
            AlarmPackage alarmPackage = new PackageConstructor().structureAlarmPackage(alarm);
            alarmService.dealAlarmPackage(alarmPackage);
        }).start();
    }

    /**
     * <p>
     * 在spring容器销毁时关闭线程池
     * </p>
     * 关闭线程池：monitoring-cpu-pool-thread
     *
     * @author 皮锋
     * @custom.date 2020/3/26 16:33
     */
    @Override
    public void destroy() {
        if (!this.seService.isShutdown()) {
            this.seService.shutdown();
            log.info("延迟/周期执行线程池“monitoring-cpu-pool-thread”已经关闭！");
        }
    }
}
