package com.transfar.business.core;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.hyperic.sigar.SigarException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.transfar.dto.ServerPackage;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 在容器启动的时候定时向服务端发服务器信息包
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月8日 下午2:45:00
 */
@Component
public class ServerInfoCommandLineRunner implements CommandLineRunner {

    @Override
    public void run(String... args) {
        // 重新开启线程，让他单独去做我们想要做的操作，此时CommandLineRunner执行的操作和主线程是相互独立的，抛出异常并不会影响到主线程
        Thread thread = new Thread(() -> {
            final ScheduledExecutorService seService = Executors.newScheduledThreadPool(5, new ThreadFactory() {
                AtomicInteger atomic = new AtomicInteger();

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "monitoring-server-pool-thread-" + this.atomic.getAndIncrement());
                }
            });
            seService.scheduleAtFixedRate(new ServerInfoScheduledExecutor(), 30,
                    ConfigLoader.monitoringProperties.getMonitoringServerInfoProperties().getRate(), TimeUnit.SECONDS);
        });
        // 设置守护线程
        thread.setDaemon(true);
        // 开始执行分进程
        thread.start();
    }

}

/**
 * <p>
 * 服务器信息包调度程序执行器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月8日 下午2:48:10
 */
@Slf4j
class ServerInfoScheduledExecutor implements Runnable {

    @Override
    public void run() {
        try {
            ServerPackage serverPackage = new PackageConstructor().structureServerPackage();
            // 向服务端发送心跳包
            MethodExecuteHandler.sendServerPackage2Server(serverPackage);
        } catch (SigarException e) {
            log.error("Sigar异常！", e);
        }
    }

}
