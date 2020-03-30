package com.transfar.server.business.server.core;

import com.transfar.common.constant.AlarmLevelEnums;
import com.transfar.common.domain.Alarm;
import com.transfar.common.dto.AlarmPackage;
import com.transfar.common.inf.ICallback;
import com.transfar.server.business.server.domain.Memory;
import com.transfar.server.business.server.service.IAlarmService;
import com.transfar.server.property.MonitoringServerWebProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

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
public class MemoryMonitor implements ICallback, DisposableBean {

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
     * 监控配置属性
     */
    @Autowired
    private MonitoringServerWebProperties monitoringServerWebProperties;

    /**
     * 线程池
     */
    private ExecutorService seService = Executors.newCachedThreadPool(new ThreadFactory() {
        AtomicInteger atomic = new AtomicInteger();

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "monitoring-memory-pool-thread-" + this.atomic.getAndIncrement());
        }
    });

    /**
     * <p>
     * 监控内存使用率，发送内存过载告警信息
     * </p>
     *
     * @param obj 回调参数
     * @author 皮锋
     * @custom.date 2020/3/30 22:01
     */
    @Override
    public void event(Object... obj) {
        this.seService.execute(() -> {
            String key = String.valueOf(obj[0]);
            Memory memory = this.memoryPool.get(key);
            String menUsedPercent = memory.getMemoryDomain().getMenUsedPercent();
            // 物理内存使用率
            double usedPercent = Double.parseDouble(menUsedPercent.substring(0, menUsedPercent.length() - 1));
            // 物理内存占用率超过90%
            if (usedPercent > 90) {
                int num = memory.getNum();
                memory.setNum(num + 1);
                // 最终确认内存过载的阈值
                int threshold = this.monitoringServerWebProperties.getThreshold();
                if (num > threshold) {
                    // 处理物理内存过载
                    this.dealMemoryOverLoad(key, memory);
                }
            } else {
                // 处理物理内存正常
                this.dealMemoryNotOverLoad(key, memory);
            }
            log.info("当前服务器内存信息池大小：{}，内存过载：{}，详细信息：{}",//
                    this.memoryPool.size(),//
                    this.memoryPool.entrySet().stream().filter((e) -> e.getValue().isOverLoad()).count(),//
                    this.memoryPool.toJsonString());
        });
    }

    /**
     * <p>
     * 处理物理内存正常
     * </p>
     *
     * @param key    服务器内存键
     * @param memory 服务器内存
     * @author 皮锋
     * @custom.date 2020/3/27 10:22
     */
    private void dealMemoryNotOverLoad(String key, Memory memory) {
        memory.setOverLoad(false);
        memory.setAlarm(false);
        // 过载次数恢复为0
        memory.setNum(0);
        this.memoryPool.replace(key, memory);
    }

    /**
     * <p>
     * 处理物理内存过载
     * </p>
     *
     * @param key    服务器内存键
     * @param memory 服务器内存
     * @author 皮锋
     * @custom.date 2020/3/27 10:20
     */
    private void dealMemoryOverLoad(String key, Memory memory) {
        memory.setOverLoad(true);
        // 是否已经发送过告警消息
        boolean isAlarm = memory.isAlarm();
        if (!isAlarm) {
            this.sendAlarmInfo("服务器内存过载", AlarmLevelEnums.WARN, memory);
            memory.setAlarm(true);
        }
        this.memoryPool.replace(key, memory);
    }

    /**
     * <p>
     * 发送告警信息
     * </p>
     *
     * @param title           告警标题
     * @param alarmLevelEnums 告警级别
     * @param memory          内存信息
     * @author 皮锋
     * @custom.date 2020/3/25 14:46
     */
    private synchronized void sendAlarmInfo(String title, AlarmLevelEnums alarmLevelEnums, Memory memory) {
        this.seService.execute(() -> {
            String msg = "IP地址：" + memory.getIp() + "，内存使用率：" + memory.getMemoryDomain().getMenUsedPercent();
            Alarm alarm = Alarm.builder()//
                    .title(title)//
                    .msg(msg)//
                    .alarmLevel(alarmLevelEnums)//
                    .build();
            AlarmPackage alarmPackage = new PackageConstructor().structureAlarmPackage(alarm);
            this.alarmService.dealAlarmPackage(alarmPackage);
        });
    }

    /**
     * <p>
     * 在spring容器销毁时关闭线程池
     * </p>
     * 关闭线程池：monitoring-memory-pool-thread
     *
     * @author 皮锋
     * @custom.date 2020/3/26 16:33
     */
    @Override
    public void destroy() {
        if (!this.seService.isShutdown()) {
            this.seService.shutdown();
            log.info("线程池“monitoring-memory-pool-thread”已经关闭！");
        }
    }
}
