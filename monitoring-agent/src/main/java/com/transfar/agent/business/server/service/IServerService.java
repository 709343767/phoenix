package com.transfar.agent.business.server.service;

import com.transfar.agent.business.annotation.TargetInf;
import com.transfar.agent.business.annotation.TargetMethod;
import com.transfar.common.dto.BaseResponsePackage;
import com.transfar.common.dto.ServerPackage;

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
     * @return BaseResponsePackage
     * @throws Exception 所有异常
     * @author 皮锋
     * @custom.date 2020年3月7日 下午5:24:47
     */
    // 加了注解的方法将会添加到命令执行器管理器，注册到bean容器
    @TargetMethod(method = "sendServerPackage")
    BaseResponsePackage sendServerPackage(ServerPackage serverPackage) throws Exception;
}
