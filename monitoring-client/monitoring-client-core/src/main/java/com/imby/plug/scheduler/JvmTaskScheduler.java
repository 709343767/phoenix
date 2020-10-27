package com.imby.plug.scheduler;

import com.imby.plug.core.ConfigLoader;
import com.imby.plug.thread.JvmThread;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 发送Java虚拟机信息任务调度器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/14 21:20
 */
public class JvmTaskScheduler {

    /**
     * <p>
     * 私有化构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/10/27 13:26
     */
    private JvmTaskScheduler() {
    }

    /**
     * <p>
     * 如果监控配置文件中配置了发送Java虚拟机，则延迟15秒启动定时任务，定时发送Java虚拟机包，
     * 定时任务的执行频率一般为监控配置文件中配置的Java虚拟机包发送频率，如果监控配置文件中没有配置Java虚拟机包的发送频率，
     * 则由类{@link ConfigLoader}提供默认的发送Java虚拟机频率。
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020年3月7日 下午4:43:35
     */
    public static void run() {
        // 是否发送Java虚拟机
        boolean jvmInfoEnable = ConfigLoader.monitoringProperties.getJvmInfoProperties().isEnable();
        if (jvmInfoEnable) {
            final ScheduledExecutorService seService = new ScheduledThreadPoolExecutor(1,
                    new BasicThreadFactory.Builder()
                            // 设置线程名
                            .namingPattern("monitoring-jvm-pool-thread-%d")
                            // 设置为守护线程
                            .daemon(true)
                            .build());
            // 发送Java虚拟机的频率
            long rate = ConfigLoader.monitoringProperties.getJvmInfoProperties().getRate();
            seService.scheduleAtFixedRate(new JvmThread(), 15, rate, TimeUnit.SECONDS);
        }
    }

}
