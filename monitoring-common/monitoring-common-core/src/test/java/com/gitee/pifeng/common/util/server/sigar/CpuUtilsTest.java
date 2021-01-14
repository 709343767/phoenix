package com.gitee.pifeng.common.util.server.sigar;

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

}
