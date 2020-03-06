package com.transfar.business.core;

import com.transfar.business.dto.AgentRequestHeartbeatPackage;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
            ScheduledExecutorService seService = Executors.newScheduledThreadPool(1,
                    r -> new Thread(r, "monitoring-heartbeat-pool-thread-1"));
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
        AgentRequestHeartbeatPackage heartbeatPackage = new AgentRequestHeartbeatPackage();
        heartbeatPackage.setDateTime(new Date());
        // 向服务端发送心跳包
        MethodExecuteHandler.sendHeartbeatPackage2Server(heartbeatPackage);
    }
}
