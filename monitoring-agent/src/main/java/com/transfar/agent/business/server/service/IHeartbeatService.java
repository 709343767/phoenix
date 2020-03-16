package com.transfar.agent.business.server.service;

import com.transfar.agent.business.annotation.TargetInf;
import com.transfar.agent.business.annotation.TargetMethod;
import com.transfar.common.dto.BaseResponsePackage;
import com.transfar.common.dto.HeartbeatPackage;

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
     * @return BaseResponsePackage
     * @throws Exception 所有异常
     * @author 皮锋
     * @custom.date 2020年3月4日 下午2:16:07
     */
    // 加了注解的方法将会添加到命令执行器管理器，注册到bean容器
    @TargetMethod(method = "sendHeartbeatPackage")
    BaseResponsePackage sendHeartbeatPackage(HeartbeatPackage heartbeatPackage) throws Exception;

}
