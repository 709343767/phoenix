package com.imby.common.threadpool;

import com.imby.common.util.CpuUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.*;

/**
 * <p>
 * 公共的线程池。
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/27 10:20
 */
public class ThreadPool {

    /**
     * <p>
     * 私有化构造方法。
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/9/27 10:25
     */
    private ThreadPool() {
    }

    /**
     * 创建一个CPU密集型的线程池。
     * corePoolSize：指定了线程池中的线程数量，它的数量决定了添加的任务是开辟新的线程去执行，还是放到workQueue任务队列中去。<br>
     * maxPoolSize：指定了线程池中的最大线程数量，这个参数会根据你使用的workQueue任务队列的类型，决定线程池会开辟的最大线程数量。<br>
     * keepAliveTime：线程空闲时间。当线程空闲时间达到keepAliveTime时，线程会退出（关闭），直到线程数等于核心线程数。<br>
     * workQueue：阻塞队列。建议使用有界队列，比如ArrayBlockingQueue。<br>
     * ThreadFactory：线程创建工厂。一般用来设置线程名称的。<br>
     * handler：拒绝策略。一般用来做日志记录等。<br>
     */
    public static final ThreadPoolExecutor COMMON_CPU_INTENSIVE_THREAD_POOL = new ThreadPoolExecutor(
            // 线程数 = Ncpu /（1 - 阻塞系数），CPU密集型阻塞系数相对较小
            (int) (CpuUtils.getAvailableProcessors() / (1 - 0.2)),
            // 线程数 = Ncpu /（1 - 阻塞系数），CPU密集型阻塞系数相对较小
            (int) (CpuUtils.getAvailableProcessors() / (1 - 0.2)),
            1L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1024),
            new BasicThreadFactory.Builder()
                    // 设置线程名
                    .namingPattern("monitoring-common-cpu-intensive-thread-%d")
                    // 设置为守护线程
                    .daemon(true)
                    .build(),
            new ThreadPoolExecutor.AbortPolicy());

    /**
     * 创建一个IO密集型的线程池。
     * corePoolSize：指定了线程池中的线程数量，它的数量决定了添加的任务是开辟新的线程去执行，还是放到workQueue任务队列中去。<br>
     * maxPoolSize：指定了线程池中的最大线程数量，这个参数会根据你使用的workQueue任务队列的类型，决定线程池会开辟的最大线程数量。<br>
     * keepAliveTime：线程空闲时间。当线程空闲时间达到keepAliveTime时，线程会退出（关闭），直到线程数等于核心线程数。<br>
     * workQueue：阻塞队列。建议使用有界队列，比如ArrayBlockingQueue。<br>
     * ThreadFactory：线程创建工厂。一般用来设置线程名称的。<br>
     * handler：拒绝策略。一般用来做日志记录等。<br>
     */
    public static final ThreadPoolExecutor COMMON_IO_INTENSIVE_THREAD_POOL = new ThreadPoolExecutor(
            // 线程数 = Ncpu /（1 - 阻塞系数），IO密集型阻塞系数相对较大
            (int) (CpuUtils.getAvailableProcessors() / (1 - 0.8)),
            // 线程数 = Ncpu /（1 - 阻塞系数），IO密集型阻塞系数相对较大
            (int) (CpuUtils.getAvailableProcessors() / (1 - 0.8)),
            1L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1024),
            new BasicThreadFactory.Builder()
                    // 设置线程名
                    .namingPattern("monitoring-common-io-intensive-thread-%d")
                    // 设置为守护线程
                    .daemon(true)
                    .build(),
            new ThreadPoolExecutor.AbortPolicy());

    /**
     * 延迟/周期执行线程池（CPU密集型）
     */
    public static final ScheduledExecutorService COMMON_CPU_INTENSIVE_SCHEDULED_THREAD_POOL = new ScheduledThreadPoolExecutor(
            // 线程数 = Ncpu /（1 - 阻塞系数），CPU密集型阻塞系数相对较小
            (int) (CpuUtils.getAvailableProcessors() / (1 - 0.2)),
            new BasicThreadFactory.Builder()
                    // 设置线程名
                    .namingPattern("monitoring-common-cpu-intensive-scheduled-%d")
                    // 设置为守护线程
                    .daemon(true)
                    .build());

    /**
     * 延迟/周期执行线程池（IO密集型）
     */
    public static final ScheduledExecutorService COMMON_IO_INTENSIVE_SCHEDULED_THREAD_POOL = new ScheduledThreadPoolExecutor(
            // 线程数 = Ncpu /（1 - 阻塞系数），IO密集型阻塞系数相对较大
            (int) (CpuUtils.getAvailableProcessors() / (1 - 0.8)),
            new BasicThreadFactory.Builder()
                    // 设置线程名
                    .namingPattern("monitoring-common-io-intensive-scheduled-%d")
                    // 设置为守护线程
                    .daemon(true)
                    .build());

}
