package com.transfar.common.dto;

import java.io.Serializable;
import java.util.Date;

import com.transfar.common.abs.InstanceBean;

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
 * 基础响应包
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月11日 上午9:40:59
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class BaseResponsePackage extends InstanceBean implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -8909786365854143972L;
	
	/**
	 * ID
	 */
	@ApiModelProperty(value = "ID")
	private String id;

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
