package com.transfar.business.core;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.transfar.util.PropertiesUtils;

import lombok.SneakyThrows;

/**
 * <p>
 * 加载解析监控配置文件
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月4日 下午2:36:18
 */
public class ConfigLoader {

    /**
     * 文件路径
     */
    private static final String FILE_PATH = "monitoring.properties";
    /**
     * URL
     */
    private static final String URL = "monitoring.server.url";
    /**
     * 用户名
     */
    private static final String USERNAME = "monitoring.server.username";
    /**
     * 密码
     */
    private static final String PASSWORD = "monitoring.server.password";

    /**
     * 当前应用实例名
     */
    private static final String INSTANCE_NAME = "monitoring.own.instance.name";

    /**
     * 与服务端发心跳包的频率（秒）
     */
    private static final String HEARTBEAT_RATE = "monitoring.heartbeat.rate";

    /**
     * monitoring.properties配置文件
     */
    private static final Properties PROPERTIES = PropertiesUtils.loadProperties(FILE_PATH);

    /**
     * <p>
     * 获取连接服务端的URL
     * </p>
     *
     * @return URL
     * @author 皮锋
     * @custom.date 2020年3月4日 下午3:17:39
     */
    @SneakyThrows
    public static String getServerUrl() {
        return StringUtils.trim(PROPERTIES.getProperty(URL)) + "/monitoring-server-web";
    }

    /**
     * <p>
     * 获取连接服务端的用户名
     * </p>
     *
     * @return 用户名
     * @author 皮锋
     * @custom.date 2020年3月4日 下午3:18:24
     */
    @SneakyThrows
    public static String getServerUsername() {
        return StringUtils.trim(PROPERTIES.getProperty(USERNAME));
    }

    /**
     * <p>
     * 获取连接服务端的密码
     * </p>
     *
     * @return 密码
     * @author 皮锋
     * @custom.date 2020年3月4日 下午3:18:30
     */
    @SneakyThrows
    public static String getServerPassword() {
        return StringUtils.trim(PROPERTIES.getProperty(PASSWORD));
    }

    /**
     * <p>
     * 当前应用实例名
     * </p>
     *
     * @return 当前应用实例名
     * @author 皮锋
     * @custom.date 2020年3月4日 下午4:50:40
     */
    @SneakyThrows
    public static String getInstanceName() {
        return StringUtils.trim(PROPERTIES.getProperty(INSTANCE_NAME));
    }

    /**
     * <p>
     * 获取与服务端发心跳包的频率（秒）
     * </p>
     *
     * @return 心跳频率
     * @author 皮锋
     * @custom.date 2020年3月5日 上午10:50:11
     */
    @SneakyThrows
    public static long getHeartbeatRate() {
        String rate = StringUtils.trim(PROPERTIES.getProperty(HEARTBEAT_RATE));
        if (StringUtils.isBlank(rate)) {
            // 默认心跳频率是60秒
            return 60L;
        } else {
            return Long.parseLong(rate);
        }
    }

}
