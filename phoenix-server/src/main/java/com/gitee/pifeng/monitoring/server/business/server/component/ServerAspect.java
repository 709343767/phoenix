package com.gitee.pifeng.monitoring.server.business.server.component;

import com.gitee.pifeng.monitoring.common.dto.ServerPackage;
import com.gitee.pifeng.monitoring.common.threadpool.ThreadPool;
import com.gitee.pifeng.monitoring.server.business.server.controller.ServerController;
import com.gitee.pifeng.monitoring.server.inf.ILinkListener;
import com.gitee.pifeng.monitoring.server.inf.IServerMonitoringListener;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 服务器信息包切面
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年4月1日 下午3:21:19
 */
@Slf4j
@Aspect
@Component
public class ServerAspect {

    /**
     * 服务器信息监听器
     */
    @Autowired(required = false)
    private List<IServerMonitoringListener> serverMonitoringListeners;

    /**
     * 链路信息监听器
     */
    @Autowired(required = false)
    private List<ILinkListener> linkListeners;

    /**
     * <p>
     * 定义切入点，切入点为{@link ServerController#acceptServerPackage(ServerPackage)}这一个方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/2/22 17:56
     */
    @Pointcut("execution(public * com.gitee.pifeng.monitoring.server.business.server.controller.ServerController.acceptServerPackage(..))")
    public void tangentPoint() {
    }

    /**
     * <p>
     * 通过前置通知，调用监听器回调接口。
     * </p>
     *
     * @param joinPoint 提供对连接点上可用状态和有关状态的静态信息的反射访问。
     * @author 皮锋
     * @custom.date 2022/12/22 10:01
     */
    @Before("tangentPoint()")
    public void beforeWakeUp(JoinPoint joinPoint) {
        ServerPackage serverPackage = (ServerPackage) joinPoint.getArgs()[0];
        // 调用监听器回调接口
        if (this.linkListeners != null) {
            this.linkListeners.forEach(o -> ThreadPool.getCommonIoIntensiveThreadPoolExecutor().execute(() -> {
                try {
                    o.wakeUpMonitor(serverPackage);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }));
        }
    }

    /**
     * <p>
     * 通过后置通知，调用监听器回调接口。
     * </p>
     *
     * @param joinPoint 提供对连接点上可用状态和有关状态的静态信息的反射访问。
     * @author 皮锋
     * @custom.date 2020年4月1日 下午3:34:06
     */
    @After("tangentPoint()")
    public void afterWakeUp(JoinPoint joinPoint) {
        ServerPackage serverPackage = (ServerPackage) joinPoint.getArgs()[0];
        // IP地址
        String ip = serverPackage.getIp();
        // 调用监听器回调接口
        if (this.serverMonitoringListeners != null) {
            this.serverMonitoringListeners.forEach(o -> ThreadPool.getCommonIoIntensiveThreadPoolExecutor().execute(() -> {
                try {
                    o.wakeUpMonitor(ip);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }));
        }
    }

}
