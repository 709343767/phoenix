package com.transfar.business.server.core;

import com.transfar.business.server.domain.Instance;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * 在容器启动后，定时扫描应用实例池中的所有应用，实时更新应用实例状态
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/12 12:07
 */
@Slf4j
@Component
public class InstanceCommandLineRunner implements CommandLineRunner {

    /**
     * 应用实例池
     */
    @Autowired
    private InstancePool instancePool;

    @Override
    public void run(String... args) {
        // 重新开启线程，让他单独去做我们想要做的操作，此时CommandLineRunner执行的操作和主线程是相互独立的，抛出异常并不会影响到主线程
        Thread thread = new Thread(() -> {
            final ScheduledExecutorService seService = Executors.newScheduledThreadPool(5, new ThreadFactory() {
                AtomicInteger atomic = new AtomicInteger();

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "monitoring-instance-pool-thread-" + this.atomic.getAndIncrement());
                }
            });
            seService.scheduleAtFixedRate(() -> {
                        // 循环所有应用实例
                        for (Map.Entry<String, Instance> entry : this.instancePool.entrySet()) {
                            String key = entry.getKey();
                            Instance instance = entry.getValue();
                            // 允许的误差时间
                            int thresholdSecond = instance.getThresholdSecond();
                            // 最后一次通过心跳包更新的时间
                            Date dateTime = instance.getDateTime();
                            // 判决时间
                            DateTime judgeDateTime = new DateTime(dateTime).plusSeconds(thresholdSecond);
                            if (judgeDateTime.isBeforeNow()) {
                                instancePool.replace(key, instance.setOnline(false));
                            }
                        }
                        // 打印当前应用池中的所有应用
                        log.info("当前应用实例池中的应用：{}", this.instancePool.toJsonString());
                    }, 30, 30
                    , TimeUnit.SECONDS);
        });
        // 设置守护线程
        thread.setDaemon(true);
        // 开始执行分进程
        thread.start();
    }
}
