package com.gitee.pifeng.monitoring.common.util.server;

import com.gitee.pifeng.monitoring.common.constant.TcpIpEnums;
import com.gitee.pifeng.monitoring.common.exception.NetException;
import lombok.extern.slf4j.Slf4j;
import org.hyperic.sigar.SigarException;
import org.junit.Test;

import java.util.Map;

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
     * 测试检测IP地址是否能ping通
     * </p>
     *
     * @author 皮锋
     * @custom.date 2021/1/14 13:01
     */
    @Test
    public void testIsConnect() throws NetException {
        Map<String, Object> objectMap = NetUtils.isConnect(NetUtils.getLocalIp());
        Object isConnect = objectMap.get("isConnect");
        Object avgTime = objectMap.get("avgTime");
        assertTrue(Boolean.parseBoolean(String.valueOf(isConnect)));
        log.info("isConnect：{}", isConnect);
        log.info("avgTime：{}", avgTime);
    }

    /**
     * <p>
     * 检测telnet状态
     * </p>
     *
     * @author 皮锋
     * @custom.date 2022/1/26 15:00
     */
    @Test
    public void testTelnet() {
        boolean telnet = NetUtils.telnet("127.0.0.1", 8000, TcpIpEnums.TCP);
        assertTrue(telnet);
    }

    /**
     * <p>
     * 检测telnet状态
     * </p>
     *
     * @author 皮锋
     * @custom.date 2022/3/6 15:00
     */
    @Test
    public void telnetVT200() {
        Map<String, Object> telnet = NetUtils.telnetVT200("127.0.0.1", 12000, TcpIpEnums.TCP);
        Object isConnect = telnet.get("isConnect");
        Object avgTime = telnet.get("avgTime");
        assertTrue(Boolean.parseBoolean(String.valueOf(isConnect)));
        log.info("isConnect：{}", isConnect);
        log.info("avgTime：{}", avgTime);
    }

}
