package com.gitee.pifeng.monitoring.plug.core;

import com.gitee.pifeng.monitoring.common.threadpool.MonitoredScheduledThreadPoolExecutor;
import com.gitee.pifeng.monitoring.common.util.server.ProcessorsUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * <p>
 * 线程池获取器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2024/12/6 12:46
 */
public class ThreadPoolAcquirer {

    /**
     * 心跳任务调度 延迟/周期执行线程池
     */
    private static volatile MonitoredScheduledThreadPoolExecutor heartbeatScheduledThreadPoolExecutor;

    /**
     * Java虚拟机信息任务调度 延迟/周期执行线程池
     */
    private static volatile MonitoredScheduledThreadPoolExecutor jvmScheduledThreadPoolExecutor;

    /**
     * 服务器信息任务调度 延迟/周期执行线程池
     */
    private static volatile MonitoredScheduledThreadPoolExecutor serverScheduledThreadPoolExecutor;

    /**
     * <p>
     * 私有化构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2025/2/9 22:36
     */
    private ThreadPoolAcquirer() {
    }

    /**
     * <p>
     * 获取 心跳任务调度 延迟/周期执行线程池
     * </p>
     *
     * @return {@link MonitoredScheduledThreadPoolExecutor} 心跳任务调度 延迟/周期执行线程池
     * @custom.date 2025/2/9 22:37
     */
    public static MonitoredScheduledThreadPoolExecutor getHeartbeatScheduledThreadPoolExecutor() {
        if (heartbeatScheduledThreadPoolExecutor == null) {
            synchronized (ThreadPoolAcquirer.class) {
                if (heartbeatScheduledThreadPoolExecutor == null) {
                    heartbeatScheduledThreadPoolExecutor = new MonitoredScheduledThreadPoolExecutor(
                            // 线程数 = Ncpu /（1 - 阻塞系数），IO密集型阻塞系数相对较大
                            (int) (ProcessorsUtils.getAvailableProcessors() / (1 - 0.8)),
                            new BasicThreadFactory.Builder()
                                    // 设置线程名
                                    .namingPattern("phoenix-heartbeat-scheduled-pool-thread-%d")
                                    // 设置为守护线程
                                    .daemon(true)
                                    .build(),
                            new ThreadPoolExecutor.AbortPolicy(), "phoenix-heartbeat-scheduled-pool", true);
                }
            }
        }
        return heartbeatScheduledThreadPoolExecutor;
    }

    /**
     * <p>
     * 获取 Java虚拟机信息任务调度 延迟/周期执行线程池
     * </p>
     *
     * @return {@link MonitoredScheduledThreadPoolExecutor} Java虚拟机信息任务调度 延迟/周期执行线程池
     * @custom.date 2025/2/9 22:37
     */
    public static MonitoredScheduledThreadPoolExecutor getJvmScheduledThreadPoolExecutor() {
        if (jvmScheduledThreadPoolExecutor == null) {
            synchronized (ThreadPoolAcquirer.class) {
                if (jvmScheduledThreadPoolExecutor == null) {
                    jvmScheduledThreadPoolExecutor = new MonitoredScheduledThreadPoolExecutor(
                            // 线程数 = Ncpu /（1 - 阻塞系数），IO密集型阻塞系数相对较大
                            (int) (ProcessorsUtils.getAvailableProcessors() / (1 - 0.8)),
                            new BasicThreadFactory.Builder()
                                    // 设置线程名
                                    .namingPattern("phoenix-jvm-scheduled-pool-thread-%d")
                                    // 设置为守护线程
                                    .daemon(true)
                                    .build(),
                            new ThreadPoolExecutor.AbortPolicy(), "phoenix-jvm-scheduled-pool", true);
                }
            }
        }
        return jvmScheduledThreadPoolExecutor;
    }

    /**
     * <p>
     * 获取 服务器信息任务调度 延迟/周期执行线程池
     * </p>
     *
     * @return {@link MonitoredScheduledThreadPoolExecutor} 服务器信息任务调度 延迟/周期执行线程池
     * @custom.date 2025/2/9 22:37
     */
    public static MonitoredScheduledThreadPoolExecutor getServerScheduledThreadPoolExecutor() {
        if (serverScheduledThreadPoolExecutor == null) {
            synchronized (ThreadPoolAcquirer.class) {
                if (serverScheduledThreadPoolExecutor == null) {
                    serverScheduledThreadPoolExecutor = new MonitoredScheduledThreadPoolExecutor(
                            // 线程数 = Ncpu /（1 - 阻塞系数），IO密集型阻塞系数相对较大
                            (int) (ProcessorsUtils.getAvailableProcessors() / (1 - 0.8)),
                            new BasicThreadFactory.Builder()
                                    // 设置线程名
                                    .namingPattern("phoenix-server-scheduled-pool-thread-%d")
                                    // 设置为守护线程
                                    .daemon(true)
                                    .build(),
                            new ThreadPoolExecutor.AbortPolicy(), "phoenix-server-scheduled-pool", true);
                }
            }
        }
        return serverScheduledThreadPoolExecutor;
    }

}
