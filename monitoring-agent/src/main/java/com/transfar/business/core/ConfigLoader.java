package com.transfar.business.core;

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
		return PropertiesUtils.readProperty(FILE_PATH, URL) + "/monitoring-server-web";
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
		return PropertiesUtils.readProperty(FILE_PATH, USERNAME);
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
		return PropertiesUtils.readProperty(FILE_PATH, PASSWORD);
	}

	/**
	 * <p>
	 * 当前应用实例名
	 * </p>
	 *
	 * @author 皮锋
	 * @custom.date 2020年3月4日 下午4:50:40
	 * @return 当前应用实例名
	 */
	@SneakyThrows
	public static String getInstanceName() {
		return PropertiesUtils.readProperty(FILE_PATH, INSTANCE_NAME);
	}

}
