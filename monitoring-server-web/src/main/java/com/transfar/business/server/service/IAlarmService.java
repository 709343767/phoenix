package com.transfar.business.server.service;

import com.transfar.dto.AlarmPackage;

/**
 * <p>
 * 告警服务接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月10日 下午1:29:18
 */
public interface IAlarmService {

	/**
	 * <p>
	 * 处理心跳包
	 * </p>
	 *
	 * @author 皮锋
	 * @custom.date 2020年3月10日 下午1:33:55
	 * @param alarmPackage 心跳包
	 * @return Boolean
	 */
	Boolean dealAlarmPackage(AlarmPackage alarmPackage);

}
