package com.transfar.util;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

import com.transfar.business.core.ConfigLoader;
import com.transfar.config.ContextUtils;

/**
 * <p>
 * 应用实例工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月4日 下午10:41:27
 */
public class InstanceUtils {

	/**
	 * <p>
	 * 获取java进程PID
	 * </p>
	 *
	 * @return PID
	 * @author 皮锋
	 * @custom.date 2020年3月4日 下午10:48:07
	 */
	private static String getJavaPid() {
		String pid = null;
		RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
		String name = runtime.getName();
		int index = name.indexOf("@");
		if (index != -1) {
			pid = name.substring(0, index);
		}
		return pid;
	}

	/**
	 * <p>
	 * 获取应用实例ID
	 * </p>
	 *
	 * @return 应用实例ID
	 * @author 皮锋
	 * @custom.date 2020年3月4日 下午11:12:46
	 */
	public static String getInstanceId() {
		String mac = LocalMacUtils.getLocalMac();
		String rootUrl=ContextUtils.getRootUrl();
		System.out.println(rootUrl);
		return mac + getJavaPid();
	}

	/**
	 * <p>
	 * 获取应用实例名称
	 * </p>
	 *
	 * @return 应用实例名称
	 * @author 皮锋
	 * @custom.date 2020年3月4日 下午11:12:46
	 */
	public static String getInstanceName() {
		return ConfigLoader.monitoringProperties.getOwnProperties().getInstanceName();
	}

}
