package com.transfar.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 与服务端相关的监控配置
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月4日 下午2:45:10
 */
@Data
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class MonitoringServerProperties {

	/**
	 * 监控服务端url
	 */
	private String url;

	/**
	 * 监控服务端用户名
	 */
	private String username;

	/**
	 * 监控服务端密码
	 */
	private String password;

}
