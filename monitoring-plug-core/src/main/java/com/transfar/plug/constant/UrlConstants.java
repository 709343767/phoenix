package com.transfar.plug.constant;

import com.transfar.plug.core.ConfigLoader;

/**
 * <p>
 * URL地址
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月6日 下午2:23:41
 */
public final class UrlConstants {

    /**
     * 服务根路径
     */
    private static final String ROOT_URI = ConfigLoader.monitoringProperties.getServerProperties().getUrl();

    /**
     * 心跳地址
     */
    public static final String HEARTBEAT_URL = ROOT_URI + "/heartbeat/accept-heartbeat-package";

    /**
     * 告警地址
     */
    public static final String ALARM_URL = ROOT_URI + "/alarm/accept-alarm-package";

    /**
     * 发送服务器信息地址
     */
    public static final String SERVER_URL = ROOT_URI + "/server/accept-server-package";

}
