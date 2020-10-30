package com.imby.common.util;

import com.imby.common.domain.server.NetDomain;
import org.hyperic.sigar.SigarException;
import org.junit.Test;

import java.util.List;

/**
 * <p>
 * 测试网络工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/13 22:39
 */
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
        int netNum = netVo.getNetNum();
        List<NetDomain.NetInterfaceDomain> netInterfaces = netVo.getNetList();
        System.out.println("网卡总数：" + netNum);
        netInterfaces.forEach(netInterfaceConfig -> System.out.println(netInterfaceConfig.toJsonString()));
    }

}
