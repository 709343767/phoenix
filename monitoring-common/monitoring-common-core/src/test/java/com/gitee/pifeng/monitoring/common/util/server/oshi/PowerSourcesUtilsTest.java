package com.gitee.pifeng.monitoring.common.util.server.oshi;

import com.gitee.pifeng.monitoring.common.domain.server.PowerSourcesDomain;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * <p>
 * 测试电池工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/1/14 20:25
 */
@Slf4j
public class PowerSourcesUtilsTest {

    /**
     * <p>
     * 测试获取电池信息
     * </p>
     *
     * @author 皮锋
     * @custom.date 2021/1/14 20:28
     */
    @Test
    public void testGetPowerSourceInfo() {
        PowerSourcesDomain powerSourcesDomain = PowerSourceUtils.getPowerSourcesInfo();
        assertNotNull(powerSourcesDomain);
        log.info(powerSourcesDomain.toJsonString());
    }

}
