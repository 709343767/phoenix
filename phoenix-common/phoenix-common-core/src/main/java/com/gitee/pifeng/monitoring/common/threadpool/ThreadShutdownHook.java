package com.gitee.pifeng.monitoring.common.threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

    /**
     * <p>
     * 执行关闭钩子，优雅关闭线程池
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/11/29 10:38
     */
    public void executeShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // CPU密集型的线程池
            ThreadPoolExecutor commonCpuIntensiveThreadPool = ThreadPool.COMMON_CPU_INTENSIVE_THREAD_POOL;
            this.shutdownGracefully(commonCpuIntensiveThreadPool, "phoenix-common-cpu-intensive-thread");
            // IO密集型的线程池
            ThreadPoolExecutor commonIoIntensiveThreadPool = ThreadPool.COMMON_IO_INTENSIVE_THREAD_POOL;
            this.shutdownGracefully(commonIoIntensiveThreadPool, "phoenix-common-io-intensive-thread");
            // 延迟/周期执行线程池（CPU密集型）
            ScheduledExecutorService commonCpuIntensiveScheduledThreadPool = ThreadPool.COMMON_CPU_INTENSIVE_SCHEDULED_THREAD_POOL;
            this.shutdownGracefully(commonCpuIntensiveScheduledThreadPool, "phoenix-common-cpu-intensive-scheduled");
            // 延迟/周期执行线程池（IO密集型）
            ScheduledExecutorService commonIoIntensiveScheduledThreadPool = ThreadPool.COMMON_IO_INTENSIVE_SCHEDULED_THREAD_POOL;
            this.shutdownGracefully(commonIoIntensiveScheduledThreadPool, "phoenix-common-io-intensive-scheduled");
        }));
    }

    /**
     * <p>
     * 优雅的关闭线程池
     * </p>
     *
     * @param executorService {@link ExecutorService}
     * @param alias           线程池别名
     * @author 皮锋
     * @custom.date 2020/11/29 12:30
     */
    public void shutdownGracefully(ExecutorService executorService, String alias) {
        try {
            if (!executorService.isShutdown()) {
                log.info("“{}”线程池开始关闭！", alias);
                // 使新任务无法提交
                executorService.shutdown();
                // 超时时长
                long timeout = 15L;
                // 等待未完成任务结束
                if (!executorService.awaitTermination(timeout, TimeUnit.SECONDS)) {
                    // 取消当前执行的任务
                    executorService.shutdownNow();
                    log.warn("中断工作进程，这可能导致某些任务不一致。请检查业务日志！");
                }
                // 等待任务取消的响应
                if (!executorService.awaitTermination(timeout, TimeUnit.SECONDS)) {
                    log.error("即使工作线程中断，线程池也无法关闭，这可能会导致某些任务不一致。请检查业务日志！");
                }
            }
        } catch (InterruptedException e) {
            // 重新取消当前线程进行中断
            executorService.shutdownNow();
            // 保留中断状态
            Thread.currentThread().interrupt();
        }
        log.info("“{}”线程池关闭！", alias);
    }

}
