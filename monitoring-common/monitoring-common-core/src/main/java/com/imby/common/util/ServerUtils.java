package com.imby.common.util;

import com.imby.common.domain.server.JvmDomain;
import com.imby.common.domain.server.ServerDomain;
import com.imby.common.init.InitSigar;
import lombok.extern.slf4j.Slf4j;
import org.hyperic.sigar.SigarException;

/**
 * <p>
 * Sigar工具类，获取服务器信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月3日 上午11:55:09
 */
@Slf4j
public final class ServerUtils extends InitSigar {

    /**
     * <p>
     * 屏蔽共有构造方法
     * </p>
     *
     * @author 皮锋
     */
    private ServerUtils() {
    }

    /**
     * <p>
     * 获取java虚拟机信息
     * </p>
     *
     * @return {@link JvmDomain}
     * @author 皮锋
     * @custom.date 2020年3月3日 下午4:17:55
     */
    public static JvmDomain getJvmInfo() {
        Runtime runTime = Runtime.getRuntime();
        return new JvmDomain()
                .setJavaPath(PROPS.getProperty("java.home"))
                .setJavaVendor(PROPS.getProperty("java.vendor"))
                .setJavaVersion(PROPS.getProperty("java.version"))
                .setJavaName(PROPS.getProperty("java.specification.name"))
                .setJvmVersion(PROPS.getProperty("java.vm.version"))
                .setJvmTotalMemory(String.format("%.2f", runTime.totalMemory() / MB) + "M")
                .setJvmFreeMemory(String.format("%.2f", runTime.freeMemory() / MB) + "M");
    }


    /**
     * <p>
     * 获取服务器信息
     * </p>
     *
     * @return {@link ServerDomain}
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020年3月3日 下午4:25:30
     */
    public static ServerDomain getServerInfo() throws SigarException {
        return new ServerDomain()
                .setCpuDomain(CpuUtils.getCpuInfo())
                .setJvmDomain(getJvmInfo())
                .setMemoryDomain(MemoryUtils.getMemoryInfo())
                .setNetDomain(NetUtils.getNetInfo())
                .setOsDomain(OsUtils.getOsInfo())
                .setDiskDomain(DiskUtils.getDiskInfo());
    }

}
