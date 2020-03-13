package com.transfar.util;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

import org.apache.commons.lang3.StringUtils;

import com.transfar.business.core.ConfigLoader;

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
	 * 应用实例ID
	 */
	private static String instanceId;

	/**
	 * 应用实例名字
	 */
	private static String instanceName;

	/**
	 * <p>
	 * 获取java进程PID
	 * </p>
	 *
	 * @return PID
	 * @author 皮锋
	 * @custom.date 2020年3月4日 下午10:48:07
	 */
	public static String getJavaPid() {
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
		if (StringUtils.isNotEmpty(instanceId)) {
			return instanceId;
		}
		// String mac = LocalMacUtils.getLocalMac();
		String rootUrl = ContextUtils.getRootUrl();
		instanceId = MD5Utils.encrypt16(rootUrl);
		return instanceId;
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
		if (StringUtils.isNotEmpty(instanceName)) {
			return instanceName;
		}
		instanceName = ConfigLoader.monitoringProperties.getOwnProperties().getInstanceName();
		return instanceName;
	}

}
