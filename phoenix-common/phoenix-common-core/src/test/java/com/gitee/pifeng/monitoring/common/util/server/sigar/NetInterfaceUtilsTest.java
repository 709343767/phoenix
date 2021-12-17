package com.gitee.pifeng.monitoring.common.util.server.sigar;

import com.gitee.pifeng.monitoring.common.domain.server.NetDomain;
import lombok.extern.slf4j.Slf4j;
import org.hyperic.sigar.SigarException;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * <p>
 * 测试网络接口工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/1/14 10:00
 */
@Slf4j
public class NetInterfaceUtilsTest {

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
        NetDomain netVo = NetInterfaceUtils.getNetInfo();
        assertNotNull(netVo);
        int netNum = netVo.getNetNum();
        List<NetDomain.NetInterfaceDomain> netInterfaces = netVo.getNetList();
        log.info("网卡总数：" + netNum);
        netInterfaces.forEach(netInterfaceConfig -> log.info(netInterfaceConfig.toJsonString()));
    }

}
