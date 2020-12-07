package com.gitee.pifeng.common.util;

import com.gitee.pifeng.common.domain.server.CpuDomain;
import lombok.extern.slf4j.Slf4j;
import org.hyperic.sigar.SigarException;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * <p>
 * 测试CPU工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/13 22:38
 */
@Slf4j
public class CpuUtilsTest {

    /**
     * <p>
     * 测试获取Cpu信息
     * </p>
     *
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020年3月3日 下午3:04:36
     */
    @Test
    public void testGetCpuInfo() throws SigarException {
        CpuDomain cpuVo = CpuUtils.getCpuInfo();
        assertNotNull(cpuVo);
        int cpuNum = cpuVo.getCpuNum();
        List<CpuDomain.CpuInfoDomain> cpuInfoVos = cpuVo.getCpuList();
        log.info("CPU数量：" + cpuNum);
        cpuInfoVos.forEach(cpuInfoVo -> log.info("CPU信息：" + cpuInfoVo.toJsonString()));
    }

    /**
     * <p>
     * 测试系统可用的处理器核心数
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/8/25 9:16
     */
    @Test
    public void testGetAvailableProcessors() {
        int availableProcessors = CpuUtils.getAvailableProcessors();
        log.info("系统可用的处理器核心数:" + availableProcessors);
        log.info("IO密集型线程池最佳线程数：" + (int) (availableProcessors / (1 - 0.8)));
        log.info("CPU密集型线程池最佳线程数：" + (int) (availableProcessors / (1 - 0.2)));
    }

}
