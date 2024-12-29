package com.gitee.pifeng.monitoring.common.util.server.oshi;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

@Slf4j
public class BaseboardUtilsTest {

    /**
     * <p>
     * 测试获取主板序列号
     * </p>
     *
     * @author 皮锋
     * @custom.date 2024/12/13 14:10
     */
    @Test
    public void testGetBaseboardSerialNumber() {
        String baseboardSerialNumber = BaseboardUtils.getBaseboardSerialNumber();
        assertNotNull(baseboardSerialNumber);
        log.info(baseboardSerialNumber);
    }

}
