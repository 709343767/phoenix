package com.gitee.pifeng.monitoring.common.util.server;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * <p>
 * 测试IP地址工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/1/14 13:03
 */
@Slf4j
public class IpAddressUtilsTest {

    /**
     * <p>
     * 测试 判断字符串是否为IP地址
     * </p>
     *
     * @author 皮锋
     * @custom.date 2021/1/14 13:05
     */
    @Test
    public void testIsIpAddress() {
        boolean isIpAddress = IpAddressUtils.isIpAddress(NetUtils.getLocalIp());
        assertTrue(isIpAddress);
    }

    /**
     * <p>
     * 测试 获取某个IP所在网段的所有IP地址
     * </p>
     *
     * @author 皮锋
     * @custom.date 2024/11/13 16:24
     */
    @Test
    public void testGetAllIPsInRange() {
        List<String> allIPsInRange = IpAddressUtils.getAllIPsInRange(NetUtils.getLocalIp(), NetUtils.getLocalSubnetMask());
        assertTrue(CollectionUtils.isNotEmpty(allIPsInRange));
        allIPsInRange.forEach(log::info);
    }

}
