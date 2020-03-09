package com.transfar.business.server.domain;

import com.transfar.common.InstanceBean;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 应用实例
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月9日 上午10:49:29
 */
@Getter
@Setter
@ToString
@Builder
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class Instance extends InstanceBean {

	/**
	 * 是否在线
	 */
	private Boolean isOnLine;

}
