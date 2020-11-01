package com.imby.common.util;

import com.imby.common.domain.server.OsDomain;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * <p>
 * 测试操作系统工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/13 22:35
 */
@Slf4j
public class OsUtilsTest {

    /**
     * <p>
     * 测试获取操作系统信息
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020年3月3日 下午2:17:18
     */
    @Test
    public void testGetOsInfo() {
        OsDomain osVo = OsUtils.getOsInfo();
        assertNotNull(osVo);
        log.info(osVo.toJsonString());
    }

}
