package com.imby.server.business.server.service;

import com.imby.common.domain.Result;
import com.imby.common.dto.AlarmPackage;

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
     * 处理告警包
     * </p>
     *
     * @param alarmPackage 告警包
     * @return {@link Result}
     * @author 皮锋
     * @custom.date 2020年3月10日 下午1:33:55
     */
    Result dealAlarmPackage(AlarmPackage alarmPackage);

}
