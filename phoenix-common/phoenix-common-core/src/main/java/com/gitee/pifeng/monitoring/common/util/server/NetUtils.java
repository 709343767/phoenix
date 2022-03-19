package com.gitee.pifeng.monitoring.common.util.server;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.net.NetUtil;
import com.gitee.pifeng.monitoring.common.constant.TcpIpEnums;
import com.gitee.pifeng.monitoring.common.domain.server.NetDomain;
import com.gitee.pifeng.monitoring.common.exception.NetException;
import com.gitee.pifeng.monitoring.common.util.server.sigar.NetInterfaceUtils;
import com.google.common.collect.Maps;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.telnet.TelnetClient;
import org.hyperic.sigar.SigarException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网络工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/12 11:20
 */
@Slf4j
public class NetUtils {

    /**
     * <p>
     * 私有化构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2021/1/14 9:58
     */
    private NetUtils() {
    }

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
    private static String getLocalMacByInetAddress() throws NetException {
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
            // log.error(exp, e);
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
    private static String getLocalMacBySigar() throws SigarException, NetException {
        // 获取本机IP地址
        String ip = getLocalIp();
        NetDomain netDomain = NetInterfaceUtils.getNetInfo();
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
     * @return 返回Map，key解释：<br>
     * 1.isConnect：是否能ping通；<br>
     * 2.avgTime：平均时间（毫秒）<br>
     * @author 皮锋
     * @custom.date 2020/12/15 10:58
     */
    public static Map<String, Object> isConnect(String ip) {
        // 返回值
        Map<String, Object> result = ping(ip);
        String isConnect = "isConnect";
        String avgTime = "avgTime";
        result.put(isConnect, NetUtil.ping(ip, 3000) && Boolean.parseBoolean(String.valueOf(result.get(isConnect))));
        result.put(avgTime, result.get(avgTime));
        return result;
    }

    /**
     * <p>
     * 检测IP地址是否能ping通
     * </p>
     *
     * @param ip IP地址
     * @return 返回Map，key解释：<br>
     * 1.isConnect：是否能ping通；<br>
     * 2.avgTime：平均时间（毫秒）<br>
     * @author 皮锋
     * @custom.date 2020/3/18 22:22
     */
    private static Map<String, Object> ping(String ip) {
        // 是否能ping通
        boolean isConnect;
        // ping平均时间
        String avgTime;
        ProcessBuilder processBuilder = new ProcessBuilder();
        // Windows系统
        if (OsUtils.isWindowsOs()) {
            processBuilder.command("ping", ip, "-n", "5");
        } else {
            processBuilder.command("ping", ip, "-c", "5");
        }
        try {
            // 将错误信息输出流合并到标准输出流
            processBuilder.redirectErrorStream(true);
            // 执行
            Process process = processBuilder.start();
            @Cleanup
            BufferedReader buf = new BufferedReader(new InputStreamReader(process.getInputStream(), Charset.forName("GBK")));
            // 返回值
            String line;
            // 全部返回数据
            StringBuilder lineResultBuilder = new StringBuilder();
            // ping正常的次数
            int connectedCount = 0;
            while ((line = buf.readLine()) != null) {
                lineResultBuilder.append(line);
                log.info(line);
                connectedCount += getCheckResult(line);
            }
            // 等待命令子线程执行完成
            process.waitFor();
            // 销毁子线程
            process.destroy();
            // 有收到数据包
            isConnect = connectedCount >= 1;
            // 获取平均时间
            avgTime = getPingAvgTime(lineResultBuilder.toString());
        } catch (Exception e) {
            isConnect = false;
            avgTime = "未知";
        }
        // 封装返回结果
        // 返回值
        Map<String, Object> result = Maps.newHashMap();
        result.put("isConnect", isConnect);
        result.put("avgTime", avgTime);
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
     * 获取ping平均时间
     * </p>
     *
     * @param pingResultStr ping命令返回字符串
     * @return ping平均时间（毫秒）
     * @author 皮锋
     * @custom.date 2022/1/10 11:29
     */
    private static String getPingAvgTime(String pingResultStr) {
        // 字符串为空
        if (StringUtils.isBlank(pingResultStr)) {
            return "未知";
        }
        // 全部转小写
        String lineResult = pingResultStr.toLowerCase();
        // 平均时间
        String avgTime;
        if (StringUtils.contains(lineResult, "average")) {
            lineResult = StringUtils.substringBetween(lineResult, "average", "ms");
            lineResult = StringUtils.remove(lineResult, "=");
            avgTime = StringUtils.trim(lineResult);
        } else if (StringUtils.contains(lineResult, "平均")) {
            lineResult = StringUtils.substringBetween(lineResult, "平均", "ms");
            lineResult = StringUtils.remove(lineResult, "=");
            avgTime = StringUtils.trim(lineResult);
        } else if (StringUtils.contains(lineResult, "min/avg/max/mdev")) {
            lineResult = StringUtils.substringBetween(lineResult, "min/avg/max/mdev", "ms");
            lineResult = StringUtils.trim(lineResult);
            avgTime = lineResult.split("/")[1];
        } else {
            avgTime = "未知";
        }
        return avgTime;
    }

    /**
     * <p>
     * 检测telnet状态
     * </p>
     *
     * @param hostname   主机名
     * @param port       端口号
     * @param tcpIpEnums TCP/IP协议簇枚举
     * @return boolean 返回是否telnet通
     * @author 皮锋
     * @custom.date 2022/1/10 9:37
     */
    @Deprecated
    public static boolean telnet(String hostname, int port, TcpIpEnums tcpIpEnums) {
        boolean isConnected = false;
        try {
            // TCP协议
            if (tcpIpEnums == TcpIpEnums.TCP) {
                @Cleanup
                Socket socket = new Socket();
                // 建立连接
                socket.connect(new InetSocketAddress(hostname, port), 3000);
                // 查看连通状态
                isConnected = socket.isConnected();
            }
        } catch (Exception e) {
            isConnected = false;
        }
        return isConnected;
    }

    /**
     * <p>
     * 检测telnet状态
     * </p>
     *
     * @param hostname   主机名
     * @param port       端口号
     * @param tcpIpEnums TCP/IP协议簇枚举
     * @return 返回Map，key解释：<br>
     * 1.isConnect：是否能telnet通；<br>
     * 2.avgTime：平均时间（毫秒）<br>
     * @author 皮锋
     * @custom.date 2022/3/6 9:37
     */
    public static Map<String, Object> telnetVT200(String hostname, int port, TcpIpEnums tcpIpEnums) {
        // 连通状态
        boolean isConnect = false;
        // 计时器
        TimeInterval timer = DateUtil.timer();
        try {
            // TCP协议
            if (tcpIpEnums == TcpIpEnums.TCP) {
                // 指明Telnet终端类型，否则会返回来的数据中文会乱码
                @Cleanup("disconnect")
                TelnetClient telnetClient = new TelnetClient("vt200");
                telnetClient.setConnectTimeout(3000);
                telnetClient.connect(hostname, port);
                // 查看连通状态
                isConnect = telnetClient.isConnected();
            }
        } catch (ConnectException connectException) {
            log.debug("对端拒绝连接，服务未启动端口监听或防火墙：{}", connectException.getMessage());
            isConnect = false;
        } catch (IOException ioException) {
            log.debug("对端连接失败：{}", ioException.getMessage());
            isConnect = false;
        }
        // 时间差（毫秒）
        long avgTime = timer.interval();
        // 返回值
        Map<String, Object> result = Maps.newHashMap();
        result.put("isConnect", isConnect);
        result.put("avgTime", avgTime);
        return result;
    }

}
