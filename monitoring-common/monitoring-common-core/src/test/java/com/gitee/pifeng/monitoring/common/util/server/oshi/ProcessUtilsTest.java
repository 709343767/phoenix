package com.gitee.pifeng.monitoring.common.util.server.oshi;

import com.gitee.pifeng.monitoring.common.domain.server.ProcessDomain;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * <p>
 * 测试进程工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/9/11 12:45
 */
@Slf4j
public class ProcessUtilsTest {

    /**
     * <p>
     * 测试获取进程信息
     * </p>
     *
     * @author 皮锋
     * @custom.date 2021/9/11 13:16
     */
    @Test
    public void testGetProcessInfo() {
        ProcessDomain processDomain = ProcessUtils.getProcessInfo();
        assertNotNull(processDomain);
        log.info(processDomain.toJsonString());
    }

}
