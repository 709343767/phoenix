package com.imby.common.util;

import com.google.common.collect.Lists;
import com.imby.common.domain.server.CpuDomain;
import com.imby.common.init.InitSigar;
import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.SigarException;

import java.util.List;

/**
 * <p>
 * CPU工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/13 12:27
 */
public class CpuUtils extends InitSigar {

    /**
     * <p>
     * 获取Cpu信息
     * </p>
     *
     * @return {@link CpuDomain}
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020年3月3日 下午1:04:04
     */
    public static CpuDomain getCpuInfo() throws SigarException {
        CpuDomain cpuDomain = new CpuDomain();

        CpuInfo[] cpuInfos = SIGAR.getCpuInfoList();
        CpuPerc[] cpuList = SIGAR.getCpuPercList();

        List<CpuDomain.CpuInfoDomain> cpuInfoDomains = Lists.newArrayList();
        for (int m = 0; m < cpuList.length; m++) {
            // 设置每一个cpu的信息
            CpuDomain.CpuInfoDomain cpuInfoDomain = new CpuDomain.CpuInfoDomain();

            cpuInfoDomain.setCpuMhz(cpuInfos[m].getMhz() + "MHz");
            String cpuIdle = String.format("%.2f", cpuList[m].getIdle() * 100) + "%";
            cpuInfoDomain.setCpuIdle(cpuIdle);
            String cpuCombined = String.format("%.2f", cpuList[m].getCombined() * 100) + "%";
            cpuInfoDomain.setCpuCombined(cpuCombined);
            cpuInfoDomains.add(cpuInfoDomain);
        }
        cpuDomain.setCpuNum(cpuInfoDomains.size()).setCpuList(cpuInfoDomains);
        return cpuDomain;
    }

    /**
     * <p>
     * 获取系统可用的处理器核心数
     * </p>
     *
     * @return 系统可用的处理器核心数
     * @author 皮锋
     * @custom.date 2020/8/25 9:04
     */
    public static int getAvailableProcessors() {
        return Runtime.getRuntime().availableProcessors();
    }

}
