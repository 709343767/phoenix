package com.transfar.common.util;

import lombok.Cleanup;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.charset.Charset;
import java.util.Enumeration;

/**
 * <p>
 * 网络工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/12 11:20
 */
public class NetUtils {

    /**
     * <p>
     * 判断操作系统是不是Windows
     * </p>
     *
     * @return boolean
     * @author 皮锋
     * @custom.date 2020年3月20日 上午10:30:30
     */
    public static boolean isWindowsOs() {
        boolean isWindowsOS = false;
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().contains("windows")) {
            isWindowsOS = true;
        }
        return isWindowsOS;
    }

    /**
     * <p>
     * 获取本机MAC地址
     * </p>
     *
     * @return MAC地址
     * @author 皮锋
     * @custom.date 2020/3/12 11:20
     */
    public static String getLocalMac() {
        try {
            InetAddress ia = InetAddress.getLocalHost();
            byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                if (i != 0) {
                    sb.append("-");
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
            return sb.toString().toLowerCase();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * <p>
     * 获取本机IP地址
     * </p>
     *
     * @return IP地址
     * @author 皮锋
     * @custom.date 2020/3/15 18:03
     */
    public static String getLocalIp() {
        try {
            // Windows操作系统
            if (isWindowsOs()) {
                InetAddress ip4 = Inet4Address.getLocalHost();
                return ip4.getHostAddress();
            } else {
                return getLinuxLocalIp();
            }
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * <p>
     * 获取linux系统下的IP地址
     * </p>
     *
     * @return IP地址
     * @author 皮锋
     * @custom.date 2020年3月20日 上午10:19:10
     */
    private static String getLinuxLocalIp() {
        String ip = "";
        try {
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
        } catch (Exception ex) {
            ip = "127.0.0.1";
        }
        return ip;
    }

    /**
     * <p>
     * 测试IP地址能否ping通
     * </p>
     *
     * @param address IP地址
     * @return boolean
     * @author 皮锋
     * @custom.date 2020/3/18 22:22
     */
    public static synchronized boolean ping(String address) {
        boolean result;
        try {
            Process process;
            // Windows系统
            if (isWindowsOs()) {
                process = Runtime.getRuntime().exec("ping " + address + " -n 8");
            } else {
                process = Runtime.getRuntime().exec("ping " + address + " -c 8");
            }
            assert process != null;
            @Cleanup
            InputStream inputStream = process.getInputStream();
            @Cleanup
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("GBK"));
            @Cleanup
            BufferedReader buf = new BufferedReader(inputStreamReader);
            String line;
            StringBuilder buffer = new StringBuilder();
            while ((line = buf.readLine()) != null) {
                buffer.append(line).append("\r\n");
            }
            String msg = buffer.toString();
            // 有收到数据包
            result = (!msg.contains("100%") || !msg.contains("loss")) && (!msg.contains("100%") || !msg.contains("丢失"));
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    public static void main(String[] args) {
        String mac = getLocalMac();
        System.out.println(mac);
        String hostAddress = getLocalIp();
        System.out.println(hostAddress);
        boolean ping = ping("127.0.0.1");
        System.out.println(ping);
        String ip = getLinuxLocalIp();
        System.out.println(ip);
    }
}
