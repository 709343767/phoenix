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
    private static final String ROOT_URI = ConfigLoader.getMonitoringProperties().getComm().getHttp().getUrl();

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
     * 发送离线包地址
     */
    public static final String OFFLINE_URL = ROOT_URI + "/offline/accept-offline-package";

    /**
     * 刷新服务端监控属性配置URL地址
     */
    public static final String MONITORING_PROPERTIES_CONFIG_REFRESH_URL = ROOT_URI + "/monitoring-properties-config/refresh";

    /**
     * 获取被监控网络源IP地址
     */
    public static final String GET_SOURCE_IP_URL = ROOT_URI + "/network/get-source-ip";

    /**
     * 测试网络连通性URL地址
     */
    public static final String TEST_MONITOR_NETWORK_URL = ROOT_URI + "/network/test-monitor-network";

    /**
     * 测试HTTP连通性URL地址
     */
    public static final String TEST_MONITOR_HTTP_URL = ROOT_URI + "/http/test-monitor-http";

    /**
     * 测试TCP连通性URL地址
     */
    public static final String TEST_MONITOR_TCP_URL = ROOT_URI + "/tcp/test-monitor-tcp";

    /**
     * 测试数据库连通性URL地址
     */
    public static final String TEST_MONITOR_DB_URL = ROOT_URI + "/db/test-monitor-db";

    /**
     * MySQL数据库：获取会话列表
     */
    public static final String MYSQL_GET_SESSION_LIST_URL = ROOT_URI + "/db-session4mysql/get-session-list";

    /**
     * MySQL数据库：结束会话
     */
    public static final String MYSQL_DESTROY_SESSION_URL = ROOT_URI + "/db-session4mysql/destroy-session";

    /**
     * Oracle数据库：获取会话列表
     */
    public static final String ORACLE_GET_SESSION_LIST_URL = ROOT_URI + "/db-session4oracle/get-session-list";

    /**
     * Oracle数据库：结束会话
     */
    public static final String ORACLE_DESTROY_SESSION_URL = ROOT_URI + "/db-session4oracle/destroy-session";

    /**
     * Oracle数据库：获取表空间列表(按文件)
     */
    public static final String ORACLE_GET_TABLESPACE_LIST_FILE_URL = ROOT_URI + "/db-tablespace4oracle/get-tablespace-list-file";

    /**
     * Oracle数据库：获取表空间列表
     */
    public static final String ORACLE_GET_TABLESPACE_LIST_ALL_URL = ROOT_URI + "/db-tablespace4oracle/get-tablespace-list-all";

    /**
     * Redis数据库：获取Redis信息
     */
    public static final String REDIS_GET_REDIS_INFO_URL = ROOT_URI + "/db-info4redis/get-redis-info";

    /**
     * Mongo数据库：获取Mongo信息列表
     */
    public static final String MONGO_GET_MONGO_INFO_LIST_URL = ROOT_URI + "/db-info4mongo/get-mongo-info-list";

}
