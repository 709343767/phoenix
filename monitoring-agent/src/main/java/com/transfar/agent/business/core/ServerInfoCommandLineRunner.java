package com.transfar.agent.business.core;

import com.transfar.common.dto.ServerPackage;
import com.transfar.common.property.MonitoringProperties;
import lombok.extern.slf4j.Slf4j;
import org.hyperic.sigar.SigarException;
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
 * 在容器启动的时候定时向服务端发服务器信息包
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月8日 下午2:45:00
 */
@Slf4j
@Component
public class ServerInfoCommandLineRunner implements CommandLineRunner, DisposableBean {

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
            return new Thread(r, "monitoring-server-pool-thread-" + this.atomic.getAndIncrement());
        }
    });

    @Override
    public void run(String... args) {
        // 是否发送服务器信息
        boolean serverInfoEnable = this.monitoringProperties.getMonitoringServerInfoProperties().isEnable();
        if (serverInfoEnable) {
            // 重新开启线程，让他单独去做我们想要做的操作，此时CommandLineRunner执行的操作和主线程是相互独立的，抛出异常并不会影响到主线程
            Thread thread = new Thread(() -> this.seService.scheduleAtFixedRate(new ServerInfoScheduledExecutor(), 30,
                    this.monitoringProperties.getMonitoringServerInfoProperties().getRate(), TimeUnit.SECONDS));
            // 设置守护线程
            thread.setDaemon(true);
            // 开始执行分进程
            thread.start();
        }
    }

    /**
     * <p>
     * 在spring容器销毁时关闭线程池
     * </p>
     * 关闭线程池：monitoring-server-pool-thread
     *
     * @author 皮锋
     * @custom.date 2020/3/26 10:08
     */
    @Override
    public void destroy() {
        if (!this.seService.isShutdown()) {
            this.seService.shutdown();
            log.info("延迟/周期执行线程池“monitoring-server-pool-thread”已经关闭！");
        }
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
