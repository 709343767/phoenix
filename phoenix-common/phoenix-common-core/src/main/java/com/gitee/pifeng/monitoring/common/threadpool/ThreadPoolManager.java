package com.gitee.pifeng.monitoring.common.threadpool;

import com.gitee.pifeng.monitoring.common.exception.MonitoringUniversalException;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 线程池管理器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2025/1/18 21:38
 */
@Slf4j
public class ThreadPoolManager {

    /**
     * 注册的需要关闭的线程池
     */
    private static final Map<String, ThreadPoolExecutor> NEED_SHUTDOWN_THREAD_POOLS = Maps.newConcurrentMap();

    /**
     * 注册的不需要关闭的线程池
     */
    private static final Map<String, ThreadPoolExecutor> UN_NEED_SHUTDOWN_THREAD_POOLS = Maps.newConcurrentMap();

    /**
     * <p>
     * 注册线程池
     * </p>
     *
     * @param threadPoolName 线程池名字
     * @param executor       {@link ThreadPoolExecutor} 线程池执行器
     * @param needShutdown   是否需要管理线程池关闭
     * @author 皮锋
     * @custom.date 2025/1/18 21:43
     */
    public static void register(String threadPoolName, ThreadPoolExecutor executor, boolean needShutdown) {
        if (needShutdown) {
            // 使用 putIfAbsent 确保原子性
            if (NEED_SHUTDOWN_THREAD_POOLS.putIfAbsent(threadPoolName, executor) != null) {
                throw new MonitoringUniversalException("线程池已经存在，无法注册，请修改线程池名字！");
            }
        } else {
            if (UN_NEED_SHUTDOWN_THREAD_POOLS.putIfAbsent(threadPoolName, executor) != null) {
                throw new MonitoringUniversalException("线程池已经存在，无法注册，请修改线程池名字！");
            }
        }
    }

    /**
     * <p>
     * 取消注册线程池
     * </p>
     *
     * @param threadPoolName 线程池名字
     * @author 皮锋
     * @custom.date 2025/1/19 13:29
     */
    public static void unregister(String threadPoolName) {
        NEED_SHUTDOWN_THREAD_POOLS.remove(threadPoolName);
        UN_NEED_SHUTDOWN_THREAD_POOLS.remove(threadPoolName);
    }

    /**
     * <p>
     * 优雅地关闭所有需要关闭的线程池并且取消注册
     * </p>
     *
     * @author 皮锋
     * @custom.date 2025/1/21 23:00
     */
    public static void shutdownAllGracefullyAndUnregister() {
        List<Map.Entry<String, ThreadPoolExecutor>> entries = Lists.newArrayList(NEED_SHUTDOWN_THREAD_POOLS.entrySet());
        for (Map.Entry<String, ThreadPoolExecutor> entry : entries) {
            String key = entry.getKey();
            ThreadPoolExecutor executor = entry.getValue();
            // 优雅地关闭线程池
            shutdownGracefully(executor, key);
            // 取消注册线程池
            unregister(key);
        }
    }

    /**
     * <p>
     * 优雅地关闭线程池
     * </p>
     *
     * @param threadPoolExecutor {@link ThreadPoolExecutor}
     * @param alias              线程池别名
     * @author 皮锋
     * @custom.date 2020/11/29 12:30
     */
    public static void shutdownGracefully(ThreadPoolExecutor threadPoolExecutor, String alias) {
        try {
            if (!threadPoolExecutor.isShutdown()) {
                log.info("“{}”线程池开始关闭！", alias);
                // 使新任务无法提交
                threadPoolExecutor.shutdown();
                // 超时时长
                long timeout = 15L;
                // 等待未完成任务结束
                if (!threadPoolExecutor.awaitTermination(timeout, TimeUnit.SECONDS)) {
                    // 取消当前执行的任务
                    threadPoolExecutor.shutdownNow();
                    log.warn("中断工作进程，这可能导致某些任务不一致。请检查业务日志！");
                }
                // 等待任务取消的响应
                if (!threadPoolExecutor.awaitTermination(timeout, TimeUnit.SECONDS)) {
                    log.error("即使工作线程中断，线程池也无法关闭，这可能会导致某些任务不一致。请检查业务日志！");
                }
            }
        } catch (InterruptedException e) {
            // 重新取消当前线程进行中断
            threadPoolExecutor.shutdownNow();
            // 保留中断状态
            Thread.currentThread().interrupt();
        }
        log.info("“{}”线程池关闭！", alias);
    }

}
