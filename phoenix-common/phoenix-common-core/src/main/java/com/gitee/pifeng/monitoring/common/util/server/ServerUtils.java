package com.gitee.pifeng.monitoring.common.util.server;

import com.gitee.pifeng.monitoring.common.domain.Server;
import org.hyperic.sigar.SigarException;

/**
 * <p>
 * 服务器信息工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月3日 上午11:55:09
 */
public final class ServerUtils {

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
     * 通过 sigar 获取服务器信息
     * </p>
     *
     * @return {@link Server}
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020年3月3日 下午4:25:30
     */
    public static Server getSigarServerInfo() throws SigarException {
        return Server.builder()
                .cpuDomain(com.gitee.pifeng.monitoring.common.util.server.sigar.CpuUtils.getCpuInfo())
                .memoryDomain(com.gitee.pifeng.monitoring.common.util.server.sigar.MemoryUtils.getMemoryInfo())
                .netDomain(com.gitee.pifeng.monitoring.common.util.server.sigar.NetInterfaceUtils.getNetInfo())
                .diskDomain(com.gitee.pifeng.monitoring.common.util.server.sigar.DiskUtils.getDiskInfo())
                .osDomain(OsUtils.getOsInfo())
                .powerSourcesDomain(com.gitee.pifeng.monitoring.common.util.server.oshi.PowerSourceUtils.getPowerSourcesInfo())
                .sensorsDomain(com.gitee.pifeng.monitoring.common.util.server.oshi.SensorsUtils.getSensorsInfo())
                .processDomain(com.gitee.pifeng.monitoring.common.util.server.oshi.ProcessUtils.getProcessInfo())
                .systemLoadAverageDomain(com.gitee.pifeng.monitoring.common.util.server.oshi.SystemLoadAverageUtils.getSystemLoadAverage())
                .build();
    }

    /**
     * <p>
     * 通过 oshi 获取服务器信息
     * </p>
     *
     * @return {@link Server}
     * @author 皮锋
     * @custom.date 2022/6/2 17:23
     */
    public static Server getOshiServerInfo() {
        return Server.builder()
                .cpuDomain(com.gitee.pifeng.monitoring.common.util.server.oshi.CpuUtils.getCpuInfo())
                .memoryDomain(com.gitee.pifeng.monitoring.common.util.server.oshi.MemoryUtils.getMemoryInfo())
                .netDomain(com.gitee.pifeng.monitoring.common.util.server.oshi.NetInterfaceUtils.getNetInfo())
                .diskDomain(com.gitee.pifeng.monitoring.common.util.server.oshi.DiskUtils.getDiskInfo())
                .powerSourcesDomain(com.gitee.pifeng.monitoring.common.util.server.oshi.PowerSourceUtils.getPowerSourcesInfo())
                .sensorsDomain(com.gitee.pifeng.monitoring.common.util.server.oshi.SensorsUtils.getSensorsInfo())
                .processDomain(com.gitee.pifeng.monitoring.common.util.server.oshi.ProcessUtils.getProcessInfo())
                .systemLoadAverageDomain(com.gitee.pifeng.monitoring.common.util.server.oshi.SystemLoadAverageUtils.getSystemLoadAverage())
                .osDomain(OsUtils.getOsInfo())
                .build();
    }

}
