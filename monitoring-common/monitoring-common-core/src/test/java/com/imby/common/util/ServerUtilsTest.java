package com.imby.common.util;

import com.imby.common.domain.server.*;
import org.hyperic.sigar.SigarException;
import org.junit.Test;

import java.util.List;

/**
 * <p>
 * 测试获取服务器信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月3日 上午11:58:49
 */
public class ServerUtilsTest {

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
        OsDomain osVo = OsUtils.getOsInfo();
        System.out.println(osVo.toString());
    }

    /**
     * <p>
     * 测试获取计算机名
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/4/10 14:18
     */
    @Test
    public void testGetComputerName() {
        System.out.println(OsUtils.getComputerName());
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
        MemoryDomain memoryVo = MemoryUtils.getMemoryInfo();
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
        CpuDomain cpuVo = CpuUtils.getCpuInfo();
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
        NetDomain netVo = NetUtils.getNetInfo();
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
        JvmDomain jvmVo = ServerUtils.getJvmInfo();
        System.out.println(jvmVo.toString());
    }

    /**
     * <p>
     * 测试获取磁盘信息
     * </p>
     *
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020/3/8 21:44
     */
    @Test
    public void testGetDiskInfo() throws SigarException {
        DiskDomain diskDomain = DiskUtils.getDiskInfo();
        int diskNum = diskDomain.getDiskNum();
        System.out.println("磁盘总数：" + diskNum);
        List<DiskDomain.DiskInfoDomain> diskInfoDomains = diskDomain.getDiskInfoList();
        diskInfoDomains.forEach(diskInfoDomain -> System.out.println(diskInfoDomain.toString()));
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
        ServerDomain serverInfoVo = ServerUtils.getServerInfo();
        System.out.println(serverInfoVo.toJsonString());
        System.out.println();
        // Thread.sleep(10000);
        // }
    }

}
