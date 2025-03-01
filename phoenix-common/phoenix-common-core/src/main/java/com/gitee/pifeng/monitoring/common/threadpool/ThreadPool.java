package com.gitee.pifeng.monitoring.common.threadpool;

import com.gitee.pifeng.monitoring.common.util.server.ProcessorsUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 公共的线程池。
 * </p>
 * <dl>
 *      <dt>ThreadPoolExecutor构造方法：</dt>
 *      <dd>
 *          <ul>
 *              <li>参数：corePoolSize。即使线程处于空闲状态，也要保留在池中的线程数量，除非设置allowCoreThreadTimeOut参数为true，
 *                  则在空闲时间超过keepAliveTime时，会被终止掉。allowCoreThreadTimeOut默认为false。</li>
 *              <li>参数：maximumPoolSize。线程池中允许的最大线程数量。</li>
 *              <li>参数：keepAliveTime。保持活跃的时间，也就是当线程池中的线程数量超过corePoolSize时，
 *                  这些超量的线程在等待被新任务使用前的最大等待时间，超过找个时间就要被终止掉了。</li>
 *              <li>参数：unit。保持活跃时间的单位。可选为：NANOSECONDS，MICROSECONDS，MILLISECONDS，SECONDS，MINUTES，HOURS，DAYS等。</li>
 *              <li>参数：workQueue。工作队列。这队列用来保持那些execute()方法提交的还没有执行的任务。
 *                  常用的队列有SynchronousQueue,LinkedBlockingQueue,ArrayBlockingQueue。
 *                  一般我们需要根据自己的实际业务需求选择合适的工作队列。<br>
 *                  1.SynchronousQueue：直接传递。对于一个好地默认的工作队列选择是SynchronousQueue，该队列传递任务到线程而不持有它们。
 *                  在这一点上，试图向该队列压入一个任务，如果没有可用的线程立刻运行任务，那么就会入列失败，所以一个新的线程就会被创建。
 *                  当处理那些内部依赖的任务集合时，这个选择可以避免锁住。直接传递通常需要无边界的最大线程数来避免新提交任务被拒绝处理。
 *                  当任务以平均快于被处理的速度提交到线程池时，它依次地确认无边界线程增长的可能性；<br>
 *                  2.LinkedBlockingQueue：无界队列。没有预先定义容量的无界队列，在核心线程数都繁忙的时候会使新提交的任务在队列中等待被执行，
 *                  所以将不会创建更多的线程，因此，最大线程数的值将不起作用。当每个任务之间是相互独立的时比较适合该队列，所以任务之间不能互相影响执行。
 *                  例如，在一个WEB页面服务器，当平滑的出现短暂地请求爆发时这个类型的队列是非常有用的，
 *                  当任务以快于平均处理速度被提交时，该队列会确认无边界队列增长的可能性。<br>
 *                  3.ArrayBlockingQueue：有界阻塞队列，遵循FIFO原则，一旦创建容量不能改变，
 *                  当向一个已经满了的该队列中添加元素和向一个已经为空的该队列取出元素都会导致阻塞；
 *                  当线程池使用有限的最大线程数时该队列可以帮助保护资源枯竭，但它更难协调和控制。队列大小和最大线程数在性能上可以互相交换：
 *                  使用大队列和小线程池会降低CPU使用和OS资源与上下文切换开销，但会导致人为降低吞吐量，如果任务频繁阻塞，系统的线程调度时间会超过我们的允许值；
 *                  如果使用小队列大池，这将会使CPU较为繁忙但会出现难以接受的调度开销，这也会导致降低吞吐量。</li>
 *              <li>参数：threadFactory。线程工厂。当线程池需要创建线程的时候用来创建线程。默认是Executors类的静态内部类DefaultThreadFactory。</li>
 *          </ul>
 *      </dd>
 * </dl>
 *
 * @author 皮锋
 * @custom.date 2020/9/27 10:20
 */
public class ThreadPool {

    /**
     * CPU密集型的线程池
     */
    private static volatile MonitoredThreadPoolExecutor commonCpuIntensiveThreadPoolExecutor;

    /**
     * IO密集型的线程池
     */
    private static volatile MonitoredThreadPoolExecutor commonIoIntensiveThreadPoolExecutor;

    /**
     * 延迟/周期执行线程池（CPU密集型）
     */
    private static volatile MonitoredScheduledThreadPoolExecutor commonCpuIntensiveScheduledThreadPoolExecutor;

    /**
     * 延迟/周期执行线程池（IO密集型）
     */
    private static volatile MonitoredScheduledThreadPoolExecutor commonIoIntensiveScheduledThreadPoolExecutor;

    /**
     * <p>
     * 私有化构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2025/2/9 21:36
     */
    private ThreadPool() {
    }

    /**
     * <p>
     * 获取CPU密集型的线程池。<br>
     * corePoolSize：指定了线程池中的线程数量，它的数量决定了添加的任务是开辟新的线程去执行，还是放到workQueue任务队列中去。<br>
     * maxPoolSize：指定了线程池中的最大线程数量，这个参数会根据你使用的workQueue任务队列的类型，决定线程池会开辟的最大线程数量。<br>
     * keepAliveTime：线程空闲时间。当线程空闲时间达到keepAliveTime时，线程会退出（关闭），直到线程数等于核心线程数。<br>
     * workQueue：阻塞队列。建议使用有界队列，比如ArrayBlockingQueue。<br>
     * ThreadFactory：线程创建工厂。一般用来设置线程名称的。<br>
     * handler：拒绝策略。一般用来做日志记录等。<br>
     * </p>
     *
     * @return {@link  MonitoredThreadPoolExecutor} CPU密集型的线程池
     * @custom.date 2025/2/9 21:36
     */
    public static MonitoredThreadPoolExecutor getCommonCpuIntensiveThreadPoolExecutor() {
        if (commonCpuIntensiveThreadPoolExecutor == null) {
            synchronized (ThreadPool.class) {
                if (commonCpuIntensiveThreadPoolExecutor == null) {
                    commonCpuIntensiveThreadPoolExecutor = new MonitoredThreadPoolExecutor(
                            // 线程数 = Ncpu /（1 - 阻塞系数），CPU密集型阻塞系数相对较小
                            (int) (ProcessorsUtils.getAvailableProcessors() / (1 - 0.2)),
                            (int) (ProcessorsUtils.getAvailableProcessors() / (1 - 0.2)),
                            60L,
                            TimeUnit.SECONDS,
                            new LinkedBlockingQueue<>(Integer.MAX_VALUE),
                            new BasicThreadFactory.Builder()
                                    // 设置线程名
                                    .namingPattern("phoenix-common-cpu-intensive-pool-thread-%d")
                                    // 设置为守护线程
                                    .daemon(true)
                                    .build(),
                            new ThreadPoolExecutor.AbortPolicy(), "phoenix-common-cpu-intensive-pool", true);
                }
            }
        }
        return commonCpuIntensiveThreadPoolExecutor;
    }

    /**
     * <p>
     * 获取IO密集型的线程池。<br>
     * corePoolSize：指定了线程池中的线程数量，它的数量决定了添加的任务是开辟新的线程去执行，还是放到workQueue任务队列中去。<br>
     * maxPoolSize：指定了线程池中的最大线程数量，这个参数会根据你使用的workQueue任务队列的类型，决定线程池会开辟的最大线程数量。<br>
     * keepAliveTime：线程空闲时间。当线程空闲时间达到keepAliveTime时，线程会退出（关闭），直到线程数等于核心线程数。<br>
     * workQueue：阻塞队列。建议使用有界队列，比如ArrayBlockingQueue。<br>
     * ThreadFactory：线程创建工厂。一般用来设置线程名称的。<br>
     * handler：拒绝策略。一般用来做日志记录等。<br>
     * </p>
     *
     * @return {@link MonitoredThreadPoolExecutor} IO密集型的线程池
     * @custom.date 2025/2/9 21:36
     */
    public static MonitoredThreadPoolExecutor getCommonIoIntensiveThreadPoolExecutor() {
        if (commonIoIntensiveThreadPoolExecutor == null) {
            synchronized (ThreadPool.class) {
                if (commonIoIntensiveThreadPoolExecutor == null) {
                    commonIoIntensiveThreadPoolExecutor = new MonitoredThreadPoolExecutor(
                            // 线程数 = Ncpu /（1 - 阻塞系数），IO密集型阻塞系数相对较大
                            (int) (ProcessorsUtils.getAvailableProcessors() / (1 - 0.8)),
                            (int) (ProcessorsUtils.getAvailableProcessors() / (1 - 0.8)),
                            60L,
                            TimeUnit.SECONDS,
                            new LinkedBlockingQueue<>(Integer.MAX_VALUE),
                            new BasicThreadFactory.Builder()
                                    // 设置线程名
                                    .namingPattern("phoenix-common-io-intensive-pool-thread-%d")
                                    // 设置为守护线程
                                    .daemon(true)
                                    .build(),
                            new ThreadPoolExecutor.AbortPolicy(), "phoenix-common-io-intensive-pool", true);
                }
            }
        }
        return commonIoIntensiveThreadPoolExecutor;
    }

    /**
     * <p>
     * 获取延迟/周期执行线程池（CPU密集型）
     * </p>
     *
     * @return {@link MonitoredScheduledThreadPoolExecutor} 延迟/周期执行线程池（CPU密集型）
     * @custom.date 2025/2/9 21:36
     */
    public static MonitoredScheduledThreadPoolExecutor getCommonCpuIntensiveScheduledThreadPoolExecutor() {
        if (commonCpuIntensiveScheduledThreadPoolExecutor == null) {
            synchronized (ThreadPool.class) {
                if (commonCpuIntensiveScheduledThreadPoolExecutor == null) {
                    commonCpuIntensiveScheduledThreadPoolExecutor = new MonitoredScheduledThreadPoolExecutor(
                            // 线程数 = Ncpu /（1 - 阻塞系数），CPU密集型阻塞系数相对较小
                            (int) (ProcessorsUtils.getAvailableProcessors() / (1 - 0.2)),
                            new BasicThreadFactory.Builder()
                                    // 设置线程名
                                    .namingPattern("phoenix-common-cpu-intensive-scheduled-pool-thread-%d")
                                    // 设置为守护线程
                                    .daemon(true)
                                    .build(),
                            new ThreadPoolExecutor.AbortPolicy(), "phoenix-common-cpu-intensive-scheduled-pool", true);
                }
            }
        }
        return commonCpuIntensiveScheduledThreadPoolExecutor;
    }

    /**
     * <p>
     * 获取延迟/周期执行线程池（IO密集型）
     * </p>
     *
     * @return {@link MonitoredScheduledThreadPoolExecutor} 延迟/周期执行线程池（IO密集型）
     * @custom.date 2025/2/9 21:36
     */
    public static MonitoredScheduledThreadPoolExecutor getCommonIoIntensiveScheduledThreadPoolExecutor() {
        if (commonIoIntensiveScheduledThreadPoolExecutor == null) {
            synchronized (ThreadPool.class) {
                if (commonIoIntensiveScheduledThreadPoolExecutor == null) {
                    commonIoIntensiveScheduledThreadPoolExecutor = new MonitoredScheduledThreadPoolExecutor(
                            // 线程数 = Ncpu /（1 - 阻塞系数），IO密集型阻塞系数相对较大
                            (int) (ProcessorsUtils.getAvailableProcessors() / (1 - 0.8)),
                            new BasicThreadFactory.Builder()
                                    // 设置线程名
                                    .namingPattern("phoenix-common-io-intensive-scheduled-pool-thread-%d")
                                    // 设置为守护线程
                                    .daemon(true)
                                    .build(),
                            new ThreadPoolExecutor.AbortPolicy(), "phoenix-common-io-intensive-scheduled-pool", true);
                }
            }
        }
        return commonIoIntensiveScheduledThreadPoolExecutor;
    }

}