package com.gitee.pifeng.monitoring.common.util.server.oshi;

import com.gitee.pifeng.monitoring.common.domain.server.NetDomain;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * <p>
 * 测试网络接口工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/5/30 9:42
 */
@Slf4j
public class NetInterfaceUtilsTest {

    /**
     * <p>
     * c测试获取网卡信息
     * </p>
     *
     * @author 皮锋
     * @custom.date 2022/5/30 9:43
     */
    @Test
    public void testGetNetInfo() {
        NetDomain netInfo = NetInterfaceUtils.getNetInfo();
        assertNotNull(netInfo);
        int netNum = netInfo.getNetNum();
        log.info("网卡总数：" + netNum);
        log.info("网卡信息：" + netInfo.toJsonString());
    }

}
