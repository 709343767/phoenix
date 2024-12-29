package com.gitee.pifeng.monitoring.plug.core;

import com.gitee.pifeng.monitoring.common.util.server.ProcessorsUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
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
    public static final ScheduledExecutorService HEARTBEAT_SCHEDULED_EXECUTOR_SERVICE = new ScheduledThreadPoolExecutor(
            // 线程数 = Ncpu /（1 - 阻塞系数），IO密集型阻塞系数相对较大
            (int) (ProcessorsUtils.getAvailableProcessors() / (1 - 0.8)),
            new BasicThreadFactory.Builder()
                    // 设置线程名
                    .namingPattern("phoenix-heartbeat-pool-thread-%d")
                    // 设置为守护线程
                    .daemon(true)
                    .build(),
            new ThreadPoolExecutor.AbortPolicy());

    /**
     * Java虚拟机信息任务调度 延迟/周期执行线程池
     */
    public static final ScheduledExecutorService JVM_SCHEDULED_EXECUTOR_SERVICE = new ScheduledThreadPoolExecutor(
            // 线程数 = Ncpu /（1 - 阻塞系数），IO密集型阻塞系数相对较大
            (int) (ProcessorsUtils.getAvailableProcessors() / (1 - 0.8)),
            new BasicThreadFactory.Builder()
                    // 设置线程名
                    .namingPattern("phoenix-jvm-pool-thread-%d")
                    // 设置为守护线程
                    .daemon(true)
                    .build(),
            new ThreadPoolExecutor.AbortPolicy());

    /**
     * 服务器信息任务调度 延迟/周期执行线程池
     */
    public static final ScheduledExecutorService SERVER_SCHEDULED_EXECUTOR_SERVICE = new ScheduledThreadPoolExecutor(
            // 线程数 = Ncpu /（1 - 阻塞系数），IO密集型阻塞系数相对较大
            (int) (ProcessorsUtils.getAvailableProcessors() / (1 - 0.8)),
            new BasicThreadFactory.Builder()
                    // 设置线程名
                    .namingPattern("phoenix-server-pool-thread-%d")
                    // 设置为守护线程
                    .daemon(true)
                    .build(),
            new ThreadPoolExecutor.AbortPolicy());

}
