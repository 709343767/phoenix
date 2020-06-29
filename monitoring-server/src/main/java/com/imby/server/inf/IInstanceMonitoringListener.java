package com.imby.server.inf;

/**
 * <p>
 * 应用实例监听器
 * </p>
 * 实现监听器接口，当应用实例发生改变时，自动调用监听器中的方法
 *
 * @author 皮锋
 * @custom.date 2020/6/29 14:21
 */
public interface IInstanceMonitoringListener {

    /**
     * <p>
     * 唤醒执行回调方法
     * </p>
     *
     * @param param 回调参数
     * @author 皮锋
     * @custom.date 2020/3/30 20:18
     */
    void wakeUp(Object... param);

}
