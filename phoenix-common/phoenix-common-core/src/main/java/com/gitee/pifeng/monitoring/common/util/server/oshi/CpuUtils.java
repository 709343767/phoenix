package com.gitee.pifeng.monitoring.common.util.server.oshi;

import com.gitee.pifeng.monitoring.common.domain.server.CpuDomain;
import com.gitee.pifeng.monitoring.common.init.InitOshi;
import com.google.common.collect.Lists;
import oshi.hardware.CentralProcessor;

import java.util.List;

/**
 * <p>
 * CPU工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/5/5 17:22
 */
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
        long[] currentFreq = processor.getCurrentFreq();
        long[][] processorCpuLoadTicks = processor.getProcessorCpuLoadTicks();
        double[] processorCpuLoadBetweenTicks = processor.getProcessorCpuLoadBetweenTicks(processorCpuLoadTicks);
        // 创建返回对象
        CpuDomain cpuDomain = new CpuDomain();
        List<CpuDomain.CpuInfoDomain> cpuInfoDomains = Lists.newArrayList();
        for (int i = 0; i < processorCpuLoadBetweenTicks.length; i++) {
            // 设置每一个cpu的信息
            CpuDomain.CpuInfoDomain cpuInfoDomain = new CpuDomain.CpuInfoDomain();
            // CPU的总量MHz
            cpuInfoDomain.setCpuMhz((int) (currentFreq[i] / 1000000L));
            // 获得CPU的类别，如：Celeron
            cpuInfoDomain.setCpuModel(model);
            // 获得CPU的卖主，如：Intel
            cpuInfoDomain.setCpuVendor(vendor);
            // 用户使用率
            //cpuInfoDomain.setCpuUser(cpuPerc.getUser());
            // 系统使用率
            cpuInfoDomain.setCpuSys(processorCpuLoadBetweenTicks[i]);
            // 当前等待率
            //cpuInfoDomain.setCpuWait(cpuPerc.getWait());
            // 当前错误率
            //cpuInfoDomain.setCpuNice(cpuPerc.getNice());
            // 当前空闲率
            //cpuInfoDomain.setCpuIdle(cpuPerc.getIdle());
            // 总的使用率
            //cpuInfoDomain.setCpuCombined(cpuPerc.getCombined());

            cpuInfoDomains.add(cpuInfoDomain);
        }
        cpuDomain.setCpuNum(cpuInfoDomains.size()).setCpuList(cpuInfoDomains);
        return cpuDomain;
    }
}
