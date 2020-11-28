package com.imby.plug.scheduler;

import com.imby.common.util.CpuUtils;
import com.imby.plug.core.ConfigLoader;
import com.imby.plug.thread.ServerThread;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
     * 私有化构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/10/27 13:26
     */
    private ServerTaskScheduler() {
    }

    /**
     * <p>
     * 如果监控配置文件中配置了发送服务器信息，则延迟40秒启动定时任务，定时发送服务器信息包，
     * 定时任务的执行频率一般为监控配置文件中配置的服务器信息包发送频率，如果监控配置文件中没有配置服务器信息包的发送频率，
     * 则由类{@link ConfigLoader}提供默认的发送服务器信息频率。
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020年3月7日 下午4:43:35
     */
    public static void run() {
        // 是否发送服务器信息
        boolean serverInfoEnable = ConfigLoader.MONITORING_PROPERTIES.getServerInfoProperties().isEnable();
        if (serverInfoEnable) {
            final ScheduledExecutorService seService = new ScheduledThreadPoolExecutor(
                    // 线程数 = Ncpu /（1 - 阻塞系数），IO密集型阻塞系数相对较大
                    (int) (CpuUtils.getAvailableProcessors() / (1 - 0.8)),
                    new BasicThreadFactory.Builder()
                            // 设置线程名
                            .namingPattern("monitoring-server-pool-thread-%d")
                            // 设置为守护线程
                            .daemon(true)
                            .build());
            // 发送服务器信息的频率
            long rate = ConfigLoader.MONITORING_PROPERTIES.getServerInfoProperties().getRate();
            seService.scheduleAtFixedRate(new ServerThread(), 40, rate, TimeUnit.SECONDS);
        }
    }

}
