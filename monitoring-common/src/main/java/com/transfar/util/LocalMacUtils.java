package com.transfar.util;

import java.net.InetAddress;
import java.net.NetworkInterface;

import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * 本机MAC地址工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/12 11:20
 */
public class LocalMacUtils {

	/**
	 * MAC地址
	 */
	private static String localMac = null;

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
		// 如果之前获取过了
		if (!StringUtils.isEmpty(localMac)) {
			return localMac;
		}
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

	public static void main(String[] args) {
		String mac = getLocalMac();
		System.out.println(mac);
	}
}
