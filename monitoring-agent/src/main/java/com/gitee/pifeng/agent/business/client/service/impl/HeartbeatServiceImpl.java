package com.gitee.pifeng.agent.business.client.service.impl;

import com.gitee.pifeng.agent.core.MethodExecuteHandler;
import com.gitee.pifeng.agent.business.client.service.IHeartbeatService;
import com.gitee.pifeng.common.dto.BaseResponsePackage;
import com.gitee.pifeng.common.dto.HeartbeatPackage;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 心跳服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月4日 下午1:44:54
 */
@Service
public class HeartbeatServiceImpl implements IHeartbeatService {

    /**
     * <p>
     * 处理心跳包
     * </p>
     *
     * @param heartbeatPackage 心跳包
     * @return {@link BaseResponsePackage}
     * @author 皮锋
     * @custom.date 2020年3月4日 下午1:47:28
     */
    @Override
    public BaseResponsePackage dealHeartbeatPackage(HeartbeatPackage heartbeatPackage) {
        // 把心跳包转发到服务端
        return MethodExecuteHandler.sendHeartbeatPackage2Server(heartbeatPackage);
    }

}
