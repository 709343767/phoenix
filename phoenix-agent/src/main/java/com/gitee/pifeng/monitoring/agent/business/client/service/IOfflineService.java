package com.gitee.pifeng.monitoring.agent.business.client.service;

import com.gitee.pifeng.monitoring.common.dto.BaseResponsePackage;
import com.gitee.pifeng.monitoring.common.dto.OfflinePackage;

/**
 * <p>
 * 下线信息服务接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/15 22:23
 */
public interface IOfflineService {

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
    BaseResponsePackage dealOfflinePackage(OfflinePackage offlinePackage);

}
