package com.gitee.pifeng.agent.business.server.service.impl;

import com.gitee.pifeng.agent.constant.UrlConstants;
import com.gitee.pifeng.agent.business.server.service.IAlarmService;
import com.gitee.pifeng.agent.business.server.service.IHttpService;
import com.gitee.pifeng.common.dto.AlarmPackage;
import com.gitee.pifeng.common.dto.BaseResponsePackage;
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
        return this.httpService.sendHttpPost(alarmPackage.toJsonString(), UrlConstants.ALARM_URL);
    }

}
