package com.gitee.pifeng.monitoring.ui.constant;

import com.gitee.pifeng.monitoring.plug.core.ConfigLoader;

/**
 * <p>
 * URL地址
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/4/5 12:28
 */
public class UrlConstants {

    /**
     * <p>
     * 私有化构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2021/4/5 12:29
     */
    private UrlConstants() {
    }

    /**
     * 服务根路径
     */
    private static final String ROOT_URI = ConfigLoader.MONITORING_PROPERTIES.getServerProperties().getUrl();

    /**
     * 刷新服务端监控属性配置URL地址
     */
    public static final String MONITORING_PROPERTIES_CONFIG_REFRESH_URL = ROOT_URI + "/monitoring-properties-config/refresh";

}
