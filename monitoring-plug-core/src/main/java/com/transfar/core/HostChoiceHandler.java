package com.transfar.core;

import org.apache.commons.lang3.StringUtils;

import com.transfar.exception.NotFoundHostException;
import com.transfar.properties.MonitoringAgentProperties;
import com.transfar.properties.MonitoringServerProperties;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

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
	 * <p>
	 * 选择主机
	 * </p>
	 *
	 * @author 皮锋
	 * @custom.date 2020年3月5日 下午7:33:49
	 * @return Host
	 * @throws NotFoundHostException 找不到主机异常
	 */
	public static Host choiceHost() throws NotFoundHostException {
		// 优先选择直连服务端，服务端配置为空，有代理端配置时选择代理端
		String serverUrl = SERVER_PROPERTIES.getUrl();
		String serverUserName = SERVER_PROPERTIES.getUsername();
		String serverPassword = SERVER_PROPERTIES.getPassword();
		String agentUrl = AGENT_PROPERTIES.getUrl();
		String agentUserName = AGENT_PROPERTIES.getUsername();
		String agentPassword = AGENT_PROPERTIES.getPassword();
		if (StringUtils.isNotBlank(serverUrl)) {
			return Host.builder().url(serverUrl + "/monitoring-server-web").username(serverUserName)
					.password(serverPassword).build();
		} else if (StringUtils.isNotBlank(agentUrl)) {
			return Host.builder().url(agentUrl + "/monitoring-agent").username(agentUserName).password(agentPassword)
					.build();
		} else {
			// 找不到主机异常
			throw new NotFoundHostException();
		}
	}

}

/**
 * <p>
 * 主机
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月5日 下午7:09:31
 */
@Data
@ToString
@Builder
@Accessors(chain = true)
class Host {
	/**
	 * URL
	 */
	String url;
	/**
	 * 用户名
	 */
	String username;
	/**
	 * 密码
	 */
	String password;
}
