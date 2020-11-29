package com.imby.common.threadpool;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 公共的线程关闭钩子。
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/11/29 10:23
 */
@Slf4j
public class ThreadShutdownHook {

    static {
        executeShutdownHook();
    }

    /**
     * <p>
     * 执行关闭钩子
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/11/29 10:38
     */
    private static void executeShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (!ThreadPool.COMMON_CPU_INTENSIVE_THREAD_POOL.isShutdown()) {
                ThreadPool.COMMON_CPU_INTENSIVE_THREAD_POOL.shutdown();
                log.info("{}", "CPU密集型线程池关闭！");
            }
            if (!ThreadPool.COMMON_IO_INTENSIVE_THREAD_POOL.isShutdown()) {
                ThreadPool.COMMON_IO_INTENSIVE_THREAD_POOL.shutdown();
                log.info("{}", "IO密集型线程池关闭！");
            }
            if (!ThreadPool.COMMON_CPU_INTENSIVE_SCHEDULED_THREAD_POOL.isShutdown()) {
                ThreadPool.COMMON_CPU_INTENSIVE_SCHEDULED_THREAD_POOL.shutdown();
                log.info("{}", "延迟/周期执行线程池（CPU密集型）关闭！");
            }
            if (!ThreadPool.COMMON_IO_INTENSIVE_SCHEDULED_THREAD_POOL.isShutdown()) {
                ThreadPool.COMMON_IO_INTENSIVE_SCHEDULED_THREAD_POOL.shutdown();
                log.info("{}", "延迟/周期执行线程池（IO密集型）关闭！");
            }
        }));
    }

}
