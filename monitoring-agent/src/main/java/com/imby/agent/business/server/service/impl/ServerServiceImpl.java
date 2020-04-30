package com.imby.agent.business.server.service.impl;

import com.imby.agent.business.constant.Urlconstants;
import com.imby.agent.business.server.service.IHttpService;
import com.imby.agent.business.server.service.IServerService;
import com.imby.common.dto.BaseResponsePackage;
import com.imby.common.dto.ServerPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 跟服务端相关的服务器信息服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月7日 下午5:27:43
 */
@Service
public class ServerServiceImpl implements IServerService {

    /**
     * 跟服务端相关的HTTP服务接口
     */
    @Autowired
    private IHttpService httpService;

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
    @Override
    public BaseResponsePackage sendServerPackage(ServerPackage serverPackage) throws Exception {
        return this.httpService.sendHttpPost(serverPackage.toJsonString(), Urlconstants.SERVER_URL);
    }

}
