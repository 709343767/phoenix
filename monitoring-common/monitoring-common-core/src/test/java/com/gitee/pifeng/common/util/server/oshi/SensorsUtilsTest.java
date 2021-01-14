package com.gitee.pifeng.common.util.server.oshi;

import com.gitee.pifeng.common.domain.server.SensorDomain;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * <p>
 * 测试传感器工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/1/14 17:21
 */
@Slf4j
public class SensorsUtilsTest {

    /**
     * <p>
     * 测试获取传感器信息
     * </p>
     *
     * @author 皮锋
     * @custom.date 2021/1/14 17:29
     */
    @Test
    public void testGetSensorDomain() {
        SensorDomain sensorDomain = SensorsUtils.getSensorDomain();
        assertNotNull(sensorDomain);
        log.info(sensorDomain.toJsonString());
    }

}
