package com.transfar.util;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

import org.apache.commons.lang3.StringUtils;

import com.transfar.core.ConfigLoader;

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
	 * 应用ID
	 */
	private static String instanceId;

	/**
	 * 应用名字
	 */
	private static String insranceName;

	/**
	 * <p>
	 * 获取java进程PID
	 * </p>
	 *
	 * @author 皮锋
	 * @custom.date 2020年3月4日 下午10:48:07
	 * @return PID
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
	 * @author 皮锋
	 * @custom.date 2020年3月4日 下午11:12:46
	 * @return 应用实例ID
	 */
	public static String getInstanceId() {
		if (StringUtils.isNotEmpty(instanceId)) {
			return instanceId;
		}
		String mac = LocalMacUtils.getLocalMac();
		instanceId = mac + getInstanceName();
		return instanceId;
	}

	/**
	 * <p>
	 * 获取应用实例名字
	 * </p>
	 *
	 * @author 皮锋
	 * @custom.date 2020年3月6日 下午9:16:00
	 * @return 应用实例名字
	 */
	public static String getInstanceName() {
		if (StringUtils.isNotEmpty(insranceName)) {
			return insranceName;
		}
		insranceName = ConfigLoader.monitoringProperties.getOwnProperties().getInstanceName();
		return insranceName;
	}

	public static void main(String[] args) throws InterruptedException {
		for (int i = 0; i < 100; i++) {
			String id = getInstanceId();
			System.out.println("当前应用的ID为：" + id);
			Thread.sleep(5 * 1000);
		}
	}
}
