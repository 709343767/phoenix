package com.gitee.pifeng.monitoring.common.threadpool;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <p>
 * 自定义的带有监控管理功能的线程池执行器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2025/1/21 21:40
 */
@Slf4j
public class MonitoredThreadPoolExecutor extends ThreadPoolExecutor {

    /**
     * 拒绝的任务数量
     */
    private final AtomicLong rejectedTaskCount = new AtomicLong(0);

    /**
     * 使用给定的初始值创建新的{@link  MonitoredThreadPoolExecutor}
     *
     * @param corePoolSize    核心线程数
     * @param maximumPoolSize 最大线程数
     * @param keepAliveTime   当线程数大于核心线程数（corePoolSize）时，多余的空闲线程在终止前等待新任务的最长时间
     * @param unit            keepAliveTime参数的时间单位
     * @param workQueue       来存放等待执行任务的队列
     * @param threadPoolName  线程池名字
     * @param needShutdown    是否需要管理线程池关闭
     * @throws IllegalArgumentException 非法的参数异常
     * @throws NullPointerException     空指针异常
     * @author 皮锋
     * @custom.date 2025/1/21 21:40
     */
    public MonitoredThreadPoolExecutor(int corePoolSize,
                                       int maximumPoolSize,
                                       long keepAliveTime,
                                       TimeUnit unit,
                                       BlockingQueue<Runnable> workQueue,
                                       String threadPoolName,
                                       boolean needShutdown) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        // 注册线程池
        ThreadPoolManager.register(threadPoolName, this, needShutdown);
    }

    /**
     * 使用给定的初始值创建新的{@link  MonitoredThreadPoolExecutor}
     *
     * @param corePoolSize    核心线程数
     * @param maximumPoolSize 最大线程数
     * @param keepAliveTime   当线程数大于核心线程数（corePoolSize）时，多余的空闲线程在终止前等待新任务的最长时间
     * @param unit            keepAliveTime参数的时间单位
     * @param workQueue       来存放等待执行任务的队列
     * @param handler         当线程池无法接受新任务时的行为
     * @param threadPoolName  线程池名字
     * @param needShutdown    是否需要管理线程池关闭
     * @throws IllegalArgumentException 非法的参数异常
     * @throws NullPointerException     空指针异常
     * @author 皮锋
     * @custom.date 2025/1/21 21:40
     */
    public MonitoredThreadPoolExecutor(int corePoolSize,
                                       int maximumPoolSize,
                                       long keepAliveTime,
                                       TimeUnit unit,
                                       BlockingQueue<Runnable> workQueue,
                                       RejectedExecutionHandler handler,
                                       String threadPoolName,
                                       boolean needShutdown) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
        // 注册线程池
        ThreadPoolManager.register(threadPoolName, this, needShutdown);
    }

    /**
     * 使用给定的初始值创建新的{@link  MonitoredThreadPoolExecutor}
     *
     * @param corePoolSize    核心线程数
     * @param maximumPoolSize 最大线程数
     * @param keepAliveTime   当线程数大于核心线程数（corePoolSize）时，多余的空闲线程在终止前等待新任务的最长时间
     * @param unit            keepAliveTime参数的时间单位
     * @param workQueue       来存放等待执行任务的队列
     * @param threadFactory   用来创建新线程的工厂
     * @param threadPoolName  线程池名字
     * @param needShutdown    是否需要管理线程池关闭
     * @throws IllegalArgumentException 非法的参数异常
     * @throws NullPointerException     空指针异常
     * @author 皮锋
     * @custom.date 2025/1/21 21:40
     */
    public MonitoredThreadPoolExecutor(int corePoolSize,
                                       int maximumPoolSize,
                                       long keepAliveTime,
                                       TimeUnit unit,
                                       BlockingQueue<Runnable> workQueue,
                                       ThreadFactory threadFactory,
                                       String threadPoolName,
                                       boolean needShutdown) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        // 注册线程池
        ThreadPoolManager.register(threadPoolName, this, needShutdown);
    }

    /**
     * 使用给定的初始值创建新的{@link  MonitoredThreadPoolExecutor}
     *
     * @param corePoolSize    核心线程数
     * @param maximumPoolSize 最大线程数
     * @param keepAliveTime   当线程数大于核心线程数（corePoolSize）时，多余的空闲线程在终止前等待新任务的最长时间
     * @param unit            keepAliveTime参数的时间单位
     * @param workQueue       来存放等待执行任务的队列
     * @param threadFactory   用来创建新线程的工厂
     * @param handler         当线程池无法接受新任务时的行为
     * @param threadPoolName  线程池名字
     * @param needShutdown    是否需要管理线程池关闭
     * @throws IllegalArgumentException 非法的参数异常
     * @throws NullPointerException     空指针异常
     * @author 皮锋
     * @custom.date 2025/1/21 21:40
     */
    public MonitoredThreadPoolExecutor(int corePoolSize,
                                       int maximumPoolSize,
                                       long keepAliveTime,
                                       TimeUnit unit,
                                       BlockingQueue<Runnable> workQueue,
                                       ThreadFactory threadFactory,
                                       RejectedExecutionHandler handler,
                                       String threadPoolName,
                                       boolean needShutdown) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        // 注册线程池
        ThreadPoolManager.register(threadPoolName, this, needShutdown);
    }

    @Override
    public void execute(@NonNull Runnable command) {
        try {
            super.execute(command);
        } catch (RejectedExecutionException e) {
            this.handleRejection(command, e);
            throw e;
        }
    }

    @NonNull
    @Override
    public Future<?> submit(@NonNull Runnable task) {
        try {
            return super.submit(task);
        } catch (RejectedExecutionException e) {
            this.handleRejection(task, e);
            throw e;
        }
    }

    @NonNull
    @Override
    public <T> Future<T> submit(@NonNull Callable<T> task) {
        try {
            return super.submit(task);
        } catch (RejectedExecutionException e) {
            this.handleRejection(task, e);
            throw e;
        }
    }

    @NonNull
    @Override
    public <T> Future<T> submit(@NonNull Runnable task, T result) {
        try {
            return super.submit(task, result);
        } catch (RejectedExecutionException e) {
            this.handleRejection(task, e);
            throw e;
        }
    }

    /**
     * <p>
     * 处理任务被线程池拒绝的情况
     * </p>
     *
     * @param task 任务
     * @param exp  {@link RejectedExecutionException} 当尝试将任务提交给线程池执行时，如果线程池无法接受新任务（例如因为线程池已满或已关闭），就会抛出这个异常
     * @author 皮锋
     * @custom.date 2025/1/22 09:24
     */
    private void handleRejection(Object task, RejectedExecutionException exp) {
        this.rejectedTaskCount.incrementAndGet();
        log.error("任务 {} 被线程池 {} 拒绝！", task, this, exp);
    }

    /**
     * <p>
     * 获取拒绝的任务数量
     * </p>
     *
     * @return 拒绝的任务数量
     * @author 皮锋
     * @custom.date 2025/1/22 09:20
     */
    public long getRejectedTaskCount() {
        return this.rejectedTaskCount.get();
    }

}
