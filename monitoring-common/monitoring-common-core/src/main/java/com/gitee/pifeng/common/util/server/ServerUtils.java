package com.gitee.pifeng.common.util.server;

import com.gitee.pifeng.common.domain.Server;
import com.gitee.pifeng.common.util.server.sigar.CpuUtils;
import com.gitee.pifeng.common.util.server.sigar.DiskUtils;
import com.gitee.pifeng.common.util.server.sigar.MemoryUtils;
import com.gitee.pifeng.common.util.server.sigar.NetInterfaceUtils;
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
     * 获取服务器信息
     * </p>
     *
     * @return {@link Server}
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020年3月3日 下午4:25:30
     */
    public static Server getServerInfo() throws SigarException {
        return Server.builder()
                .cpuDomain(CpuUtils.getCpuInfo())
                .memoryDomain(MemoryUtils.getMemoryInfo())
                .netDomain(NetInterfaceUtils.getNetInfo())
                .osDomain(OsUtils.getOsInfo())
                .diskDomain(DiskUtils.getDiskInfo())
                .build();
    }

}
