package com.gitee.pifeng.common.util.server;

import com.gitee.pifeng.common.exception.NetException;
import lombok.extern.slf4j.Slf4j;
import org.hyperic.sigar.SigarException;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * <p>
 * 测试网络工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/1/14 12:54
 */
@Slf4j
public class NetUtilsTest {

    /**
     * <p>
     * 测试获取本机MAC地址
     * </p>
     *
     * @author 皮锋
     * @custom.date 2021/1/14 12:57
     */
    @Test
    public void testGetLocalMac() throws NetException, SigarException {
        String mac = NetUtils.getLocalMac();
        assertNotNull(mac);
        log.info(mac);
    }

    /**
     * <p>
     * 测试获取本机IP地址
     * </p>
     *
     * @author 皮锋
     * @custom.date 2021/1/14 12:58
     */
    @Test
    public void testGetLocalIp() throws NetException {
        String ip = NetUtils.getLocalIp();
        assertNotNull(ip);
        log.info(ip);
    }

    /**
     * <p>
     * 检测IP地址是否能ping通
     * </p>
     *
     * @author 皮锋
     * @custom.date 2021/1/14 13:01
     */
    @Test
    public void testIsConnect() throws NetException {
        boolean isConnect = NetUtils.isConnect(NetUtils.getLocalIp());
        assertTrue(isConnect);
    }

}
