package com.transfar.agent.business.core;

import com.transfar.common.dto.HeartbeatPackage;
import com.transfar.common.property.MonitoringProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * 在容器启动的时候定时向服务端发心跳包
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月5日 上午10:26:19
 */
@Slf4j
@Component
public class HeartbeatCommandLineRunner implements CommandLineRunner, DisposableBean {

    /**
     * 监控配置属性
     */
    @Autowired
    private MonitoringProperties monitoringProperties;

    /**
     * 延迟/周期执行线程池
     */
    private final ScheduledExecutorService seService = Executors.newScheduledThreadPool(5, new ThreadFactory() {
        AtomicInteger atomic = new AtomicInteger();

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "monitoring-heartbeat-pool-thread-" + this.atomic.getAndIncrement());
        }
    });

    @Override
    public void run(String... args) {
        // 重新开启线程，让他单独去做我们想要做的操作，此时CommandLineRunner执行的操作和主线程是相互独立的，抛出异常并不会影响到主线程
        Thread thread = new Thread(() -> this.seService.scheduleAtFixedRate(new HeartbeatScheduledExecutor(), 30,
                this.monitoringProperties.getHeartbeatProperties().getRate(), TimeUnit.SECONDS));
        // 设置守护线程
        thread.setDaemon(true);
        // 开始执行分进程
        thread.start();
    }

    /**
     * <p>
     * 在spring容器销毁时关闭线程池
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/3/26 10:05
     */
    @Override
    public void destroy() {
        if (!this.seService.isShutdown()) {
            this.seService.shutdown();
            log.info("延迟/周期执行线程池“monitoring-heartbeat-pool-thread”已经关闭！");
        }
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
