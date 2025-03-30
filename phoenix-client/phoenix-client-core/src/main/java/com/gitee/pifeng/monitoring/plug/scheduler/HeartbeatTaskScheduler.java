package com.gitee.pifeng.monitoring.plug.scheduler;

import com.gitee.pifeng.monitoring.plug.core.ConfigLoader;
import com.gitee.pifeng.monitoring.plug.core.ThreadPoolAcquirer;
import com.gitee.pifeng.monitoring.plug.thread.HeartbeatThread;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 心跳任务调度器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月5日 下午2:54:21
 */
public class HeartbeatTaskScheduler {

    /**
     * <p>
     * 私有化构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/10/27 13:26
     */
    private HeartbeatTaskScheduler() {
    }

    /**
     * <p>
     * 延时35秒后定时发送心跳包，发送心跳包的频率一般为监控配置文件中配置的心跳频率，如果监控配置文件中没配置心跳频率，
     * 则由类{@link ConfigLoader}提供默认心跳频率。
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020年3月5日 下午2:56:47
     */
    public static void run() {
        // 心跳频率
        long rate = ConfigLoader.getMonitoringProperties().getHeartbeat().getRate();
        ThreadPoolAcquirer.getHeartbeatScheduledThreadPoolExecutor().scheduleWithFixedDelay(new HeartbeatThread(), 35, rate, TimeUnit.SECONDS);
    }

}
