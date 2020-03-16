package com.transfar.common.util;

import java.util.UUID;

/**
 * <p>
 * String工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月4日 下午5:07:49
 */
public final class StrUtils {

	/**
	 * 
	 * <p>
	 * 屏蔽共有构造方法
	 * </p>
	 * 
	 * @author 皮锋
	 */
	private StrUtils() {
	}

	/**
	 * <p>
	 * 获取UUID字符串
	 * </p>
	 *
	 * @author 皮锋
	 * @custom.date 2020年3月4日 下午5:10:33
	 * @return UUID字符串
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString();
	}
}
