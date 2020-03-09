package com.transfar.business.server.domain;

import com.transfar.common.SuperBean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * <p>
 * 短信
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月9日 下午9:43:03
 */
@Data
@Builder
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Sms extends SuperBean {

	/**
	 * 手机号码，多个手机号码用英文逗号隔开
	 */
	private String phone;

	/**
	 * 短信类型
	 */
	private String type;

	/**
	 * 短信内容
	 */
	private String content;

	/**
	 * 身份
	 */
	private String identity;

}
