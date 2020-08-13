package com.imby.common.util;

import com.imby.common.domain.server.ServerDomain;
import org.hyperic.sigar.SigarException;
import org.junit.Test;

/**
 * <p>
 * 测试服务器信息工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月3日 上午11:58:49
 */
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
        ServerDomain serverInfoVo = ServerUtils.getServerInfo();
        System.out.println(serverInfoVo.toJsonString());
    }

}
