package com.transfar.plug.scheduler;

import com.transfar.plug.core.ConfigLoader;
import com.transfar.plug.thread.ServerThread;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * 发送服务器信息任务调度器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月7日 下午4:42:19
 */
public class ServerTaskScheduler {

    /**
     * <p>
     * 如果监控配置文件中配置了发送服务器信息，则延迟30秒启动定时任务，定时发送服务器信息包，
     * 定时任务的执行频率一般为监控配置文件中配置的服务器信息包发送频率，如果监控配置文件中没有配置服务器信息包的发送频率，
     * 则由类{@link ConfigLoader}提供默认的发送服务器信息频率。
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020年3月7日 下午4:43:35
     */
    public static void run() {
        // 是否发送服务器信息
        boolean serverInfoEnable = ConfigLoader.monitoringProperties.getServerInfoProperties().isEnable();
        if (serverInfoEnable) {
            // 重新开启线程，让他单独去做我们想要做的操作，抛出异常并不会影响到主线程
            // Thread thread = new Thread(() -> {
            // AtomicInteger atomic = new AtomicInteger();
            final ScheduledExecutorService seService = Executors.newScheduledThreadPool(5, new ThreadFactory() {
                AtomicInteger atomic = new AtomicInteger();

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "monitoring-server-pool-thread-" + this.atomic.getAndIncrement());
                }
            });
            // 发送服务器信息的频率
            long rate = ConfigLoader.monitoringProperties.getServerInfoProperties().getRate();
            seService.scheduleAtFixedRate(new ServerThread(), 30, rate, TimeUnit.SECONDS);
            // });
            // 设置线程名
            // thread.setName("monitoring-server-thread");
            // 开始执行分进程
            // thread.start();
        }
    }
}
