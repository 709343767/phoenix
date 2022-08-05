package com.gitee.pifeng.monitoring.common.util.server.oshi;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import oshi.software.os.OperatingSystem;

import static org.junit.Assert.assertNotNull;

/**
 * <p>
 * 测试操作系统工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/8/5 11:32
 */
@Slf4j
public class OperatingSystemUtilsTest {

    /**
     * <p>
     * 测试获取操作系统信息
     * </p>
     *
     * @author 皮锋
     * @custom.date 2022/8/5 11:33
     */
    @Test
    public void testGetOperatingSystemInfo() {
        OperatingSystem operatingSystem = OperatingSystemUtils.getOperatingSystemInfo();
        assertNotNull(operatingSystem);
        log.info("操作系统信息：" + operatingSystem.toString());
    }

}
