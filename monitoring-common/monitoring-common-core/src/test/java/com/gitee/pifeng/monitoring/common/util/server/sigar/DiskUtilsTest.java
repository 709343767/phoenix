package com.gitee.pifeng.monitoring.common.util.server.sigar;

import com.gitee.pifeng.monitoring.common.domain.server.DiskDomain;
import lombok.extern.slf4j.Slf4j;
import org.hyperic.sigar.SigarException;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * <p>
 * 测试磁盘工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/13 22:40
 */
@Slf4j
public class DiskUtilsTest {

    /**
     * <p>
     * 测试获取磁盘信息
     * </p>
     *
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020/3/8 21:44
     */
    @Test
    public void testGetDiskInfo() throws SigarException {
        DiskDomain diskDomain = DiskUtils.getDiskInfo();
        assertNotNull(diskDomain);
        int diskNum = diskDomain.getDiskNum();
        log.info("磁盘总数：" + diskNum);
        List<DiskDomain.DiskInfoDomain> diskInfoDomains = diskDomain.getDiskInfoList();
        diskInfoDomains.forEach(diskInfoDomain -> log.info(diskInfoDomain.toJsonString()));
    }

}
