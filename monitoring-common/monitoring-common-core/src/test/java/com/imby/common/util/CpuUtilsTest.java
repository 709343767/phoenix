package com.imby.common.util;

import com.imby.common.domain.server.CpuDomain;
import org.hyperic.sigar.SigarException;
import org.junit.Test;

import java.util.List;

/**
 * <p>
 * 测试CPU工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/13 22:38
 */
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
        int cpuNum = cpuVo.getCpuNum();
        List<CpuDomain.CpuInfoDomain> cpuInfoVos = cpuVo.getCpuList();
        System.out.println("CPU数量：" + cpuNum);
        cpuInfoVos.forEach(cpuInfoVo -> System.out.println("CPU信息：" + cpuInfoVo.toJsonString()));
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
        System.out.println("系统可用的处理器核心数:" + CpuUtils.getAvailableProcessors());
        System.out.println("IO密集型线程池最佳线程数：" + (int) (CpuUtils.getAvailableProcessors() / (1 - 0.8)));
        System.out.println("CPU密集型线程池最佳线程数：" + (int) (CpuUtils.getAvailableProcessors() / (1 - 0.2)));
    }

}
