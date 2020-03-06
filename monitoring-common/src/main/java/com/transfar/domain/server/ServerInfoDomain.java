package com.transfar.domain.server;

import com.transfar.common.SuperBean;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 服务器信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/3 11:26
 */
@Data
@Builder
@ToString
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ServerInfoDomain extends SuperBean {

	/**
	 * 应用服务器信息
	 */
	protected AppServerDomain appServerDomain;

	/**
	 * 操作系统信息
	 */
	protected OsDomain osDomain;

	/**
	 * 内存信息
	 */
	protected MemoryDomain memoryDomain;

	/**
	 * Cpu信息
	 */
	protected CpuDomain cpuDomain;

	/**
	 * 网卡信息
	 */
	protected NetDomain netDomain;

	/**
	 * JVM信息
	 */
	protected JvmDomain jvmDomain;

}
