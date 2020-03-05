package com.transfar.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 与代理端相关的监控属性
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月5日 下午3:18:25
 */
@Data
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class MonitoringAgentProperties {

	/**
	 * 监控代理端url
	 */
	private String url;

	/**
	 * 监控代理端用户名
	 */
	private String username;

	/**
	 * 监控代理端密码
	 */
	private String password;

}
