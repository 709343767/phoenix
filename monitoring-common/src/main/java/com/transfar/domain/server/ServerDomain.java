package com.transfar.domain.server;

import com.transfar.common.SuperBean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public final class ServerDomain extends SuperBean {

	/*
	 * 应用服务器信息
	 */
	//private AppServerDomain appServerDomain;

	/**
	 * 操作系统信息
	 */
	private OsDomain osDomain;

	/**
	 * 内存信息
	 */
	private MemoryDomain memoryDomain;

	/**
	 * Cpu信息
	 */
	private CpuDomain cpuDomain;

	/**
	 * 网卡信息
	 */
	private NetDomain netDomain;

	/**
	 * JVM信息
	 */
	private JvmDomain jvmDomain;

}
