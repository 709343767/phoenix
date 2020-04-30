package com.imby.agent.business.server.service.impl;

import com.imby.agent.business.constant.Urlconstants;
import com.imby.agent.business.server.service.IHeartbeatService;
import com.imby.agent.business.server.service.IHttpService;
import com.imby.common.dto.BaseResponsePackage;
import com.imby.common.dto.HeartbeatPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 跟服务端相关的心跳服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月4日 下午2:13:59
 */
@Service
public class HeartbeatServiceImpl implements IHeartbeatService {

    /**
     * 跟服务端相关的HTTP服务接口
     */
    @Autowired
    private IHttpService httpService;

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
    @Override
    public BaseResponsePackage sendHeartbeatPackage(HeartbeatPackage heartbeatPackage) throws Exception {
        return this.httpService.sendHttpPost(heartbeatPackage.toJsonString(), Urlconstants.HEARTBEAT_URL);
    }

}
