package com.gitee.pifeng.monitoring.common.util.server.oshi;

import com.gitee.pifeng.monitoring.common.domain.server.GpuDomain;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * <p>
 * 测试GPU工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2025/6/6 16:52
 */
@Slf4j
public class GpuUtilsTest {

    /**
     * <p>
     * 测试获取Gpu信息
     * </p>
     *
     * @author 皮锋
     * @custom.date 2025/6/6 16:52
     */
    @Test
    public void testGetGpuInfo() {
        GpuDomain gpuInfo = GpuUtils.getGpuInfo();
        assertNotNull(gpuInfo);
        log.info(gpuInfo.toJsonString());
    }

}