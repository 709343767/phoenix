package com.gitee.pifeng.monitoring.common.threadpool;

import lombok.Builder;
import lombok.Data;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * <p>
 * 线程池执行器对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2023/5/31 13:12
 */
@Data
@Builder
public class ExecutorObject {

    /**
     * 名称
     */
    private String name;

    /**
     * {@link ThreadPoolExecutor}
     */
    private ThreadPoolExecutor threadPoolExecutor;

    /**
     * {@link ScheduledExecutorService}
     */
    private ScheduledExecutorService scheduledExecutorService;

}
