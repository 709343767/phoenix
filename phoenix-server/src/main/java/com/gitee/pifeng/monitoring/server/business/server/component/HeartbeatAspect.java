package com.gitee.pifeng.monitoring.server.business.server.component;

import com.gitee.pifeng.monitoring.common.dto.HeartbeatPackage;
import com.gitee.pifeng.monitoring.common.threadpool.ThreadPool;
import com.gitee.pifeng.monitoring.server.business.server.controller.HeartbeatController;
import com.gitee.pifeng.monitoring.server.inf.IInstanceMonitoringListener;
import com.gitee.pifeng.monitoring.server.inf.ILinkListener;
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
 * 心跳包切面
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/12/18 19:05
 */
@Slf4j
@Aspect
@Component
public class HeartbeatAspect {

    /**
     * 链路信息监听器
     */
    @Autowired(required = false)
    private List<ILinkListener> linkListeners;

    /**
     * 应用实例信息监听器
     */
    @Autowired(required = false)
    private List<IInstanceMonitoringListener> instanceMonitoringListeners;

    /**
     * <p>
     * 定义切入点，切入点为{@link HeartbeatController#acceptHeartbeatPackage(HeartbeatPackage)}这一个方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2022/12/18 19:56
     */
    @Pointcut("execution(public * com.gitee.pifeng.monitoring.server.business.server.controller.HeartbeatController.acceptHeartbeatPackage(..))")
    public void tangentPoint() {
    }

    /**
     * <p>
     * 通过前置通知，调用监听器回调接口。
     * </p>
     *
     * @param joinPoint 提供对连接点上可用状态和有关状态的静态信息的反射访问。
     * @author 皮锋
     * @custom.date 2020年4月1日 下午3:34:06
     */
    @Before("tangentPoint()")
    public void beforeWakeUp(JoinPoint joinPoint) {
        HeartbeatPackage heartbeatPackage = (HeartbeatPackage) joinPoint.getArgs()[0];
        // 调用监听器回调接口
        if (this.linkListeners != null) {
            this.linkListeners.forEach(o -> ThreadPool.getCommonIoIntensiveThreadPoolExecutor().execute(() -> {
                try {
                    o.wakeUpMonitor(heartbeatPackage);
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
     * @custom.date 2025年2月18日 上午10:57:08
     */
    @After("tangentPoint()")
    public void afterWakeUp(JoinPoint joinPoint) {
        HeartbeatPackage heartbeatPackage = (HeartbeatPackage) joinPoint.getArgs()[0];
        // 应用实例ID
        String instanceId = heartbeatPackage.getInstanceId();
        // 调用监听器回调接口
        if (this.instanceMonitoringListeners != null) {
            this.instanceMonitoringListeners.forEach(o -> ThreadPool.getCommonIoIntensiveThreadPoolExecutor().execute(() -> {
                try {
                    o.wakeUpMonitor(instanceId);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }));
        }
    }

}
