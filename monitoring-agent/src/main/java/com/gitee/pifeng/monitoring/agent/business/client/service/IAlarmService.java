package com.gitee.pifeng.monitoring.agent.business.client.service;

import com.gitee.pifeng.monitoring.common.dto.AlarmPackage;
import com.gitee.pifeng.monitoring.common.dto.BaseResponsePackage;

/**
 * <p>
 * 告警服务接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月6日 下午3:07:24
 */
public interface IAlarmService {

	/**
	 * <p>
	 * 处理告警包
	 * </p>
	 *
	 * @author 皮锋
	 * @custom.date 2020年3月6日 下午3:09:12
	 * @param heartbeatPackage 告警包
	 * @return {@link BaseResponsePackage}
	 */
	BaseResponsePackage dealAlarmPackage(AlarmPackage heartbeatPackage);

}
