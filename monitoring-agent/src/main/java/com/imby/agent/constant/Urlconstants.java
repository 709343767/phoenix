package com.imby.agent.constant;

/**
 * <p>
 * URL
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月6日 下午3:34:20
 */
public final class Urlconstants {

    /**
     * 发送心跳包URL地址
     */
    public static final String HEARTBEAT_URL = "/heartbeat/accept-heartbeat-package";

    /**
     * 发送告警包URL地址
     */
    public static final String ALARM_URL = "/alarm/accept-alarm-package";

    /**
     * 发送服务器信息包URL地址
     */
    public static final String SERVER_URL = "/server/accept-server-package";

    /**
     * 发送Java虚拟机信息包URL地址
     */
    public static final String JVM_URL = "/jvm/accept-jvm-package";


}
