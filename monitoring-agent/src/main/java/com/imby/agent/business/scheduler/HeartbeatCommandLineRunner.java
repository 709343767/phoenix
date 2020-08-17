package com.imby.agent.business.scheduler;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.imby.agent.business.core.MethodExecuteHandler;
import com.imby.agent.business.core.PackageConstructor;
import com.imby.common.dto.BaseResponsePackage;
import com.imby.common.dto.HeartbeatPackage;
import com.imby.common.property.MonitoringProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
@Order(1)
public class HeartbeatCommandLineRunner implements CommandLineRunner, DisposableBean {

    /**
     * 监控配置属性
     */
    @Autowired
    private MonitoringProperties monitoringProperties;

    /**
     * 延迟/周期执行线程池
     */
    private final ScheduledExecutorService seService = new ScheduledThreadPoolExecutor(5,
            new BasicThreadFactory.Builder()
                    // 设置线程名
                    .namingPattern("monitoring-heartbeat-pool-thread-%d")
                    // 设置为守护线程
                    .daemon(true)
                    .build());

    /**
     * <p>
     * 项目启动完成后延迟5秒钟启动定时任务，定时向服务端发送心跳包，定时任务的执行频率为监控配置文件中所配置的心跳频率。
     * </p>
     *
     * @param args 传入的主方法参数
     * @author 皮锋
     * @custom.date 2020/4/9 17:12
     */
    @Override
    public void run(String... args) {
        this.seService.scheduleAtFixedRate(new HeartbeatScheduledExecutor(), 5,
                this.monitoringProperties.getHeartbeatProperties().getRate(), TimeUnit.SECONDS);
    }

    /**
     * <p>
     * 在spring容器销毁时关闭线程池
     * </p>
     * 关闭线程池：monitoring-heartbeat-pool-thread
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
@Slf4j
class HeartbeatScheduledExecutor implements Runnable {

    /**
     * <p>
     * 发送心跳包
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/4/11 20:47
     */
    @Override
    public void run() {
        HeartbeatPackage heartbeatPackage = new PackageConstructor().structureHeartbeatPackage();
        // 开始时间
        Date beginDate = new Date();
        // 向服务端发送心跳包
        BaseResponsePackage baseResponsePackage = MethodExecuteHandler.sendHeartbeatPackage2Server(heartbeatPackage);
        // 结束时间
        Date endDate = new Date();
        // 时间差（毫秒）
        long betweenDay = DateUtil.between(beginDate, endDate, DateUnit.MS);
        log.debug("心跳包响应消息：{}", baseResponsePackage.toJsonString());
        log.debug("发送心跳包耗时：{} {}", betweenDay, "ms");
    }
}
