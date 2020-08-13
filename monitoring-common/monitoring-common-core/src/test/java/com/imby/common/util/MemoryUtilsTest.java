package com.imby.common.util;

import com.imby.common.domain.server.MemoryDomain;
import org.hyperic.sigar.SigarException;
import org.junit.Test;

/**
 * <p>
 * 测试内存工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/13 22:37
 */
public class MemoryUtilsTest {

    /**
     * <p>
     * 测试获取内存信息
     * </p>
     *
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020年3月3日 下午2:26:34
     */
    @Test
    public void testGetMemoryInfo() throws SigarException {
        MemoryDomain memoryVo = MemoryUtils.getMemoryInfo();
        System.out.println(memoryVo.toJsonString());
    }

}
