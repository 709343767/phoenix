package com.gitee.pifeng.monitoring.common.util.server.sigar;

import com.gitee.pifeng.monitoring.common.domain.server.NetDomain;
import com.gitee.pifeng.monitoring.common.init.InitSigar;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.hyperic.sigar.NetFlags;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.SigarException;

import java.util.List;

/**
 * <p>
 * 网络接口工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/1/14 9:56
 */
@Slf4j
public class NetInterfaceUtils extends InitSigar {

    /**
     * <p>
     * 获取网卡信息
     * </p>
     *
     * @return {@link NetDomain}
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020年3月3日 下午3:33:08
     */
    public static NetDomain getNetInfo() throws SigarException {
        NetDomain netDomain = new NetDomain();

        String[] netInfos = SIGAR.getNetInterfaceList();

        List<NetDomain.NetInterfaceDomain> netInterfaceConfigDomains = Lists.newArrayList();
        for (String info : netInfos) {
            // 网卡配置
            NetInterfaceConfig netInfo = SIGAR.getNetInterfaceConfig(info);
            // 网卡状态
            NetInterfaceStat netStat = SIGAR.getNetInterfaceStat(info);
            if (
                // 127.0.0.1
                    NetFlags.LOOPBACK_ADDRESS.equals(netInfo.getAddress())
                            // 标识为0
                            || netInfo.getFlags() == 0
                            // MAC地址不存在
                            || NetFlags.NULL_HWADDR.equals(netInfo.getHwaddr())
                            // 0.0.0.0
                            || NetFlags.ANY_ADDR.equals(netInfo.getAddress())
            ) {
                continue;
            }
            NetDomain.NetInterfaceDomain netInterfaceDomain = new NetDomain.NetInterfaceDomain();
            // 网卡名字
            String name = netInfo.getName();
            if (name.contains("docker") || name.contains("lo")) {
                continue;
            }
            // 网卡配置
            netInterfaceDomain.setName(name)
                    .setType(netInfo.getType())
                    .setAddress(netInfo.getAddress())
                    .setMask(netInfo.getNetmask())
                    .setBroadcast(netInfo.getBroadcast())
                    .setHwAddr(netInfo.getHwaddr())
                    .setDescription(netInfo.getDescription());
            // 网速
            long start = System.currentTimeMillis();
            long rxBytesStart = netStat.getRxBytes();
            long txBytesStart = netStat.getTxBytes();
            // 休眠一秒
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error("线程中断异常！", e);
            }
            long end = System.currentTimeMillis();
            NetInterfaceStat statEnd = SIGAR.getNetInterfaceStat(info);
            long rxBytesEnd = statEnd.getRxBytes();
            long txBytesEnd = statEnd.getTxBytes();
            // 1Byte=8bit
            double rxBps = (((double) (rxBytesEnd - rxBytesStart) * 8) / ((double) (end - start) / 1000)) / 8;
            double txBps = (((double) (txBytesEnd - txBytesStart) * 8) / ((double) (end - start) / 1000)) / 8;
            netInterfaceDomain.setDownloadBps(rxBps)
                    .setUploadBps(txBps);
            // 网卡状态
            netInterfaceDomain.setRxBytes(statEnd.getRxBytes())
                    .setRxDropped(statEnd.getRxDropped())
                    .setRxErrors(statEnd.getRxErrors())
                    .setRxPackets(statEnd.getRxPackets())
                    .setTxBytes(statEnd.getTxBytes())
                    .setTxDropped(statEnd.getTxDropped())
                    .setTxErrors(statEnd.getTxErrors())
                    .setTxPackets(statEnd.getTxPackets());
            netInterfaceConfigDomains.add(netInterfaceDomain);
        }
        netDomain.setNetNum(netInterfaceConfigDomains.size()).setNetList(netInterfaceConfigDomains);
        return netDomain;
    }

}
