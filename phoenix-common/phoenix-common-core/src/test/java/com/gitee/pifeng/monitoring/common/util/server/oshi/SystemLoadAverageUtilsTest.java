package com.gitee.pifeng.monitoring.common.util.server.oshi;

import com.gitee.pifeng.monitoring.common.domain.server.SystemLoadAverageDomain;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * <p>
 * 测试系统平均负载工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/6/17 11:30
 */
@Slf4j
public class SystemLoadAverageUtilsTest {

    /**
     * <p>
     * 测试获取系统平均负载信息
     * </p>
     *
     * @author 皮锋
     * @custom.date 2022/6/17 11:30
     */
    @Test
    public void testGetSystemLoadAverageInfo() {
        SystemLoadAverageDomain systemLoadAverage = SystemLoadAverageUtils.getSystemLoadAverageInfo();
        assertNotNull(systemLoadAverage);
        log.info(systemLoadAverage.toJsonString());
    }

}
