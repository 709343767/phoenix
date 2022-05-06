package com.gitee.pifeng.monitoring.common.util.server.oshi;

import com.gitee.pifeng.monitoring.common.domain.server.CpuDomain;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * <p>
 * 测试CPU工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/5/6 16:33
 */
@Slf4j
public class CpuUtilsTest {

    /**
     * <p>
     * 测试获取Cpu信息
     * </p>
     *
     * @author 皮锋
     * @custom.date 2022/5/6 16:33
     */
    @Test
    public void testGetCpuInfo() {
        CpuDomain cpuInfo = CpuUtils.getCpuInfo();
        assertNotNull(cpuInfo);
        log.info(cpuInfo.toJsonString());
    }

}
