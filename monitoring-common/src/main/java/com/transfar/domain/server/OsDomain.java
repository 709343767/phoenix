package com.transfar.domain.server;

import com.transfar.common.SuperBean;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 操作系统信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月3日 下午2:08:48
 */
@Data
@Builder
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public final class OsDomain extends SuperBean {

	/**
	 * 操作系统名称
	 */
	private String osName;
	/**
	 * 操作系统版本
	 */
	private String osVersion;
	/**
	 * 用户名称
	 */
	private String userName;
	/**
	 * 用户主目录
	 */
	private String userHome;
	/**
	 * 操作系统时区
	 */
	private String osTimeZone;

	@Override
	public String toString() {
		return "OsVo [操作系统名称=" + osName + ", 操作系统版本=" + osVersion + ", 用户名称=" + userName + ", 用户主目录=" + userHome
				+ ", 操作系统时区=" + osTimeZone + "]";
	}

}
