package com.gitee.pifeng.monitoring.plug.constant;

import com.gitee.pifeng.monitoring.plug.core.ConfigLoader;

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
    private static final String ROOT_URI = ConfigLoader.getMonitoringProperties().getComm().getHttp().getUrl();

    /**
     * 心跳地址
     */
    public static final String HEARTBEAT_URL = ROOT_URI + "/heartbeat/accept-heartbeat-package";

    /**
     * 下线地址
     */
    public static final String OFFLINE_URL = ROOT_URI + "/offline/accept-offline-package";

    /**
     * 告警地址
     */
    public static final String ALARM_URL = ROOT_URI + "/alarm/accept-alarm-package";

    /**
     * 发送服务器信息地址
     */
    public static final String SERVER_URL = ROOT_URI + "/server/accept-server-package";

    /**
     * 发送Java虚拟机信息地址
     */
    public static final String JVM_URL = ROOT_URI + "/jvm/accept-jvm-package";

}
