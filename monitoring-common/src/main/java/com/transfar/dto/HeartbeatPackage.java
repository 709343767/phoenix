package com.transfar.dto;

import java.io.Serializable;
import java.util.Date;

import com.transfar.common.SuperBean;
import com.transfar.util.StrUtils;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 心跳包传输层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月4日 下午12:20:06
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class HeartbeatPackage extends SuperBean implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1227833279912325068L;

	/**
	 * ID
	 */
	@ApiModelProperty(value = "ID")
	private String id = StrUtils.getUUID();

	/**
	 * 时间
	 */
	@ApiModelProperty(value = "时间")
	private Date dateTime;

	/**
	 * 端点（服务端、代理端、客户端）
	 */
	@ApiModelProperty(value = "端点（服务端、代理端、客户端）")
	private String endpoint;

	/**
	 * 应用实例ID
	 */
	@ApiModelProperty(value = "应用实例ID")
	private String instanceId;

	/**
	 * 应用实例名
	 */
	@ApiModelProperty(value = "应用实例名")
	private String instanceName;

	/**
	 * 响应结果
	 */
	@ApiModelProperty(value = "响应结果")
	private Boolean result;

}
