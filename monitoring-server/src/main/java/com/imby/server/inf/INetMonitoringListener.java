package com.imby.server.inf;

import com.imby.common.constant.AlarmTypeEnums;
import com.imby.server.business.web.service.impl.MonitorNetServiceImpl;

import java.util.List;

/**
 * <p>
 * 网络信息监听器。
 * </p>
 * 一个被spring容器管理的类只要实现此监听器接口，当网络信息发生改变时，就会自动调用监听器中相应的方法。
 *
 * @author 皮锋
 * @custom.date 2020/6/29 14:20
 */
public interface INetMonitoringListener {

    /**
     * <p>
     * 删除数据库中的网络信息时，唤醒执行监控信息池回调方法。
     * </p>
     * 此方法在{@link MonitorNetServiceImpl#deleteMonitorNet(List)}中被调用。
     *
     * @param alarmTypeEnums 告警类型
     * @param params         回调参数
     * @author 皮锋
     * @custom.date 2020/3/30 20:18
     */
    default void wakeUpMonitorPool(AlarmTypeEnums alarmTypeEnums, List<String> params) {
    }

}
