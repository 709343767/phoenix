package com.transfar.dto;

import java.util.Date;

import com.transfar.common.InstanceBean;
import com.transfar.domain.server.ServerInfoDomain;
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
 * 服务器信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月6日 上午9:54:41
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ServerInfo extends InstanceBean {

	/**
	 * 服务器信息
	 */
	@ApiModelProperty(value = "服务器信息")
	private ServerInfoDomain serverInfoDomain;

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

}
