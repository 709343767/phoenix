package com.gitee.pifeng.monitoring.common.util.server.sigar;

import com.gitee.pifeng.monitoring.common.domain.server.NetDomain;
import com.gitee.pifeng.monitoring.common.init.InitSigar;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
        try {
            NetDomain netDomain = new NetDomain();

            String[] netInfos = SIGAR.getNetInterfaceList();

            List<NetDomain.NetInterfaceDomain> netInterfaceConfigDomains = Lists.newArrayList();
            for (String info : netInfos) {
                // 网卡配置
                NetInterfaceConfig netInfo = SIGAR.getNetInterfaceConfig(info);
                // 网卡状态
                NetInterfaceStat netStat = SIGAR.getNetInterfaceStat(info);
                // 网卡地址
                String address = netInfo.getAddress();
                // MAC地址
                String hwaddr = netInfo.getHwaddr().toUpperCase();
                // 子网掩码
                String netmask = netInfo.getNetmask();
                // 广播地址
                String broadcast = netInfo.getBroadcast();
                // 网卡名字
                String name = netInfo.getName();
                // 网卡描述信息
                String description = netInfo.getDescription();
                if (
                    // 127.0.0.1
                        NetFlags.LOOPBACK_ADDRESS.equals(address)
                                // 标识为0
                                || netInfo.getFlags() == 0
                                // MAC地址不存在
                                || NetFlags.NULL_HWADDR.equals(hwaddr)
                                // 0.0.0.0
                                || NetFlags.ANY_ADDR.equals(address)
                                || StringUtils.containsIgnoreCase(name, "docker")
                                || StringUtils.containsIgnoreCase(name, "lo")
                ) {
                    continue;
                }
                NetDomain.NetInterfaceDomain netInterfaceDomain = new NetDomain.NetInterfaceDomain();
                // 网卡配置
                netInterfaceDomain.setName(name)
                        .setType(netInfo.getType())
                        .setAddress(address)
                        .setMask(netmask)
                        .setBroadcast(broadcast)
                        .setHwAddr(hwaddr)
                        .setDescription(description);
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
                // double rxBps = (((double) (rxBytesEnd - rxBytesStart) * 8) / ((double) (end - start) / 1000)) / 8;
                // double txBps = (((double) (txBytesEnd - txBytesStart) * 8) / ((double) (end - start) / 1000)) / 8;
                double rxBps = (double) (rxBytesEnd - rxBytesStart) / ((double) (end - start) / 1000);
                double txBps = (double) (txBytesEnd - txBytesStart) / ((double) (end - start) / 1000);
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
        } catch (SigarException s) {
            throw s;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

}
