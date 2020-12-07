package com.gitee.pifeng.server.inf;

import com.gitee.pifeng.common.constant.MonitorTypeEnums;
import com.gitee.pifeng.server.business.web.service.impl.MonitorInstanceServiceImpl;

import java.util.List;

/**
 * <p>
 * 应用实例监听器。
 * </p>
 * 一个被spring容器管理的类只要实现此监听器接口，当应用实例发生改变时，就会自动调用监听器中相应的方法。
 *
 * @author 皮锋
 * @custom.date 2020/6/29 14:21
 */
public interface IInstanceMonitoringListener {

    /**
     * <p>
     * 删除数据库中的应用实例时，唤醒执行监控信息池回调方法。
     * </p>
     * 此方法在{@link MonitorInstanceServiceImpl#deleteMonitorInstance(List)}中被调用。
     *
     * @param monitorTypeEnum 监控类型
     * @param params           回调参数
     * @author 皮锋
     * @custom.date 2020/3/30 20:18
     */
    default void wakeUpMonitorPool(MonitorTypeEnums monitorTypeEnum, List<String> params) {
    }

}
