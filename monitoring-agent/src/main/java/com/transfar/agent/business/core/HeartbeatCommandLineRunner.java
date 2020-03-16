package com.transfar.agent.business.core;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.transfar.common.dto.HeartbeatPackage;

/**
 * <p>
 * 在容器启动的时候定时向服务端发心跳包
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月5日 上午10:26:19
 */
@Component
public class HeartbeatCommandLineRunner implements CommandLineRunner {

    @Override
    public void run(String... args) {
        // 重新开启线程，让他单独去做我们想要做的操作，此时CommandLineRunner执行的操作和主线程是相互独立的，抛出异常并不会影响到主线程
        Thread thread = new Thread(() -> {
            final ScheduledExecutorService seService = Executors.newScheduledThreadPool(5, new ThreadFactory() {
                AtomicInteger atomic = new AtomicInteger();

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "monitoring-heartbeat-pool-thread-" + this.atomic.getAndIncrement());
                }
            });
            seService.scheduleAtFixedRate(new HeartbeatScheduledExecutor(), 30,
                    ConfigLoader.monitoringProperties.getHeartbeatProperties().getRate(), TimeUnit.SECONDS);
        });
        // 设置守护线程
        thread.setDaemon(true);
        // 开始执行分进程
        thread.start();
    }
}

/**
 * <p>
 * 心跳包调度程序执行器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月5日 上午11:27:31
 */
class HeartbeatScheduledExecutor implements Runnable {

    @Override
    public void run() {
        HeartbeatPackage heartbeatPackage = new PackageConstructor().structureHeartbeatPackage();
        // 向服务端发送心跳包
        MethodExecuteHandler.sendHeartbeatPackage2Server(heartbeatPackage);
    }
}
