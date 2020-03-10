package com.transfar.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * <p>
 * 短信配置属性
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月10日 下午2:22:46
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MonitoringSmsProperties {

	/**
	 * 手机号码
	 */
	private String[] phoneNumbers;

	/**
	 * 接口地址
	 */
	private String address;

	/**
	 * 接口协议
	 */
	private String protocol;

	/**
	 * 接口所属企业
	 */
	private String enterprise;

}
