package com.gitee.pifeng.agent.business.client.service.impl;

import com.gitee.pifeng.agent.core.MethodExecuteHandler;
import com.gitee.pifeng.agent.business.client.service.IServerService;
import com.gitee.pifeng.common.dto.BaseResponsePackage;
import com.gitee.pifeng.common.dto.ServerPackage;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务器信息服务实现
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月7日 下午5:16:30
 */
@Service
public class ServerServiceImpl implements IServerService {

    /**
     * <p>
     * 处理服务器信息包
     * </p>
     *
     * @param serverPackage 服务器信息包
     * @return {@link BaseResponsePackage}
     * @author 皮锋
     * @custom.date 2020年3月7日 下午5:14:29
     */
    @Override
    public BaseResponsePackage dealServerPackage(ServerPackage serverPackage) {
        // 把服务器信息包转发到服务端
        return MethodExecuteHandler.sendServerPackage2Server(serverPackage);
    }

}
