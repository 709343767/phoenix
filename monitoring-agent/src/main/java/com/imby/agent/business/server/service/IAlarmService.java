package com.imby.agent.business.server.service;

import com.imby.agent.business.annotation.TargetInf;
import com.imby.agent.business.annotation.TargetMethod;
import com.imby.common.dto.AlarmPackage;
import com.imby.common.dto.BaseResponsePackage;

/**
 * <p>
 * 跟服务端相关的告警服务接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月6日 下午3:25:05
 */
@TargetInf(inf = IAlarmService.class)
public interface IAlarmService {

    /**
     * <p>
     * 给服务端发告警包
     * </p>
     *
     * @param alarmPackage 告警包
     * @return {@link BaseResponsePackage}
     * @throws Exception 所有异常
     * @author 皮锋
     * @custom.date 2020年3月6日 下午3:27:17
     */
    @TargetMethod(method = "sendAlarmPackage")
    BaseResponsePackage sendAlarmPackage(AlarmPackage alarmPackage) throws Exception;
}
