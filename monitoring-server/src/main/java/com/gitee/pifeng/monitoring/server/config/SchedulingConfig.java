package com.gitee.pifeng.monitoring.server.config;

import com.gitee.pifeng.monitoring.common.threadpool.ThreadPool;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.config.Task;

/**
 * <p>
 * 调度配置
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021年4月2日 下午20:46:33
 */
@Configuration
@EnableScheduling
public class SchedulingConfig implements SchedulingConfigurer {

    /**
     * <p>
     * 回调，允许针对给定的{@link ScheduledTaskRegistrar}注册{@link TaskScheduler}和特定{@link Task}实例。
     * </p>
     *
     * @param scheduledTaskRegistrar 要配置的注册器
     * @author 皮锋
     * @custom.date 2021年4月2日 下午20:46:33
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(ThreadPool.COMMON_IO_INTENSIVE_SCHEDULED_THREAD_POOL);
    }

}
