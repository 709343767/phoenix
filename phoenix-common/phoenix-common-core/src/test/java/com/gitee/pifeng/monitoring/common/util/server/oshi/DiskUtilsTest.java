package com.gitee.pifeng.monitoring.common.util.server.oshi;

import com.gitee.pifeng.monitoring.common.domain.server.DiskDomain;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * <p>
 * 测试磁盘工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/5/12 11:13
 */
@Slf4j
public class DiskUtilsTest {

    /**
     * <p>
     * 测试获取磁盘信息
     * </p>
     *
     * @author 皮锋
     * @custom.date 2022/5/12 11:14
     */
    @Test
    public void testGetDiskInfo() {
        DiskDomain diskInfo = DiskUtils.getDiskInfo();
        assertNotNull(diskInfo);
        log.info(diskInfo.toJsonString());
    }

}
