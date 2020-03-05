package com.transfar.business.plug.dto;

import com.transfar.business.core.ConfigLoader;
import com.transfar.constant.EndpointTypeConstant;
import com.transfar.dto.HeartbeatPackage;
import com.transfar.util.InstanceIdUtils;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 代理端响应心跳包
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月5日 上午12:02:07
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class AgentResponseHeartbeatPackage extends HeartbeatPackage {

	/**
	 * <p>
	 * 在构造方法中设置：实例ID、实例名称、端点类型
	 * </p>
	 * 
	 * @author 皮锋
	 */
	public AgentResponseHeartbeatPackage() {
		this.setInstanceId();
		this.setInstanceName();
		this.setEndpoint();
	}

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -8080309817629513401L;

	/**
	 * <p>
	 * 设置实例ID
	 * </p>
	 *
	 * @author 皮锋
	 * @custom.date 2020年3月5日 上午12:08:43
	 */
	private void setInstanceId() {
		super.setInstanceId(InstanceIdUtils.getInstanceId());
	}

	/**
	 * <p>
	 * 设置实例名称
	 * </p>
	 *
	 * @author 皮锋
	 * @custom.date 2020年3月5日 上午12:08:59
	 */
	private void setInstanceName() {
		super.setInstanceName(ConfigLoader.getInstanceName());
	}

	/**
	 * <p>
	 * 设置端点类型为服务端
	 * </p>
	 *
	 * @author 皮锋
	 * @custom.date 2020年3月5日 上午12:11:37
	 */
	private void setEndpoint() {
		super.setEndpoint(EndpointTypeConstant.AGENT);
	}

}
