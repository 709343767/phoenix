package com.gitee.pifeng.monitoring.agent.business.server.service;

import com.gitee.pifeng.monitoring.common.dto.BaseResponsePackage;
import com.gitee.pifeng.monitoring.common.dto.OfflinePackage;
import com.gitee.pifeng.monitoring.common.web.annotation.TargetInf;
import com.gitee.pifeng.monitoring.common.web.annotation.TargetMethod;

/**
 * <p>
 * 下线信息服务接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/15 22:23
 */
@TargetInf
public interface IOfflineService {

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
    @TargetMethod
    BaseResponsePackage sendOfflinePackage(OfflinePackage offlinePackage) throws Exception;

}
