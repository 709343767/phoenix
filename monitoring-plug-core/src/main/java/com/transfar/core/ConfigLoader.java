package com.transfar.core;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.transfar.exception.NotFoundConfigFileException;
import com.transfar.exception.NotFoundConfigParamException;
import com.transfar.properties.MonitoringHeartbeatProperties;
import com.transfar.properties.MonitoringOwnProperties;
import com.transfar.properties.MonitoringProperties;
import com.transfar.properties.MonitoringServerInfoProperties;
import com.transfar.properties.MonitoringServerProperties;
import com.transfar.util.PropertiesUtils;

/**
 * <p>
 * 加载监控配置文件信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月5日 下午3:06:21
 */
public class ConfigLoader {

	/**
	 * 监控属性
	 */
	public static MonitoringProperties monitoringProperties = new MonitoringProperties();

	/**
	 * <p>
	 * 加载配置信息
	 * </p>
	 *
	 * @param configPath 配置文件路径
	 * @param configName 配置文件名称
	 * @throws NotFoundConfigParamException 找不到配置信息异常
	 * @throws NotFoundConfigFileException  找不到配置文件异常
	 * @author 皮锋
	 * @custom.date 2020年3月5日 下午3:36:32
	 */
	public static void load(String configPath, String configName)
			throws NotFoundConfigParamException, NotFoundConfigFileException {
		// 如果没有填写配置文件路径，默认在根路径
		if (StringUtils.isBlank(configPath)) {
			configPath = "";
		}
		// 如果没有写配置文件名字，默认为：monitoring.properties
		if (StringUtils.isBlank(configName)) {
			configName = "monitoring.properties";
		}
		Properties properties;
		try {
			properties = PropertiesUtils.loadProperties(configPath + configName);
		} catch (IOException e) {
			throw new NotFoundConfigFileException("监控程序找不到配置文件！");
		}
		// 解析配置文件
		analysis(properties);
	}

	/**
	 * <p>
	 * 解析配置信息
	 * </p>
	 *
	 * @param properties 配置信息
	 * @throws NotFoundConfigParamException 找不到配置信息异常
	 * @author 皮锋
	 * @custom.date 2020年3月5日 下午3:51:47
	 */
	private static void analysis(Properties properties) throws NotFoundConfigParamException {
		// 监控服务端url
		String serverUrl = StringUtils.trimToNull(properties.getProperty("monitoring.server.url"));
		// 监控服务端用户名
		String serverUserName = StringUtils.trimToNull(properties.getProperty("monitoring.server.username"));
		// 监控服务端密码
		String serverPassword = StringUtils.trimToNull(properties.getProperty("monitoring.server.password"));
		// 实例名称
		String instanceName = StringUtils.trimToNull(properties.getProperty("monitoring.own.instance.name"));
		// 缺省[与服务端或者代理端发心跳包的频率（秒），默认30秒]
		String heartbeatRateStr = StringUtils.trimToNull(properties.getProperty("monitoring.heartbeat.rate"));
		long heartbeatRate = StringUtils.isBlank(heartbeatRateStr) ? 30L : Long.parseLong(heartbeatRateStr);
		// 缺省[与服务端或者代理端发服务器信息包的频率（秒），默认60秒]
		String serverInfoRateStr = StringUtils.trimToNull(properties.getProperty("monitoring.server-info.rate"));
		long serverInfoRate = StringUtils.isBlank(serverInfoRateStr) ? 60L : Long.parseLong(serverInfoRateStr);
		// 没有配置连接
		if (StringUtils.isBlank(serverUrl)) {
			throw new NotFoundConfigParamException("监控程序找不到任何监控服务端配置！");
		}
		// 用户名密码暂不做处理
		// 没有实例名称
		if (StringUtils.isBlank(instanceName)) {
			throw new NotFoundConfigParamException("监控程序找不到实例名称配置！");
		}
		// 封装数据
		wrap(serverUrl, serverUserName, serverPassword, instanceName, heartbeatRate, serverInfoRate);
	}

	/**
	 * <p>
	 * 封装配置信息
	 * </p>
	 *
	 * @param serverUrl      监控服务端url
	 * @param serverUserName 监控服务端用户名
	 * @param serverPassword 监控服务端密码
	 * @param instanceName   实例名称
	 * @param heartbeatRate  缺省[与服务端或者代理端发心跳包的频率（秒），默认30秒]
	 * @param serverInfoRate 缺省[与服务端或者代理端发服务器信息包的频率（秒），默认60秒]
	 * @author 皮锋
	 * @custom.date 2020年3月5日 下午4:36:33
	 */
	private static void wrap(String serverUrl, String serverUserName, String serverPassword, String instanceName,
			long heartbeatRate, long serverInfoRate) {
		MonitoringServerProperties serverProperties = new MonitoringServerProperties();
		serverProperties.setUrl(serverUrl);
		serverProperties.setUsername(serverUserName);
		serverProperties.setPassword(serverPassword);
		monitoringProperties.setServerProperties(serverProperties);
		MonitoringOwnProperties ownProperties = new MonitoringOwnProperties();
		ownProperties.setInstanceName(instanceName);
		monitoringProperties.setOwnProperties(ownProperties);
		MonitoringHeartbeatProperties heartbeatProperties = new MonitoringHeartbeatProperties();
		heartbeatProperties.setRate(heartbeatRate);
		monitoringProperties.setHeartbeatProperties(heartbeatProperties);
		MonitoringServerInfoProperties monitoringServerInfoProperties = new MonitoringServerInfoProperties();
		monitoringServerInfoProperties.setRate(serverInfoRate);
		monitoringProperties.setMonitoringServerInfoProperties(monitoringServerInfoProperties);
	}

}
