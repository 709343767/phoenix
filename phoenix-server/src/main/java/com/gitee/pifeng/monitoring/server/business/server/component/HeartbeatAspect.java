package com.gitee.pifeng.monitoring.server.business.server.component;

import com.gitee.pifeng.monitoring.common.dto.HeartbeatPackage;
import com.gitee.pifeng.monitoring.common.threadpool.ThreadPool;
import com.gitee.pifeng.monitoring.server.business.server.controller.HeartbeatController;
import com.gitee.pifeng.monitoring.server.inf.ILinkListener;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
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
    @Autowired
    private List<ILinkListener> linkListeners;

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
        this.linkListeners.forEach(o -> ThreadPool.COMMON_IO_INTENSIVE_THREAD_POOL.execute(() -> {
            try {
                o.wakeUpMonitor(heartbeatPackage);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }));
    }

}
