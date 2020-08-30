package com.imby.server.inf;

import com.imby.common.exception.NetException;
import org.hyperic.sigar.SigarException;

/**
 * <p>
 * 服务器信息监听器
 * </p>
 * 实现监听器接口，当服务器信息发生改变时，自动调用监听器中的方法
 *
 * @author 皮锋
 * @custom.date 2020/3/30 20:17
 */
public interface IServerMonitoringListener {

    /**
     * <p>
     * 唤醒执行回调方法
     * </p>
     *
     * @param param 回调参数
     * @throws NetException   获取网络信息异常
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020/3/30 20:18
     */
    void wakeUp(Object... param) throws NetException, SigarException;

}
