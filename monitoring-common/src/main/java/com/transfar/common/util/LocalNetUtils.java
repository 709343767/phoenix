package com.transfar.common.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * <p>
 * 本机MAC地址工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/12 11:20
 */
public class LocalNetUtils {

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

    public static void main(String[] args) {
        String mac = getLocalMac();
        System.out.println(mac);
        String hostAddress = getLocalHostAddress();
        System.out.println(hostAddress);
    }
}
