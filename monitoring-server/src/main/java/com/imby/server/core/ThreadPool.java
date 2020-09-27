package com.imby.server.core;

import com.imby.common.util.CpuUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
     * corePoolSize：核心线程数。核心线程会一直存在，即使没有任务执行；当线程数小于核心线程数的时候，即使有空闲线程，也会一直创建线程直到达到核心线程数；通常设置为1就可以了。<br>
     * maxPoolSize：最大线程数。是线程池里允许存在的最大线程数量。<br>
     * keepAliveTime：线程空闲时间。当线程空闲时间达到keepAliveTime时，线程会退出（关闭），直到线程数等于核心线程数。<br>
     * workQueue：阻塞队列。建议使用有界队列，比如ArrayBlockingQueue。<br>
     * ThreadFactory：线程创建工厂。一般用来设置线程名称的。<br>
     * handler：拒绝策略。一般用来做日志记录等。<br>
     */
    public static final ThreadPoolExecutor CPU_INTENSIVE_THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(1,
            // 线程数 = Ncpu /（1 - 阻塞系数），CPU密集型阻塞系数相对较小
            (int) (CpuUtils.getAvailableProcessors() / (1 - 0.2)),
            1L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1024),
            new BasicThreadFactory.Builder()
                    // 设置线程名
                    .namingPattern("monitoring-common-pool-thread-%d")
                    // 设置为守护线程
                    .daemon(true)
                    .build(),
            new ThreadPoolExecutor.AbortPolicy());

}
