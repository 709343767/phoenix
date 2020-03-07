package com.transfar.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * MD5工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月4日 下午11:18:23
 */
@Slf4j
public final class MD5Utils {

	/**
	 * 
	 * <p>
	 * 私有化构造方法
	 * </p>
	 * 
	 * @author 皮锋
	 */
	private MD5Utils() {
	}

	/**
	 * 默认的密码字符串组合，用来将字节转换成 16 进制表示的字符,apache校验下载的文件的正确性用的就是默认的这个组合
	 */
	private static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };

	private static MessageDigest messagedigest = null;

	static {
		try {
			messagedigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException nsaex) {
			log.error("[" + MD5Utils.class.getName() + "]类初始化失败！", nsaex);
		}
	}

	/**
	 * <p>
	 * 字符串生成MD5校验码
	 * </p>
	 *
	 * @author 皮锋
	 * @custom.date 2020年3月4日 下午11:23:54
	 * @param s 字符串
	 * @return MD5校验码
	 */
	public static String getMD5String(String s) {
		return getMD5String(s.getBytes());
	}

	/**
	 * <p>
	 * bytes[]数组生成MD5校验码
	 * </p>
	 *
	 * @author 皮锋
	 * @custom.date 2020年3月4日 下午11:26:32
	 * @param bytes bytes数组
	 * @return MD5校验码
	 */
	public static String getMD5String(byte[] bytes) {
		messagedigest.update(bytes);
		return bufferToHex(messagedigest.digest());
	}

	/**
	 * <p>
	 * bytes[]转16进制字符串
	 * </p>
	 *
	 * @author 皮锋
	 * @custom.date 2020年3月4日 下午11:27:18
	 * @param bytes bytes数组
	 * @return 16进制字符串
	 */
	private static String bufferToHex(byte bytes[]) {
		return bufferToHex(bytes, 0, bytes.length);
	}

	/**
	 * <p>
	 * bytes[]转16进制字符串
	 * </p>
	 *
	 * @author 皮锋
	 * @custom.date 2020年3月4日 下午11:28:22
	 * @param bytes bytes数组
	 * @param m     开始位置
	 * @param n     长度
	 * @return 16进制字符串
	 */
	private static String bufferToHex(byte bytes[], int m, int n) {
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++) {
			appendHexPair(bytes[l], stringbuffer);
		}
		return stringbuffer.toString();
	}

	private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
		char c0 = hexDigits[(bt & 0xf0) >> 4];
		char c1 = hexDigits[bt & 0xf];
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}

}
