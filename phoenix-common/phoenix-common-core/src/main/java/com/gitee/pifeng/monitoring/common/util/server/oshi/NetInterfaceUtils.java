package com.gitee.pifeng.monitoring.common.util.server.oshi;

import cn.hutool.core.net.Ipv4Util;
import cn.hutool.core.util.ArrayUtil;
import com.gitee.pifeng.monitoring.common.domain.server.NetDomain;
import com.gitee.pifeng.monitoring.common.init.InitOshi;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hyperic.sigar.NetFlags;
import oshi.hardware.NetworkIF;

import java.util.List;

/**
 * <p>
 * 网络接口工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/5/30 9:26
 */
@Slf4j
public class NetInterfaceUtils extends InitOshi {

    /**
     * <p>
     * 获取网卡信息
     * </p>
     *
     * @return {@link NetDomain}
     * @author 皮锋
     * @custom.date 2022/5/30 9:26
     */
    public static NetDomain getNetInfo() {
        NetDomain netDomain = new NetDomain();
        List<NetDomain.NetInterfaceDomain> netInterfaceConfigDomains = Lists.newArrayList();

        List<NetworkIF> networkIfsEnd = SYSTEM_INFO.getHardware().getNetworkIFs(true);
        for (NetworkIF net : networkIfsEnd) {
            NetDomain.NetInterfaceDomain netInterfaceDomain = new NetDomain.NetInterfaceDomain();
            // 网卡地址
            String[] iPv4addr = net.getIPv4addr();
            // 掩码长度
            Short[] subnetMasks = net.getSubnetMasks();
            // MAC地址
            String macAddr = net.getMacaddr();
            // 网卡名字
            String netName = net.getName();
            // 网卡描述信息
            String displayName = net.getDisplayName();
            // 是否忽略此网卡
            if (ignore(iPv4addr, macAddr, netName)) {
                continue;
            }
            // 网速
            long start = System.currentTimeMillis();
            long rxBytesStart = net.getBytesRecv();
            long txBytesStart = net.getBytesSent();
            // 休眠一秒
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error("线程中断异常！", e);
            }
            long end = System.currentTimeMillis();
            // 更新此接口上的接口网络统计信息
            net.updateAttributes();
            long rxBytesEnd = net.getBytesRecv();
            long txBytesEnd = net.getBytesSent();
            // 网卡配置
            netInterfaceDomain.setName(net.getName())
                    .setType("Ethernet")
                    .setAddress(iPv4addr[0])
                    .setMask(Ipv4Util.getMaskByMaskBit(subnetMasks[0]))
                    .setBroadcast(Ipv4Util.getEndIpStr(iPv4addr[0], (int) subnetMasks[0]))
                    .setHwAddr(macAddr)
                    .setDescription(displayName);
            // 网卡状态
            netInterfaceDomain.setRxBytes(net.getBytesRecv())
                    .setRxDropped(net.getInDrops())
                    .setRxErrors(net.getInErrors())
                    .setRxPackets(net.getPacketsRecv())
                    .setTxBytes(net.getBytesSent())
                    .setTxDropped(net.getCollisions())
                    .setTxErrors(net.getOutErrors())
                    .setTxPackets(net.getPacketsSent());
            // 1Byte=8bit
            double rxBps = (double) (rxBytesEnd - rxBytesStart) / ((double) (end - start) / 1000);
            double txBps = (double) (txBytesEnd - txBytesStart) / ((double) (end - start) / 1000);
            // 网速
            netInterfaceDomain.setDownloadBps(rxBps)
                    .setUploadBps(txBps);
            netInterfaceConfigDomains.add(netInterfaceDomain);
        }

        netDomain.setNetNum(netInterfaceConfigDomains.size()).setNetList(netInterfaceConfigDomains);
        return netDomain;
    }

    /**
     * <p>
     * 是否忽略此网卡
     * </p>
     *
     * @param iPv4addr 网卡地址
     * @param macAddr  MAC地址
     * @param netName  网卡名字
     * @return 是/否
     * @author 皮锋
     * @custom.date 2022/5/31 10:13
     */
    private static boolean ignore(String[] iPv4addr, String macAddr, String netName) {
        return ArrayUtil.isEmpty(iPv4addr)
                // 127.0.0.1
                || ArrayUtil.contains(iPv4addr, NetFlags.LOOPBACK_ADDRESS)
                // MAC地址不存在
                || NetFlags.NULL_HWADDR.equals(macAddr)
                // 0.0.0.0
                || ArrayUtil.contains(iPv4addr, NetFlags.ANY_ADDR)
                || StringUtils.containsIgnoreCase(netName, "docker")
                || StringUtils.containsIgnoreCase(netName, "lo");
    }

}
