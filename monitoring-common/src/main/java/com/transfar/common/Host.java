package com.transfar.common;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 主机
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月5日 下午7:09:31
 */
@Data
@Builder
@ToString
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class Host extends SuperBean {
	/**
	 * URL
	 */
	public String url;
	/**
	 * 用户名
	 */
	public String username;
	/**
	 * 密码
	 */
	public String password;
}