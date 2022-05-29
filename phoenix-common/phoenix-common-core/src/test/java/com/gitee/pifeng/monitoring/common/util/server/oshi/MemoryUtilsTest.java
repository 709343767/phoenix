package com.gitee.pifeng.monitoring.common.util.server.oshi;

import com.gitee.pifeng.monitoring.common.domain.server.MemoryDomain;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * <p>
 * 测试内存工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/5/29 21:06
 */
@Slf4j
public class MemoryUtilsTest {

    /**
     * <p>
     * 测试获取内存信息
     * </p>
     *
     * @author 皮锋
     * @custom.date 2022/5/29 21:07
     */
    @Test
    public void testGetMemoryInfo() {
        MemoryDomain memoryInfo = MemoryUtils.getMemoryInfo();
        assertNotNull(memoryInfo);
        log.info(memoryInfo.toJsonString());
    }

}
