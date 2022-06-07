package com.gitee.pifeng.monitoring.common.util.server.oshi;

import com.gitee.pifeng.monitoring.common.domain.server.CpuDomain;
import com.gitee.pifeng.monitoring.common.init.InitOshi;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import oshi.hardware.CentralProcessor;
import oshi.hardware.CentralProcessor.TickType;

import java.util.List;

/**
 * <p>
 * CPU工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/5/5 17:22
 */
@Slf4j
public class CpuUtils extends InitOshi {

    /**
     * <p>
     * 获取Cpu信息
     * </p>
     *
     * @return {@link CpuDomain}
     * @author 皮锋
     * @custom.date 2022/5/5 17:24
     */
    public static CpuDomain getCpuInfo() {
        CentralProcessor processor = SYSTEM_INFO.getHardware().getProcessor();
        CentralProcessor.ProcessorIdentifier processorIdentifier = processor.getProcessorIdentifier();
        String model = processorIdentifier.getName();
        String vendor = processorIdentifier.getVendor();
        int logicalProcessorCount = processor.getLogicalProcessorCount();
        long[] currentFreq = processor.getCurrentFreq();
        long[][] prevTicks = processor.getProcessorCpuLoadTicks();
        // 休眠一秒
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error("线程中断异常！", e);
        }
        long[][] ticks = processor.getProcessorCpuLoadTicks();
        // 创建返回对象
        CpuDomain cpuDomain = new CpuDomain();
        List<CpuDomain.CpuInfoDomain> cpuInfoDomains = Lists.newArrayList();
        for (int cpu = 0; cpu < logicalProcessorCount; cpu++) {

            long user = ticks[cpu][TickType.USER.getIndex()] - prevTicks[cpu][TickType.USER.getIndex()];
            long nice = ticks[cpu][TickType.NICE.getIndex()] - prevTicks[cpu][TickType.NICE.getIndex()];
            long sys = ticks[cpu][TickType.SYSTEM.getIndex()] - prevTicks[cpu][TickType.SYSTEM.getIndex()];
            long idle = ticks[cpu][TickType.IDLE.getIndex()] - prevTicks[cpu][TickType.IDLE.getIndex()];
            long iowait = ticks[cpu][TickType.IOWAIT.getIndex()] - prevTicks[cpu][TickType.IOWAIT.getIndex()];
            long irq = ticks[cpu][TickType.IRQ.getIndex()] - prevTicks[cpu][TickType.IRQ.getIndex()];
            long softirq = ticks[cpu][TickType.SOFTIRQ.getIndex()] - prevTicks[cpu][TickType.SOFTIRQ.getIndex()];
            long steal = ticks[cpu][TickType.STEAL.getIndex()] - prevTicks[cpu][TickType.STEAL.getIndex()];
            long totalCpu = user + nice + sys + idle + iowait + irq + softirq + steal;

            // 设置每一个cpu的信息
            CpuDomain.CpuInfoDomain cpuInfoDomain = new CpuDomain.CpuInfoDomain();
            // CPU的总量MHz
            cpuInfoDomain.setCpuMhz((int) (currentFreq[cpu] / 1000000L));
            // 获得CPU的类别，如：Celeron
            cpuInfoDomain.setCpuModel(model);
            // 获得CPU的卖主，如：Intel
            cpuInfoDomain.setCpuVendor(vendor);
            // 用户使用率
            cpuInfoDomain.setCpuUser((double) user / (double) totalCpu);
            // 系统使用率
            cpuInfoDomain.setCpuSys((double) sys / (double) totalCpu);
            // 当前等待率
            cpuInfoDomain.setCpuWait((double) iowait / (double) totalCpu);
            // 当前错误率
            cpuInfoDomain.setCpuNice((double) nice / (double) totalCpu);
            // 当前空闲率
            cpuInfoDomain.setCpuIdle((double) idle / (double) totalCpu);
            // 总的使用率
            cpuInfoDomain.setCpuCombined((double) (user + sys) / (double) totalCpu);

            cpuInfoDomains.add(cpuInfoDomain);
        }
        cpuDomain.setCpuNum(cpuInfoDomains.size()).setCpuList(cpuInfoDomains);
        return cpuDomain;
    }
}
