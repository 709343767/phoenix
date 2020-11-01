package com.imby.common.util;

import com.imby.common.domain.server.NetDomain;
import lombok.extern.slf4j.Slf4j;
import org.hyperic.sigar.SigarException;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * <p>
 * 测试网络工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/13 22:39
 */
@Slf4j
public class NetUtilsTest {

    /**
     * <p>
     * 测试获取网卡信息
     * </p>
     *
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020年3月3日 下午3:38:26
     */
    @Test
    public void testGetNetInfo() throws SigarException {
        NetDomain netVo = NetUtils.getNetInfo();
        assertNotNull(netVo);
        int netNum = netVo.getNetNum();
        List<NetDomain.NetInterfaceDomain> netInterfaces = netVo.getNetList();
        log.info("网卡总数：" + netNum);
        netInterfaces.forEach(netInterfaceConfig -> log.info(netInterfaceConfig.toJsonString()));
    }

}
