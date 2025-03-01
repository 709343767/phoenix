package com.gitee.pifeng.monitoring.server.inf;

import com.gitee.pifeng.monitoring.server.business.server.component.HeartbeatAspect;
import org.aspectj.lang.JoinPoint;

/**
 * <p>
 * 应用实例信息监听器。
 * </p>
 * 一个被spring容器管理的类只要实现此监听器接口，当应用实例信息发生改变时，就会自动调用监听器中相应的方法。
 *
 * @author 皮锋
 * @custom.date 2025-2-18 10:16
 */
public interface IInstanceMonitoringListener {

    /**
     * <p>
     * 收到心跳信息包时，唤醒执行监控回调方法。
     * </p>
     * 此方法在{@link HeartbeatAspect#afterWakeUp(JoinPoint)}中被注册监听。
     *
     * @param param 回调参数
     * @author 皮锋
     * @custom.date 2025/2/18 11:15
     */
    void wakeUpMonitor(Object... param);

}