package com.gitee.pifeng.monitoring.server.inf;

import com.gitee.pifeng.monitoring.server.business.server.component.ServerAspect;
import org.aspectj.lang.JoinPoint;

/**
 * <p>
 * 服务器信息监听器。
 * </p>
 * 一个被spring容器管理的类只要实现此监听器接口，当服务器信息发生改变时，就会自动调用监听器中相应的方法。
 *
 * @author 皮锋
 * @custom.date 2020/3/30 20:17
 */
@FunctionalInterface
public interface IServerMonitoringListener {

    /**
     * <p>
     * 收到服务器信息包时，唤醒执行监控回调方法。
     * </p>
     * 此方法在{@link ServerAspect#wakeUp(JoinPoint)}中被注册监听。
     *
     * @param param 回调参数
     * @author 皮锋
     * @custom.date 2020/3/30 20:18
     */
    void wakeUpMonitor(Object... param);

}
