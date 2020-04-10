package com.transfar.agent.business.server.service;

import com.transfar.agent.business.annotation.TargetInf;
import com.transfar.agent.business.annotation.TargetMethod;
import com.transfar.common.dto.AlarmPackage;
import com.transfar.common.dto.BaseResponsePackage;

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
    // 加了注解的方法将会添加到命令执行器管理器，注册到bean容器
    @TargetMethod(method = "sendAlarmPackage")
    BaseResponsePackage sendAlarmPackage(AlarmPackage alarmPackage) throws Exception;
}
