package com.gitee.pifeng.monitoring.plug.scheduler;

import com.gitee.pifeng.monitoring.common.threadpool.ExecutorObject;
import com.gitee.pifeng.monitoring.plug.core.ConfigLoader;
import com.gitee.pifeng.monitoring.plug.thread.JvmThread;

import java.util.concurrent.TimeUnit;

import static com.gitee.pifeng.monitoring.plug.core.ThreadPoolAcquirer.JVM_SCHEDULED_EXECUTOR_SERVICE;

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
     * 如果监控配置文件中配置了发送Java虚拟机信息，则延迟45秒启动定时任务，定时发送Java虚拟机信息包，
     * 定时任务的执行频率一般为监控配置文件中配置的Java虚拟机信息包发送频率，如果监控配置文件中没有配置Java虚拟机信息包的发送频率，
     * 则由类{@link ConfigLoader}提供默认的发送Java虚拟机信息频率。
     * </p>
     *
     * @return {@link ExecutorObject}
     * @author 皮锋
     * @custom.date 2020年3月7日 下午4:43:35
     */
    public static ExecutorObject run() {
        // 是否发送Java虚拟机
        boolean jvmInfoEnable = ConfigLoader.getMonitoringProperties().getJvmInfo().getEnable();
        if (jvmInfoEnable) {
            // 发送Java虚拟机的频率
            long rate = ConfigLoader.getMonitoringProperties().getJvmInfo().getRate();
            JVM_SCHEDULED_EXECUTOR_SERVICE.scheduleAtFixedRate(new JvmThread(), 45, rate, TimeUnit.SECONDS);
            return ExecutorObject.builder()
                    .scheduledExecutorService(JVM_SCHEDULED_EXECUTOR_SERVICE)
                    .name("phoenix-jvm-pool-thread")
                    .build();
        }
        return null;
    }

}
