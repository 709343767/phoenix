package com.gitee.pifeng.monitoring.server.config;

import com.gitee.pifeng.monitoring.common.threadpool.ThreadPool;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;

/**
 * <p>
 * 异步配置
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021年4月2日 下午20:58:33
 */
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    /**
     * <p>
     * 异步方法调用时要使用的执行器实例。
     * </p>
     *
     * @author 皮锋
     * @custom.date 2021年4月2日 下午21:05:38
     */
    @Override
    public Executor getAsyncExecutor() {
        // IO密集型的线程池
        return ThreadPool.getCommonIoIntensiveThreadPoolExecutor();
    }

}
