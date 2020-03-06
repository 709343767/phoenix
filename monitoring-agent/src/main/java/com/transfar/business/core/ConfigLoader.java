package com.transfar.business.core;

import com.transfar.AgentApplication;
import com.transfar.properties.MonitoringProperties;

/**
 * <p>
 * 加载配置
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月4日 下午2:36:18
 */
public class ConfigLoader {

	/**
	 * 监控配置
	 */
	public static MonitoringProperties monitoringProperties = getConfig();

	/**
	 * <p>
	 * 获取监控配置
	 * </p>
	 *
	 * @author 皮锋
	 * @custom.date 2020年3月6日 下午12:11:43
	 * @return MonitoringProperties
	 */
	private static MonitoringProperties getConfig() {
		return AgentApplication.applicationContext.getBean(MonitoringProperties.class);
	}

}
