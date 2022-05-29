package com.gitee.pifeng.monitoring.common.util.server.oshi;

import cn.hutool.core.util.NumberUtil;
import com.gitee.pifeng.monitoring.common.domain.server.MemoryDomain;
import com.gitee.pifeng.monitoring.common.init.InitOshi;
import oshi.hardware.GlobalMemory;
import oshi.hardware.VirtualMemory;

/**
 * <p>
 * 内存工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/5/29 10:42
 */
public class MemoryUtils extends InitOshi {

    /**
     * <p>
     * 获取内存信息
     * </p>
     *
     * @return {@link MemoryDomain}
     * @author 皮锋
     * @custom.date 2022/5/29 10:44
     */
    public static MemoryDomain getMemoryInfo() {
        GlobalMemory memory = SYSTEM_INFO.getHardware().getMemory();
        long memoryTotal = memory.getTotal();
        long memFree = memory.getAvailable();
        long memUsed = memoryTotal - memFree;
        MemoryDomain.MenDomain menDomain = MemoryDomain.MenDomain.builder()
                .memTotal(memoryTotal)
                // 实际内存使用量
                .memUsed(memUsed)
                // 实际内存剩余量
                .memFree(memFree)
                // 物理内存使用率
                .menUsedPercent(NumberUtil.round((double) memUsed / (double) memoryTotal, 4).doubleValue())
                .build();
        VirtualMemory virtualMemory = memory.getVirtualMemory();
        long swapTotal = virtualMemory.getVirtualMax();
        // long swapUsed = virtualMemory.getSwapTotal(); // 到底是哪个？
        long swapUsed = virtualMemory.getSwapUsed() + virtualMemory.getVirtualInUse();
        long swapFree = swapTotal - swapUsed;
        MemoryDomain.SwapDomain swapDomain = MemoryDomain.SwapDomain.builder()
                // 交换区总量
                .swapTotal(swapTotal)
                // 交换区使用量
                .swapUsed(swapUsed)
                // 交换区剩余量
                .swapFree(swapFree)
                // 交换区使用率
                .swapUsedPercent(NumberUtil.round((double) swapUsed / (double) swapTotal, 4).doubleValue())
                .build();
        return MemoryDomain.builder().menDomain(menDomain).swapDomain(swapDomain).build();
    }

}
