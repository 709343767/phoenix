package com.gitee.pifeng.monitoring.server.business.server.component;

import com.gitee.pifeng.monitoring.common.dto.OfflinePackage;
import com.gitee.pifeng.monitoring.common.threadpool.ThreadPool;
import com.gitee.pifeng.monitoring.server.business.server.controller.OfflineController;
import com.gitee.pifeng.monitoring.server.inf.IOfflineListener;
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
 * 下线信息包切面
 * </p>
 *
 * @author 皮锋
 * @custom.date 2023/6/1 8:42
 */
@Slf4j
@Aspect
@Component
public class OfflineAspect {

    /**
     * 下线监听器
     */
    @Autowired
    private List<IOfflineListener> offlineListeners;

    /**
     * <p>
     * 定义切入点，切入点为{@link OfflineController#acceptOfflinePackage(OfflinePackage)}这一个方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2023/6/1 19:56
     */
    @Pointcut("execution(public * com.gitee.pifeng.monitoring.server.business.server.controller.OfflineController.acceptOfflinePackage(..))")
    public void tangentPoint() {
    }

    /**
     * <p>
     * 通过前置通知，调用监听器回调接口。
     * </p>
     *
     * @param joinPoint 提供对连接点上可用状态和有关状态的静态信息的反射访问。
     * @author 皮锋
     * @custom.date 2023年6月1日 下午20:34:03
     */
    @Before("tangentPoint()")
    public void beforeWakeUp(JoinPoint joinPoint) {
        OfflinePackage offlinePackage = (OfflinePackage) joinPoint.getArgs()[0];
        // 调用监听器回调接口
        this.offlineListeners.forEach(o -> ThreadPool.COMMON_IO_INTENSIVE_THREAD_POOL.execute(() -> {
            try {
                o.notifyOffline(offlinePackage);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }));
    }

}
