package com.transfar.property;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 心跳属性
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月5日 下午3:23:02
 */
@Data
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class MonitoringHeartbeatProperties {

	/**
	 * 心跳频率
	 */
	private long rate;

}
