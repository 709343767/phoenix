package com.gitee.pifeng.monitoring.agent.business.client.service.impl;

import com.gitee.pifeng.monitoring.agent.core.MethodExecuteHandler;
import com.gitee.pifeng.monitoring.agent.business.client.service.IAlarmService;
import com.gitee.pifeng.monitoring.common.dto.AlarmPackage;
import com.gitee.pifeng.monitoring.common.dto.BaseResponsePackage;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 告警服务实现
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月6日 下午3:11:11
 */
@Service
public class AlarmServiceImpl implements IAlarmService {

    /**
     * <p>
     * 处理告警包
     * </p>
     *
     * @param heartbeatPackage 告警包
     * @return {@link BaseResponsePackage}
     * @author 皮锋
     * @custom.date 2020年3月6日 下午3:09:12
     */
    @Override
    public BaseResponsePackage dealAlarmPackage(AlarmPackage heartbeatPackage) {
        // 把告警包转发到服务端
        return MethodExecuteHandler.sendAlarmPackage2Server(heartbeatPackage);
    }

}

	