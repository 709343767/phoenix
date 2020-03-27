package com.transfar.server.business.server.core;

import com.transfar.server.business.server.domain.Cpu;
import com.transfar.server.business.server.service.IAlarmService;
import com.transfar.server.property.MonitoringServerWebProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
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
public class CpuCommandLineRunner implements CommandLineRunner, DisposableBean {

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
