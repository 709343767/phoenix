package com.transfar.server;

import java.util.List;

import org.hyperic.sigar.SigarException;
import org.junit.Test;

import com.transfar.domain.server.CpuDomain;
import com.transfar.domain.server.JvmDomain;
import com.transfar.domain.server.MemoryDomain;
import com.transfar.domain.server.NetDomain;
import com.transfar.domain.server.OsDomain;
import com.transfar.domain.server.ServerInfoDomain;
import com.transfar.util.SigarUtils;

/**
 * <p>
 * 测试获取服务器信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月3日 上午11:58:49
 */
public class ServerTest {

	/**
	 * <p>
	 * 测试获取应用服务器信息
	 * </p>
	 *
	 * @author 皮锋
	 * @custom.date 2020年3月3日 下午2:10:39
	 */
	// @Test
	// public void testGetAppServerInfo() {
	// AppServerDomain appServerVo = SigarUtils.getAppServerInfo();
	// System.out.println(appServerVo.toString());
	// }

	/**
	 * <p>
	 * 测试获取操作系统信息
	 * </p>
	 *
	 * @author 皮锋
	 * @custom.date 2020年3月3日 下午2:17:18
	 */
	@Test
	public void testGetOsInfo() {
		OsDomain osVo = SigarUtils.getOsInfo();
		System.out.println(osVo.toString());
	}

	/**
	 * <p>
	 * 测试获取内存信息
	 * </p>
	 *
	 * @throws SigarException Sigar异常
	 * @author 皮锋
	 * @custom.date 2020年3月3日 下午2:26:34
	 */
	@Test
	public void testGetMemoryInfo() throws SigarException {
		MemoryDomain memoryVo = SigarUtils.getMemoryInfo();
		System.out.println(memoryVo);
	}

	/**
	 * <p>
	 * 测试获取Cpu信息
	 * </p>
	 *
	 * @throws SigarException Sigar异常
	 * @author 皮锋
	 * @custom.date 2020年3月3日 下午3:04:36
	 */
	@Test
	public void testGetCpuInfo() throws SigarException {
		CpuDomain cpuVo = SigarUtils.getCpuInfo();
		int cpuNum = cpuVo.getCpuNum();
		List<CpuDomain.CpuInfoDomain> cpuInfoVos = cpuVo.getCpuList();
		System.out.println("CPU数量：" + cpuNum);
		cpuInfoVos.forEach(cpuInfoVo -> System.out.println("CPU信息：" + cpuInfoVo.toString()));
	}

	/**
	 * <p>
	 * 测试获取网卡信息
	 * </p>
	 *
	 * @throws SigarException Sigar异常
	 * @author 皮锋
	 * @custom.date 2020年3月3日 下午3:38:26
	 */
	@Test
	public void testGetNetInfo() throws SigarException {
		NetDomain netVo = SigarUtils.getNetInfo();
		int netNum = netVo.getNetNum();
		List<NetDomain.NetInterfaceConfigDomain> netInterfaceConfigs = netVo.getNetList();
		System.out.println("网卡总数：" + netNum);
		netInterfaceConfigs.forEach(netInterfaceConfig -> System.out.println(netInterfaceConfig.toString()));
	}

	/**
	 * <p>
	 * 测试获取java虚拟机信息
	 * </p>
	 *
	 * @author 皮锋
	 * @custom.date 2020年3月3日 下午4:20:58
	 */
	@Test
	public void testGetJvmInfo() {
		JvmDomain jvmVo = SigarUtils.getJvmInfo();
		System.out.println(jvmVo.toString());
	}

	/**
	 * <p>
	 * 测试获取服务器信息
	 * </p>
	 *
	 * @throws SigarException Sigar异常
	 * @author 皮锋
	 * @custom.date 2020年3月3日 下午4:28:43
	 */
	@Test
	public void testGetServerInfo() throws SigarException {
		// while (true) {
		ServerInfoDomain serverInfoVo = SigarUtils.getServerInfo();
		System.out.println(serverInfoVo.toJsonString());
		System.out.println();
		// Thread.sleep(10000);
		// }
	}

}
