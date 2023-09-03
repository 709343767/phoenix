package com.gitee.pifeng.monitoring.server.inf;

import com.gitee.pifeng.monitoring.common.dto.OfflinePackage;
import com.gitee.pifeng.monitoring.server.business.server.component.OfflineAspect;
import org.aspectj.lang.JoinPoint;

/**
 * <p>
 * 下线监听器。
 * </p>
 * 一个被spring容器管理的类只要实现此监听器接口，当收到下线信息包时，就会自动调用监听器中相应的方法。
 *
 * @author 皮锋
 * @custom.date 2023/6/1 8:47
 */
@FunctionalInterface
public interface IOfflineListener {

    /**
     * <p>
     * 收到下线信息包时，唤醒执行监控回调方法，通知下线。
     * </p>
     * 此方法在{@link OfflineAspect#beforeWakeUp(JoinPoint)}中被注册监听。
     *
     * @param offlinePackage 下线信息包
     * @author 皮锋
     * @custom.date 2023/6/1 8:50
     */
    void notifyOffline(OfflinePackage offlinePackage);

}
