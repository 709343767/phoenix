package com.gitee.pifeng.monitoring.agent.business.server.service.impl;

import com.gitee.pifeng.monitoring.agent.business.server.service.IAlarmService;
import com.gitee.pifeng.monitoring.agent.business.server.service.IHttpService;
import com.gitee.pifeng.monitoring.agent.constant.UrlConstants;
import com.gitee.pifeng.monitoring.agent.core.AgentPackageConstructor;
import com.gitee.pifeng.monitoring.common.dto.AlarmPackage;
import com.gitee.pifeng.monitoring.common.dto.BaseResponsePackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 跟服务端相关的告警服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月6日 下午3:30:01
 */
@Service
public class AlarmServiceImpl implements IAlarmService {

    /**
     * 包构造器接口
     */
    @Autowired
    private AgentPackageConstructor agentPackageConstructor;

    /**
     * 跟服务端相关的HTTP服务接口
     */
    @Autowired
    private IHttpService httpService;

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
    @Override
    public BaseResponsePackage sendAlarmPackage(AlarmPackage alarmPackage) throws Exception {
        // 添加链路信息
        alarmPackage.setChain(this.agentPackageConstructor.getChain(alarmPackage));
        BaseResponsePackage baseResponsePackage = this.httpService.sendHttpPost(alarmPackage.toJsonString(), UrlConstants.ALARM_URL);
        // 添加链路信息
        baseResponsePackage.setChain(this.agentPackageConstructor.getChain(baseResponsePackage));
        return baseResponsePackage;
    }

}
