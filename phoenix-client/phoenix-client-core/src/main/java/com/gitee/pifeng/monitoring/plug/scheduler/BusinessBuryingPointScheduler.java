package com.gitee.pifeng.monitoring.plug.scheduler;

import com.gitee.pifeng.monitoring.common.constant.ThreadTypeEnums;
import com.gitee.pifeng.monitoring.common.threadpool.MonitoredScheduledThreadPoolExecutor;
import com.gitee.pifeng.monitoring.common.threadpool.ThreadPool;

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
     * 定时运行业务埋点监控任务
     * </p>
     *
     * @param command        要执行的任务
     * @param initialDelay   初次埋点监控延迟的时间
     * @param period         两次埋点监控任务之间的时间间隔
     * @param unit           时间单位
     * @param threadTypeEnum 线程类型：CPU密集型、IO密集型
     * @return {@link MonitoredScheduledThreadPoolExecutor}
     * @author 皮锋
     * @custom.date 2020年3月5日 下午2:56:47
     */
    public static MonitoredScheduledThreadPoolExecutor run(Runnable command, long initialDelay, long period, TimeUnit unit, ThreadTypeEnums threadTypeEnum) {
        // CPU密集型
        if (threadTypeEnum == ThreadTypeEnums.CPU_INTENSIVE_THREAD) {
            final MonitoredScheduledThreadPoolExecutor executor = ThreadPool.getCommonCpuIntensiveScheduledThreadPoolExecutor();
            executor.scheduleAtFixedRate(command, initialDelay, period, unit);
            return executor;
        }
        // IO密集型
        else {
            final MonitoredScheduledThreadPoolExecutor executor = ThreadPool.getCommonIoIntensiveScheduledThreadPoolExecutor();
            executor.scheduleAtFixedRate(command, initialDelay, period, unit);
            return executor;
        }
    }

}
