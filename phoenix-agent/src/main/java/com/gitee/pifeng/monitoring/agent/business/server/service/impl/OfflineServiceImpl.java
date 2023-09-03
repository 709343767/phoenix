package com.gitee.pifeng.monitoring.agent.business.server.service.impl;

import com.gitee.pifeng.monitoring.agent.business.server.service.IHttpService;
import com.gitee.pifeng.monitoring.agent.business.server.service.IOfflineService;
import com.gitee.pifeng.monitoring.agent.constant.UrlConstants;
import com.gitee.pifeng.monitoring.agent.core.AgentPackageConstructor;
import com.gitee.pifeng.monitoring.common.dto.BaseResponsePackage;
import com.gitee.pifeng.monitoring.common.dto.OfflinePackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 跟服务端相关的下线信息服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/15 22:18
 */
@Service
public class OfflineServiceImpl implements IOfflineService {

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
     * 给服务端发下线信息包
     * </p>
     *
     * @param offlinePackage 下线信息包
     * @return {@link BaseResponsePackage}
     * @throws Exception 所有异常
     * @author 皮锋
     * @custom.date 2020年3月7日 下午5:24:47
     */
    @Override
    public BaseResponsePackage sendOfflinePackage(OfflinePackage offlinePackage) throws Exception {
        // 添加链路信息
        offlinePackage.setChain(this.agentPackageConstructor.getChain(offlinePackage));
        BaseResponsePackage baseResponsePackage = this.httpService.sendHttpPost(offlinePackage.toJsonString(), UrlConstants.OFFLINE_URL);
        // 添加链路信息
        baseResponsePackage.setChain(this.agentPackageConstructor.getChain(baseResponsePackage));
        return baseResponsePackage;
    }

}
