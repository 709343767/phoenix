package com.gitee.pifeng.monitoring.common.util.server.sigar;

import cn.hutool.core.util.NumberUtil;
import com.gitee.pifeng.monitoring.common.domain.server.MemoryDomain;
import com.gitee.pifeng.monitoring.common.init.InitSigar;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Swap;

/**
 * <p>
 * 内存工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/13 11:35
 */
public class MemoryUtils extends InitSigar {

    /**
     * <p>
     * 获取内存信息
     * </p>
     *
     * @return {@link MemoryDomain}
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020年3月3日 下午2:24:41
     */
    public static MemoryDomain getMemoryInfo() throws SigarException {
        Mem mem = SIGAR.getMem();
        Swap swap = SIGAR.getSwap();
        MemoryDomain.MenDomain menDomain = MemoryDomain.MenDomain.builder()
                .memTotal(mem.getTotal())
                // 实际内存使用量
                .memUsed(mem.getActualUsed())
                // 实际内存剩余量
                .memFree(mem.getActualFree())
                // 物理内存使用率
                .menUsedPercent(NumberUtil.round(mem.getUsedPercent() / 100D, 4).doubleValue())
                .build();
        MemoryDomain.SwapDomain swapDomain = MemoryDomain.SwapDomain.builder()
                // 交换区总量
                .swapTotal(swap.getTotal())
                // 交换区使用量
                .swapUsed(swap.getUsed())
                // 交换区剩余量
                .swapFree(swap.getFree())
                // 交换区使用率
                .swapUsedPercent(NumberUtil.round((double) swap.getUsed() / (double) swap.getTotal(), 4).doubleValue())
                .build();
        return MemoryDomain.builder().menDomain(menDomain).swapDomain(swapDomain).build();
    }
}
