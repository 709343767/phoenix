package com.transfar.common.util;

import lombok.Cleanup;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.charset.Charset;

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
            for (byte b : mac) {
                // 字节转换为整数
                int temp = b & 0xff;
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
    public static String getLocalHostAddress() {
        try {
            InetAddress ip4 = Inet4Address.getLocalHost();
            return ip4.getHostAddress();
        } catch (Exception e) {
            return "";
        }
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
            // 获取操作系统类型
            String os = System.getProperty("os.name").toLowerCase();
            Process process = null;
            // Linux系统
            if (os.contains("linux")) {
                process = Runtime.getRuntime().exec("ping " + address + " -c 8");
                // Windows系统
            } else if (os.contains("windows")) {
                process = Runtime.getRuntime().exec("ping " + address + " -n 8");
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
        String hostAddress = getLocalHostAddress();
        System.out.println(hostAddress);
        boolean ping = ping("127.0.0.1");
        System.out.println(ping);
    }
}
