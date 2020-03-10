package com.transfar.property;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 监控属性
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月5日 下午3:24:54
 */
@Data
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class MonitoringProperties {

	/**
	 * 与服务端相关的监控属性
	 */
	private MonitoringServerProperties serverProperties;

	/**
	 * 与自己相关的监控属性
	 */
	private MonitoringOwnProperties ownProperties;

	/**
	 * 心跳属性
	 */
	private MonitoringHeartbeatProperties heartbeatProperties;

	/**
	 * 服务器信息属性
	 */
	private MonitoringServerInfoProperties monitoringServerInfoProperties;

}
