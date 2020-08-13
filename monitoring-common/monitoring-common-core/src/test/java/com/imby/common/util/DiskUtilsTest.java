package com.imby.common.util;

import com.imby.common.domain.server.DiskDomain;
import org.hyperic.sigar.SigarException;
import org.junit.Test;

import java.util.List;

/**
 * <p>
 * 测试磁盘工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/13 22:40
 */
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
        int diskNum = diskDomain.getDiskNum();
        System.out.println("磁盘总数：" + diskNum);
        List<DiskDomain.DiskInfoDomain> diskInfoDomains = diskDomain.getDiskInfoList();
        diskInfoDomains.forEach(diskInfoDomain -> System.out.println(diskInfoDomain.toJsonString()));
    }

}
