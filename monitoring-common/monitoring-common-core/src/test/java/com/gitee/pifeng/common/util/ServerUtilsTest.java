package com.gitee.pifeng.common.util;

import com.gitee.pifeng.common.domain.Server;
import lombok.extern.slf4j.Slf4j;
import org.hyperic.sigar.SigarException;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * <p>
 * 测试服务器信息工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月3日 上午11:58:49
 */
@Slf4j
public class ServerUtilsTest {

    /**
     * <p>
     * 测试获取服务器信息
     * </p>
     *
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020年3月3日 下午4:28:43
     */
    @Test
    public void testGetServerInfo() throws SigarException {
        Server serverInfoVo = ServerUtils.getServerInfo();
        assertNotNull(serverInfoVo);
        log.info(serverInfoVo.toJsonString());
    }

}
