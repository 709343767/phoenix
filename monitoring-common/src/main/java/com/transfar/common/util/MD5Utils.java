package com.transfar.common.util;

import java.security.MessageDigest;

import lombok.SneakyThrows;

/**
 * <p>
 * MD5工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月4日 下午11:18:23
 */
public final class MD5Utils {

	/**
	 * 加密算法
	 */
	private static final String ALGORITHM = "MD5";

	/**
	 * <p>
	 * 私有化构造方法
	 * </p>
	 *
	 * @author 皮锋
	 */
	private MD5Utils() {
	}

	/**
	 * <p>
	 * 获取32位md5校验码
	 * </p>
	 *
	 * @author 皮锋
	 * @custom.date 2020年3月13日 上午9:50:28
	 * @param encryptStr 字符串
	 * @return 32位md5校验码
	 */
	@SneakyThrows
	public static String encrypt32(String encryptStr) {
		MessageDigest md5 = MessageDigest.getInstance(ALGORITHM);
		byte[] md5Bytes = md5.digest(encryptStr.getBytes());
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

	/**
	 * <p>
	 * 获取16位md5校验码
	 * </p>
	 *
	 * @author 皮锋
	 * @custom.date 2020年3月13日 上午9:51:41
	 * @param encryptStr 字符串
	 * @return 16位md5校验码
	 */
	public static String encrypt16(String encryptStr) {
		return encrypt32(encryptStr).substring(8, 24);
	}

	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			System.out.println(encrypt16("http://localhost:12000/monitoring-agent"));
		}
	}

}
