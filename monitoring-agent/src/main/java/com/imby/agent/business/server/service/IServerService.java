package com.imby.agent.business.server.service;

import com.imby.agent.business.annotation.TargetInf;
import com.imby.agent.business.annotation.TargetMethod;
import com.imby.common.dto.BaseResponsePackage;
import com.imby.common.dto.ServerPackage;

/**
 * <p>
 * 跟服务端相关的服务器信息服务接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月7日 下午5:21:44
 */
@TargetInf(inf = IServerService.class)
public interface IServerService {

    /**
     * <p>
     * 给服务端发服务器信息包
     * </p>
     *
     * @param serverPackage 服务器信息包
     * @return {@link BaseResponsePackage}
     * @throws Exception 所有异常
     * @author 皮锋
     * @custom.date 2020年3月7日 下午5:24:47
     */
    @TargetMethod(method = "sendServerPackage")
    BaseResponsePackage sendServerPackage(ServerPackage serverPackage) throws Exception;
}
