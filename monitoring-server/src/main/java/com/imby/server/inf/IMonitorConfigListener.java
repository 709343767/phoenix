package com.imby.server.inf;

import com.imby.server.business.web.service.impl.MonitorConfigServiceImpl;
import com.imby.server.business.web.vo.MonitorConfigPageFormVo;

/**
 * <p>
 * 监控配置监听器。
 * </p>
 * 一个被spring容器管理的类只要实现此监听器接口，当监控配置发生改变时，就会自动调用监听器中相应的方法。
 *
 * @author 皮锋
 * @custom.date 2020/11/9 20:51
 */
public interface IMonitorConfigListener {

    /**
     * <p>
     * 更新数据库中的监控配置信息时，唤醒执行监控配置属性加载器回调方法。
     * </p>
     * 此方法在{@link MonitorConfigServiceImpl#update(MonitorConfigPageFormVo)}中被调用。
     *
     * @author 皮锋
     * @custom.date 2020/3/30 20:18
     */
    default void wakeUpMonitoringConfigPropertiesLoader() {
    }

}
