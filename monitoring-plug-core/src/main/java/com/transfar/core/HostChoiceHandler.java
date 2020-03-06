package com.transfar.core;

import org.apache.commons.lang3.StringUtils;

import com.transfar.common.Host;
import com.transfar.properties.MonitoringAgentProperties;
import com.transfar.properties.MonitoringServerProperties;

/**
 * <p>
 * 目标主机选择助手
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月5日 下午6:22:57
 */
public class HostChoiceHandler {

	/**
	 * 服务端配置
	 */
	private static final MonitoringServerProperties SERVER_PROPERTIES = ConfigLoader.monitoringProperties
			.getServerProperties();

	/**
	 * 代理端配置
	 */
	private static final MonitoringAgentProperties AGENT_PROPERTIES = ConfigLoader.monitoringProperties
			.getAgentProperties();

	/**
	 * 服务程序名字
	 */
	private static final String SERVER_NAME = "/monitoring-server-web";

	/**
	 * 代理程序名字
	 */
	private static final String AGENT_NAME = "/monitoring-agent";

	/**
	 * <p>
	 * 选择主机
	 * </p>
	 *
	 * @return Host
	 * @author 皮锋
	 * @custom.date 2020年3月5日 下午7:33:49
	 */
	public static Host choiceHost() {
		// 优先选择直连服务端，服务端配置为空，有代理端配置时选择代理端
		String serverUrl = SERVER_PROPERTIES.getUrl();
		String serverUserName = SERVER_PROPERTIES.getUsername();
		String serverPassword = SERVER_PROPERTIES.getPassword();
		String agentUrl = AGENT_PROPERTIES.getUrl();
		String agentUserName = AGENT_PROPERTIES.getUsername();
		String agentPassword = AGENT_PROPERTIES.getPassword();
		if (StringUtils.isNotBlank(serverUrl)) {
			// 主机为服务端
			return Host.builder().url(serverUrl + SERVER_NAME).username(serverUserName).password(serverPassword)
					.build();
		} else {
			// 主机为代理端
			return Host.builder().url(agentUrl + AGENT_NAME).username(agentUserName).password(agentPassword).build();
		}
	}

}