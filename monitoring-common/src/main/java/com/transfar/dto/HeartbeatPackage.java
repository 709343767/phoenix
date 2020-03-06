package com.transfar.dto;

import java.io.Serializable;
import java.util.Date;

import com.transfar.common.InstanceBean;
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
public class HeartbeatPackage extends InstanceBean implements Serializable {

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
	 * 响应结果
	 */
	@ApiModelProperty(value = "响应结果")
	private Boolean result;

}
