package com.gitee.pifeng.monitoring.common.util.server.sigar;

import com.gitee.pifeng.monitoring.common.domain.server.SystemLoadAverageDomain;
import com.gitee.pifeng.monitoring.common.init.InitSigar;
import com.gitee.pifeng.monitoring.common.util.server.OsUtils;
import lombok.extern.slf4j.Slf4j;
import org.hyperic.sigar.SigarException;

/**
 * <p>
 * 系统平均负载工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/6/17 11:18
 */
@Slf4j
public class SystemLoadAverageUtils extends InitSigar {

    /**
     * <p>
     * 获取系统平均负载信息
     * </p>
     *
     * @return {@link SystemLoadAverageDomain}
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2022/6/17 11:28
     */
    public static SystemLoadAverageDomain getSystemLoadAverageInfo() throws SigarException {
        try {
            // window系统不能获取，设置为-1
            if (OsUtils.isWindowsOs()) {
                return SystemLoadAverageDomain.builder()
                        .logicalProcessorCount(SIGAR.getCpuPercList().length)
                        .oneLoadAverage(-1D)
                        .fiveLoadAverage(-1D)
                        .fifteenLoadAverage(-1D)
                        .build();
            }
            double[] loadAverage = SIGAR.getLoadAverage();
            return SystemLoadAverageDomain.builder()
                    .logicalProcessorCount(SIGAR.getCpuPercList().length)
                    .oneLoadAverage(loadAverage[0])
                    .fiveLoadAverage(loadAverage[1])
                    .fifteenLoadAverage(loadAverage[2])
                    .build();
        } catch (SigarException s) {
            throw s;
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

}
