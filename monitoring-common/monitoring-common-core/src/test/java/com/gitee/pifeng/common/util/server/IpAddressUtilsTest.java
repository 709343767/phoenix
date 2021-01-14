package com.gitee.pifeng.common.util.server;

import com.gitee.pifeng.common.exception.NetException;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * <p>
 * 测试IP地址工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/1/14 13:03
 */
public class IpAddressUtilsTest {

    /**
     * <p>
     * 判断字符串是否为IP地址
     * </p>
     *
     * @author 皮锋
     * @custom.date 2021/1/14 13:05
     */
    @Test
    public void test() throws NetException {
        boolean isIpAddress = IpAddressUtils.isIpAddress(NetUtils.getLocalIp());
        assertTrue(isIpAddress);
    }

}
