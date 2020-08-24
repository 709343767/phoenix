package com.imby.plug.scheduler;

import com.imby.common.domain.Result;
import com.imby.plug.core.ConfigLoader;
import com.imby.plug.thread.CheckIOThread;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 检测HTTP连接IO情况的任务调度器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/16 11:54
 */
@Slf4j
@Deprecated
public class CheckIOTaskScheduler {

    /**
     * 线程池。<br>
     * corePoolSize：核心线程数。核心线程会一直存在，即使没有任务执行；当线程数小于核心线程数的时候，即使有空闲线程，也会一直创建线程直到达到核心线程数；通常设置为1就可以了。<br>
     * maxPoolSize：最大线程数。是线程池里允许存在的最大线程数量。<br>
     * keepAliveTime：线程空闲时间。当线程空闲时间达到keepAliveTime时，线程会退出（关闭），直到线程数等于核心线程数。<br>
     * workQueue：阻塞队列。建议使用有界队列，比如ArrayBlockingQueue。<br>
     * ThreadFactory：线程创建工厂。一般用来设置线程名称的。<br>
     * handler：拒绝策略。一般用来做日志记录等。<br>
     */
    private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(1,
            16,
            1L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1024),
            new BasicThreadFactory.Builder()
                    // 设置线程名
                    .namingPattern("monitoring-checkIO-pool-thread-%d")
                    // 设置为守护线程
                    .daemon(true)
                    .build(),
            new ThreadPoolExecutor.AbortPolicy());

    /**
     * <p>
     * 检测与监控服务端或者监控代理端的IO情况
     * </p>
     *
     * @return 与监控服务端或者监控代理端的IO是否正常
     * @author 皮锋
     * @custom.date 2020/8/16 17:49
     */
    public static boolean call() {
        try {
            Future<Result> scheduledFuture = THREAD_POOL_EXECUTOR.submit(new CheckIOThread());
            Result result = scheduledFuture.get();
            boolean b = result.isSuccess();
            if (!b) {
                // 休眠一会，时间为监控配置文件中配置的心跳频率
                Thread.sleep(ConfigLoader.monitoringProperties.getHeartbeatProperties().getRate() * 1000);
                // 递归调用
                b = call();
            } else {
                // 关闭线程池
                THREAD_POOL_EXECUTOR.shutdown();
            }
            return b;
        } catch (Exception e) {
            log.error("检测与监控服务端或者监控代理端的IO情况异常！", e);
        }
        return false;
    }

}
