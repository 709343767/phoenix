package com.transfar.domain.server;

import com.transfar.common.SuperBean;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * java虚拟机信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月3日 下午4:09:49
 */
@Data
@Builder
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class JvmDomain extends SuperBean {
	/**
	 * java安路径
	 */
	private String javaPath;
	/**
	 * java运行时供应商
	 */
	private String javaVendor;
	/**
	 * java版本
	 */
	private String javaVersion;
	/**
	 * java运行时名称
	 */
	private String javaName;
	/**
	 * java虚拟机版本
	 */
	private String jvmVersion;
	/**
	 * java虚拟机总内存
	 */
	private String jvmTotalMemory;
	/**
	 * java虚拟机剩余内存
	 */
	private String jvmFreeMemory;

	@Override
	public String toString() {
		return "JvmVo [java安路径=" + javaPath + ", java运行时供应商=" + javaVendor + ", java版本=" + javaVersion + ", java运行时名称="
				+ javaName + ", java虚拟机版本=" + jvmVersion + ", java虚拟机总内存=" + jvmTotalMemory + ", java虚拟机剩余内存="
				+ jvmFreeMemory + "]";
	}

}
