package com.imby.plug.scheduler;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 业务埋点任务调度器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/24 20:25
 */
public class BusinessBuryingPointScheduler {

    /**
     * <p>
     * 私有化构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/10/27 13:26
     */
    private BusinessBuryingPointScheduler() {
    }

    /**
     * <p>
     * 定时运行业务埋点监测任务
     * </p>
     *
     * @param command      要执行的任务
     * @param isDaemon     是否设置为守护线程
     * @param initialDelay 初次埋点监测延迟的时间
     * @param period       两次埋点监测任务之间的时间间隔
     * @param unit         时间单位
     * @return {@link ScheduledExecutorService}
     * @author 皮锋
     * @custom.date 2020年3月5日 下午2:56:47
     */
    public static ScheduledExecutorService run(Runnable command, boolean isDaemon, long initialDelay, long period, TimeUnit unit) {
        final ScheduledExecutorService seService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder()
                        // 设置为守护线程
                        .daemon(isDaemon)
                        .build());
        seService.scheduleAtFixedRate(command, initialDelay, period, unit);
        return seService;
    }

}
