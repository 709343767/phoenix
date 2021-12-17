package com.gitee.pifeng.monitoring.agent.constant;

import com.gitee.pifeng.monitoring.plug.core.ConfigLoader;

/**
 * <p>
 * URL
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月6日 下午3:34:20
 */
public final class UrlConstants {

    /**
     * <p>
     * 私有化构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/10/27 13:26
     */
    private UrlConstants() {
    }

    /**
     * 服务根路径
     */
    private static final String ROOT_URI = ConfigLoader.MONITORING_PROPERTIES.getServerProperties().getUrl();

    /**
     * 发送心跳包URL地址
     */
    public static final String HEARTBEAT_URL = ROOT_URI + "/heartbeat/accept-heartbeat-package";

    /**
     * 发送告警包URL地址
     */
    public static final String ALARM_URL = ROOT_URI + "/alarm/accept-alarm-package";

    /**
     * 发送服务器信息包URL地址
     */
    public static final String SERVER_URL = ROOT_URI + "/server/accept-server-package";

    /**
     * 发送Java虚拟机信息包URL地址
     */
    public static final String JVM_URL = ROOT_URI + "/jvm/accept-jvm-package";

    /**
     * 刷新服务端监控属性配置URL地址
     */
    public static final String MONITORING_PROPERTIES_CONFIG_REFRESH_URL = ROOT_URI + "/monitoring-properties-config/refresh";

}
