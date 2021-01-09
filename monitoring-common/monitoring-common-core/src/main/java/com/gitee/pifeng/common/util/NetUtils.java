package com.gitee.pifeng.common.util;

import cn.hutool.core.net.NetUtil;
import com.gitee.pifeng.common.domain.server.NetDomain;
import com.gitee.pifeng.common.exception.NetException;
import com.gitee.pifeng.common.init.InitSigar;
import com.google.common.collect.Lists;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hyperic.sigar.NetFlags;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.SigarException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.List;

/**
 * <p>
 * 网络工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/12 11:20
 */
@Slf4j
public class NetUtils extends InitSigar {


    /**
     * <p>
     * 获取本机MAC地址
     * </p>
     *
     * @return MAC地址
     * @throws NetException   获取网络信息异常：获取本机MAC地址异常！
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020/9/15 12:35
     */
    public static String getLocalMac() throws NetException, SigarException {
        try {
            // 通过InetAddress的方式
            return getLocalMacByInetAddress();
        } catch (Exception e) {
            // 通过Sigar的方式
            return getLocalMacBySigar();
        }
    }

    /**
     * <p>
     * 获取本机MAC地址：通过InetAddress的方式。
     * </p>
     *
     * @return MAC地址
     * @throws NetException 获取网络信息异常：获取本机MAC地址异常！
     * @author 皮锋
     * @custom.date 2020/3/12 11:20
     */
    public static String getLocalMacByInetAddress() throws NetException {
        try {
            InetAddress ia = InetAddress.getLocalHost();
            byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                if (i != 0) {
                    sb.append(":");
                }
                // 字节转换为整数
                int temp = mac[i] & 0xff;
                String str = Integer.toHexString(temp);
                if (str.length() == 1) {
                    sb.append("0").append(str);
                } else {
                    sb.append(str);
                }
            }
            return sb.toString().toUpperCase();
        } catch (Exception e) {
            String exp = "获取本机MAC地址异常！";
            log.error(exp, e);
            throw new NetException(exp);
        }
    }

    /**
     * <p>
     * 获取本机MAC地址：通过Sigar的方式。
     * </p>
     *
     * @return MAC地址
     * @throws NetException   获取网络信息异常：获取本机MAC地址异常！
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020/8/30 16:41
     * @since v0.0.2
     */
    public static String getLocalMacBySigar() throws SigarException, NetException {
        // 获取本机IP地址
        String ip = getLocalIp();
        NetDomain netDomain = getNetInfo();
        List<NetDomain.NetInterfaceDomain> netInterfaceDomains = netDomain.getNetList();
        for (NetDomain.NetInterfaceDomain netInterfaceDomain : netInterfaceDomains) {
            // 网卡IP地址
            String address = netInterfaceDomain.getAddress();
            // 是当前网卡的IP地址
            if (StringUtils.equals(ip, address)) {
                // 返回此网卡的MAC地址
                return netInterfaceDomain.getHwAddr();
            }
        }
        // 手动抛出异常
        String exp = "获取本机MAC地址异常！";
        NetException netException = new NetException(exp);
        log.error(exp, netException);
        throw new NetException(exp);
    }

    /**
     * <p>
     * 获取本机IP地址
     * </p>
     *
     * @return IP地址
     * @throws NetException 获取网络信息异常：获取本机IP地址异常！
     * @author 皮锋
     * @custom.date 2020/3/15 18:03
     */
    public static String getLocalIp() throws NetException {
        try {
            // Windows操作系统
            if (OsUtils.isWindowsOs()) {
                return getWindowsLocalIp();
            } else {
                return getLinuxLocalIp();
            }
        } catch (Exception e) {
            String exp = "获取本机IP地址异常！";
            log.error(exp, e);
            throw new NetException(exp);
        }
    }

    /**
     * <p>
     * 获取Windows系统下的IP地址
     * </p>
     *
     * @return IP地址
     * @throws UnknownHostException 无法确定主机的IP地址异常
     * @author 皮锋
     * @custom.date 2020/12/15 10:44
     */
    private static String getWindowsLocalIp() throws UnknownHostException {
        InetAddress ip4 = InetAddress.getLocalHost();
        return ip4.getHostAddress();
    }

    /**
     * <p>
     * 获取Linux系统下的IP地址
     * </p>
     *
     * @return IP地址
     * @throws SocketException 创建或访问套接字时异常
     * @author 皮锋
     * @custom.date 2020年3月20日 上午10:19:10
     */
    private static String getLinuxLocalIp() throws SocketException {
        String ip = null;
        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
            NetworkInterface intf = en.nextElement();
            String name = intf.getName();
            if (!name.contains("docker") && !name.contains("lo")) {
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ipaddress = inetAddress.getHostAddress();
                        if (!ipaddress.contains("::") && !ipaddress.contains("0:0:")
                                && !ipaddress.contains("fe80")) {
                            ip = ipaddress;
                        }
                    }
                }
            }
        }
        return ip;
    }

    /**
     * <p>
     * 检测IP地址是否能ping通
     * </p>
     *
     * @param ip ip地址
     * @return 返回是否ping通
     * @author 皮锋
     * @custom.date 2020/12/15 10:58
     */
    public static boolean isConnect(String ip) {
        return NetUtil.ping(ip, 3000) && ping(ip);
    }

    /**
     * <p>
     * 检测IP地址是否能ping通
     * </p>
     *
     * @param ip IP地址
     * @return 返回是否ping通
     * @author 皮锋
     * @custom.date 2020/3/18 22:22
     */
    private static boolean ping(String ip) {
        boolean result;
        try {
            Process process;
            // Windows系统
            if (OsUtils.isWindowsOs()) {
                process = Runtime.getRuntime().exec("ping " + ip + " -n 5");
            } else {
                process = Runtime.getRuntime().exec("ping " + ip + " -c 5");
            }
            if (process == null) {
                result = false;
            } else {
                @Cleanup
                InputStream inputStream = process.getInputStream();
                @Cleanup
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("GBK"));
                @Cleanup
                BufferedReader buf = new BufferedReader(inputStreamReader);
                String line;
                int connectedCount = 0;
                while ((line = buf.readLine()) != null) {
                    connectedCount += getCheckResult(line);
                }
                // 有收到数据包
                result = connectedCount >= 1;
            }
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    /**
     * <p>
     * 检测ping返回值是否是网络正常的返回值
     * </p>
     *
     * @param line ping返回的数据
     * @return 0或者1
     * @author 皮锋
     * @custom.date 2020/3/25 21:15
     */
    private static int getCheckResult(String line) {
        // 若line含有=18ms TTL=16字样，说明已经ping通，返回1，否则返回0
        //Pattern pattern = Pattern.compile("((\\d+ms)(\\s+)(TTL=\\d+))|((TTL=\\d+)(\\s+)(\\S*\\d+\\s*ms))",
        //        Pattern.CASE_INSENSITIVE);
        //Matcher matcher = pattern.matcher(line);
        //if (matcher.find()) {
        //    return 1;
        //}
        //return 0;
        if (StringUtils.containsIgnoreCase(line, "ms") && StringUtils.containsIgnoreCase(line, "ttl")) {
            return 1;
        }
        return 0;
    }

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
            // 网卡配置
            netInterfaceDomain.setName(netInfo.getName())
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
