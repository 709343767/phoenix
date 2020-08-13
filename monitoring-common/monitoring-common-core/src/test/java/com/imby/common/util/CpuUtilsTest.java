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

}
