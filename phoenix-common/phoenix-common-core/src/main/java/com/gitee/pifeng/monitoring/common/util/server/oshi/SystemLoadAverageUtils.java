package com.gitee.pifeng.monitoring.common.util.server.oshi;

import com.gitee.pifeng.monitoring.common.domain.server.SystemLoadAverageDomain;
import com.gitee.pifeng.monitoring.common.init.InitOshi;
import oshi.hardware.CentralProcessor;

/**
 * <p>
 * 系统平均负载工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/6/17 11:18
 */
public class SystemLoadAverageUtils extends InitOshi {

    /**
     * <p>
     * 获取系统平均负载信息
     * </p>
     *
     * @return {@link SystemLoadAverageDomain}
     * @author 皮锋
     * @custom.date 2022/6/17 11:28
     */
    public static SystemLoadAverageDomain getSystemLoadAverageInfo() {
        CentralProcessor processor = SYSTEM_INFO.getHardware().getProcessor();
        double[] loadAverage = processor.getSystemLoadAverage(3);
        return SystemLoadAverageDomain.builder()
                .logicalProcessorCount(processor.getLogicalProcessorCount())
                .oneLoadAverage(loadAverage[0])
                .fiveLoadAverage(loadAverage[1])
                .fifteenLoadAverage(loadAverage[2])
                .build();
    }

}
