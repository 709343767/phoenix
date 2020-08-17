package com.imby.agent.business.scheduler;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.imby.agent.business.core.MethodExecuteHandler;
import com.imby.agent.business.core.PackageConstructor;
import com.imby.common.dto.BaseResponsePackage;
import com.imby.common.dto.ServerPackage;
import com.imby.common.property.MonitoringProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.hyperic.sigar.SigarException;
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
 * 在容器启动的时候定时向服务端发服务器信息包
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月8日 下午2:45:00
 */
@Slf4j
@Component
@Order(2)
public class ServerInfoCommandLineRunner implements CommandLineRunner, DisposableBean {

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
                    .namingPattern("monitoring-server-pool-thread-%d")
                    // 设置为守护线程
                    .daemon(true)
                    .build());

    /**
     * <p>
     * 如果监控配置文件中配置了向服务端发送服务器信息，则项目启动完成后延迟10秒钟启动定时任务，定时向服务端发送服务器信息包，
     * 定时任务的执行频率为监控配置文件中配置的服务器信息包发送频率。
     * </p>
     *
     * @param args 传入的主方法参数
     * @author 皮锋
     * @custom.date 2020/4/9 17:17
     */
    @Override
    public void run(String... args) {
        // 是否发送服务器信息
        boolean serverInfoEnable = this.monitoringProperties.getServerInfoProperties().isEnable();
        if (serverInfoEnable) {
            this.seService.scheduleAtFixedRate(new ServerInfoScheduledExecutor(), 10,
                    this.monitoringProperties.getServerInfoProperties().getRate(), TimeUnit.SECONDS);
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

    /**
     * <p>
     * 发送服务器信息包
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/4/11 20:51
     */
    @Override
    public void run() {
        try {
            ServerPackage serverPackage = new PackageConstructor().structureServerPackage();
            // 开始时间
            Date beginDate = new Date();
            // 向服务端发送服务器信息包
            BaseResponsePackage baseResponsePackage = MethodExecuteHandler.sendServerPackage2Server(serverPackage);
            // 结束时间
            Date endDate = new Date();
            // 时间差（毫秒）
            long betweenDay = DateUtil.between(beginDate, endDate, DateUnit.MS);
            log.debug("服务器信息包响应消息：{}", baseResponsePackage.toJsonString());
            log.debug("发送服务器信息包耗时：{} {}", betweenDay, "ms");
        } catch (SigarException e) {
            log.error("Sigar异常！", e);
        }
    }

}
