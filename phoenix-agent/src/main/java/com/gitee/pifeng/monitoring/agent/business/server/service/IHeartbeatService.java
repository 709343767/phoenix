package com.gitee.pifeng.monitoring.agent.business.server.service;

import com.gitee.pifeng.monitoring.agent.annotation.TargetInf;
import com.gitee.pifeng.monitoring.agent.annotation.TargetMethod;
import com.gitee.pifeng.monitoring.common.dto.BaseResponsePackage;
import com.gitee.pifeng.monitoring.common.dto.HeartbeatPackage;

/**
 * <p>
 * 跟服务端相关的心跳服务接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月4日 下午2:12:45
 */
@TargetInf(inf = IHeartbeatService.class)
public interface IHeartbeatService {

    /**
     * <p>
     * 给服务端发心跳包
     * </p>
     *
     * @param heartbeatPackage 心跳包对象
     * @return {@link BaseResponsePackage}
     * @throws Exception 所有异常
     * @author 皮锋
     * @custom.date 2020年3月4日 下午2:16:07
     */
    @TargetMethod(method = "sendHeartbeatPackage")
    BaseResponsePackage sendHeartbeatPackage(HeartbeatPackage heartbeatPackage) throws Exception;

}
