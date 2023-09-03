package com.gitee.pifeng.monitoring.agent.business.client.service.impl;

import com.gitee.pifeng.monitoring.agent.business.client.service.IOfflineService;
import com.gitee.pifeng.monitoring.agent.core.MethodExecuteHandler;
import com.gitee.pifeng.monitoring.common.dto.BaseResponsePackage;
import com.gitee.pifeng.monitoring.common.dto.OfflinePackage;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 下线信息服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/15 22:23
 */
@Service
public class OfflineServiceImpl implements IOfflineService {

    /**
     * <p>
     * 处理下线信息包
     * </p>
     *
     * @param offlinePackage 下线信息包
     * @return {@link BaseResponsePackage}
     * @author 皮锋
     * @custom.date 2020年3月7日 下午5:14:29
     */
    @Override
    public BaseResponsePackage dealOfflinePackage(OfflinePackage offlinePackage) {
        // 把下线信息包转发到服务端
        return MethodExecuteHandler.sendOfflinePackage2Server(offlinePackage);
    }

}
