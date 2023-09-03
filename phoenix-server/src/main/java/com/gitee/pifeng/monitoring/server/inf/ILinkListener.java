package com.gitee.pifeng.monitoring.server.inf;

import com.gitee.pifeng.monitoring.server.business.server.component.HeartbeatAspect;
import com.gitee.pifeng.monitoring.server.business.server.component.ServerAspect;
import org.aspectj.lang.JoinPoint;

/**
 * <p>
 * 链路信息监听器。
 * </p>
 * 一个被spring容器管理的类只要实现此监听器接口，当链路信息发生改变时，就会自动调用监听器中相应的方法。
 *
 * @author 皮锋
 * @custom.date 2022/12/18 19:10
 */
@FunctionalInterface
public interface ILinkListener {

    /**
     * <p>
     * 唤醒执行监控回调方法。
     * </p>
     * 此方法在{@link HeartbeatAspect#beforeWakeUp(JoinPoint)},{@link ServerAspect#beforeWakeUp(JoinPoint)}中被注册监听。
     *
     * @param param 回调参数
     * @author 皮锋
     * @custom.date 2020/3/30 20:18
     */
    void wakeUpMonitor(Object... param);

}
