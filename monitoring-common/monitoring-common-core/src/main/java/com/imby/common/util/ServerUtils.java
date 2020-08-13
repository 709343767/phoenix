package com.imby.common.util;

import com.imby.common.domain.server.ServerDomain;
import com.imby.common.init.InitSigar;
import org.hyperic.sigar.SigarException;

/**
 * <p>
 * 服务器信息工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月3日 上午11:55:09
 */
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
                .setMemoryDomain(MemoryUtils.getMemoryInfo())
                .setNetDomain(NetUtils.getNetInfo())
                .setOsDomain(OsUtils.getOsInfo())
                .setDiskDomain(DiskUtils.getDiskInfo());
    }

}
