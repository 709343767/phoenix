package com.imby.plug.scheduler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.imby.plug.core.ConfigLoader;
import com.imby.plug.thread.HeartbeatThread;

/**
 * <p>
 * 心跳任务调度器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月5日 下午2:54:21
 */
public class HeartbeatTaskScheduler {

    /**
     * <p>
     * 延时15秒后定时发送心跳包，发送心跳包的频率一般为监控配置文件中配置的心跳频率，如果监控配置文件中没配置心跳频率，
     * 则由类{@link ConfigLoader}提供默认心跳频率。
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020年3月5日 下午2:56:47
     */
    public static void run() {
        // 重新开启线程，让他单独去做我们想要做的操作，抛出异常并不会影响到主线程
        // Thread thread = new Thread(() -> {
        // AtomicInteger atomic = new AtomicInteger();
        final ScheduledExecutorService seService = Executors.newScheduledThreadPool(5, new ThreadFactory() {
            AtomicInteger atomic = new AtomicInteger();

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "monitoring-heartbeat-pool-thread-" + this.atomic.getAndIncrement());
            }
        });
        // 心跳频率
        long rate = ConfigLoader.monitoringProperties.getHeartbeatProperties().getRate();
        seService.scheduleAtFixedRate(new HeartbeatThread(), 15, rate, TimeUnit.SECONDS);
        // });
        // 设置线程名
        // thread.setName("monitoring-heartbeat-thread");
        // 开始执行分进程
        // thread.start();
    }

}
