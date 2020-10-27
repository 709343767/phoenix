package com.imby.server.inf;

import com.imby.common.constant.AlarmTypeEnums;
import com.imby.common.exception.NetException;
import com.imby.server.business.server.aop.ServerAspect;
import com.imby.server.business.web.service.impl.MonitorServerServiceImpl;
import org.aspectj.lang.JoinPoint;
import org.hyperic.sigar.SigarException;

import java.util.List;

/**
 * <p>
 * 服务器信息监听器。
 * </p>
 * 一个被spring容器管理的类只要实现此监听器接口，当服务器信息发生改变时，就会自动调用监听器中相应的方法。
 *
 * @author 皮锋
 * @custom.date 2020/3/30 20:17
 */
public interface IServerMonitoringListener {

    /**
     * <p>
     * 收到服务器信息包时，唤醒执行监控回调方法。
     * </p>
     * 此方法在{@link ServerAspect#refreshAndWakeUp(JoinPoint)}中被调用。
     *
     * @param param 回调参数
     * @throws NetException   获取网络信息异常
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020/3/30 20:18
     */
    default void wakeUpMonitor(Object... param) throws NetException, SigarException {
    }

    /**
     * <p>
     * 删除数据库中的服务器信息时，唤醒执行监控信息池回调方法。
     * </p>
     * 此方法在{@link MonitorServerServiceImpl#deleteMonitorServer(List)}中被调用。
     *
     * @param alarmTypeEnums 告警类型
     * @param params         回调参数
     * @author 皮锋
     * @custom.date 2020/3/30 20:18
     */
    default void wakeUpMonitorPool(AlarmTypeEnums alarmTypeEnums, List<String> params) {
    }

}
