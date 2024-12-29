package com.gitee.pifeng.monitoring.server.config;

import com.gitee.pifeng.monitoring.common.util.server.ProcessorsUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 线程池配置
 * </p>
 *
 * @author 皮锋
 * @custom.date 2024年12月05日 下午18:25:50
 */
@Configuration
public class ThreadPoolConfig {

    /**
     * 数据库服务状态监控线程池
     *
     * @return {@link ThreadPoolExecutor}
     * @author 皮锋
     * @custom.date 2024/12/05 19:48
     */
    @Lazy
    @Bean(name = "dbMonitorThreadPoolExecutor", destroyMethod = "shutdown")
    public ThreadPoolExecutor dbMonitorThreadPoolExecutor() {
        return new ThreadPoolExecutor(
                // 线程数 = Ncpu /（1 - 阻塞系数），IO密集型阻塞系数相对较大
                (int) (ProcessorsUtils.getAvailableProcessors() / (1 - 0.8)),
                (int) (ProcessorsUtils.getAvailableProcessors() / (1 - 0.8)),
                1L,
                TimeUnit.HOURS,
                new LinkedBlockingQueue<>(Integer.MAX_VALUE),
                new BasicThreadFactory.Builder()
                        // 设置线程名
                        .namingPattern("phoenix-server-db-monitor-pool-thread-%d")
                        // 设置为守护线程
                        .daemon(true)
                        .build(),
                new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * 数据库表空间监控线程池
     *
     * @return {@link ThreadPoolExecutor}
     * @author 皮锋
     * @custom.date 2024/12/05 19:48
     */
    @Lazy
    @Bean(name = "dbTbsMonitorThreadPoolExecutor", destroyMethod = "shutdown")
    public ThreadPoolExecutor dbTbsMonitorThreadPoolExecutor() {
        return new ThreadPoolExecutor(
                // 线程数 = Ncpu /（1 - 阻塞系数），IO密集型阻塞系数相对较大
                (int) (ProcessorsUtils.getAvailableProcessors() / (1 - 0.8)),
                (int) (ProcessorsUtils.getAvailableProcessors() / (1 - 0.8)),
                1L,
                TimeUnit.HOURS,
                new LinkedBlockingQueue<>(Integer.MAX_VALUE),
                new BasicThreadFactory.Builder()
                        // 设置线程名
                        .namingPattern("phoenix-server-db-tbs-monitor-pool-thread-%d")
                        // 设置为守护线程
                        .daemon(true)
                        .build(),
                new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * HTTP服务状态监控线程池
     *
     * @return {@link ThreadPoolExecutor}
     * @author 皮锋
     * @custom.date 2024/12/05 19:48
     */
    @Lazy
    @Bean(name = "httpMonitorThreadPoolExecutor", destroyMethod = "shutdown")
    public ThreadPoolExecutor httpMonitorThreadPoolExecutor() {
        return new ThreadPoolExecutor(
                // 线程数 = Ncpu /（1 - 阻塞系数），IO密集型阻塞系数相对较大
                (int) (ProcessorsUtils.getAvailableProcessors() / (1 - 0.8)),
                (int) (ProcessorsUtils.getAvailableProcessors() / (1 - 0.8)),
                1L,
                TimeUnit.HOURS,
                new LinkedBlockingQueue<>(Integer.MAX_VALUE),
                new BasicThreadFactory.Builder()
                        // 设置线程名
                        .namingPattern("phoenix-server-http-monitor-pool-thread-%d")
                        // 设置为守护线程
                        .daemon(true)
                        .build(),
                new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * 网络服务状态监控线程池
     *
     * @return {@link ThreadPoolExecutor}
     * @author 皮锋
     * @custom.date 2024/12/05 19:48
     */
    @Lazy
    @Bean(name = "netMonitorThreadPoolExecutor", destroyMethod = "shutdown")
    public ThreadPoolExecutor netMonitorThreadPoolExecutor() {
        return new ThreadPoolExecutor(
                // 线程数 = Ncpu /（1 - 阻塞系数），IO密集型阻塞系数相对较大
                (int) (ProcessorsUtils.getAvailableProcessors() / (1 - 0.8)),
                (int) (ProcessorsUtils.getAvailableProcessors() / (1 - 0.8)),
                1L,
                TimeUnit.HOURS,
                new LinkedBlockingQueue<>(Integer.MAX_VALUE),
                new BasicThreadFactory.Builder()
                        // 设置线程名
                        .namingPattern("phoenix-server-net-monitor-pool-thread-%d")
                        // 设置为守护线程
                        .daemon(true)
                        .build(),
                new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * TCP服务状态监控线程池
     *
     * @return {@link ThreadPoolExecutor}
     * @author 皮锋
     * @custom.date 2024/12/05 19:48
     */
    @Lazy
    @Bean(name = "tcpMonitorThreadPoolExecutor", destroyMethod = "shutdown")
    public ThreadPoolExecutor tcpMonitorThreadPoolExecutor() {
        return new ThreadPoolExecutor(
                // 线程数 = Ncpu /（1 - 阻塞系数），IO密集型阻塞系数相对较大
                (int) (ProcessorsUtils.getAvailableProcessors() / (1 - 0.8)),
                (int) (ProcessorsUtils.getAvailableProcessors() / (1 - 0.8)),
                1L,
                TimeUnit.HOURS,
                new LinkedBlockingQueue<>(Integer.MAX_VALUE),
                new BasicThreadFactory.Builder()
                        // 设置线程名
                        .namingPattern("phoenix-server-tcp-monitor-pool-thread-%d")
                        // 设置为守护线程
                        .daemon(true)
                        .build(),
                new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * 告警服务线程池
     *
     * @return {@link ThreadPoolExecutor}
     * @author 皮锋
     * @custom.date 2024/12/05 19:48
     */
    @Lazy
    @Bean(name = "alarmThreadPoolExecutor", destroyMethod = "shutdown")
    public ThreadPoolExecutor alarmThreadPoolExecutor() {
        return new ThreadPoolExecutor(
                // 线程数 = Ncpu /（1 - 阻塞系数），IO密集型阻塞系数相对较大
                (int) (ProcessorsUtils.getAvailableProcessors() / (1 - 0.8)),
                (int) (ProcessorsUtils.getAvailableProcessors() / (1 - 0.8)),
                1L,
                TimeUnit.HOURS,
                new LinkedBlockingQueue<>(Integer.MAX_VALUE),
                new BasicThreadFactory.Builder()
                        // 设置线程名
                        .namingPattern("phoenix-server-alarm-pool-thread-%d")
                        // 设置为守护线程
                        .daemon(true)
                        .build(),
                new ThreadPoolExecutor.AbortPolicy());
    }

}
