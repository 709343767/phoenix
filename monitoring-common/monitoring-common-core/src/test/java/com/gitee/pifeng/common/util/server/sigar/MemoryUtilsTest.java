package com.gitee.pifeng.common.util.server.sigar;

import com.gitee.pifeng.common.domain.server.MemoryDomain;
import lombok.extern.slf4j.Slf4j;
import org.hyperic.sigar.SigarException;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * <p>
 * 测试内存工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/13 22:37
 */
@Slf4j
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
        assertNotNull(memoryVo);
        log.info(memoryVo.toJsonString());
    }

}
