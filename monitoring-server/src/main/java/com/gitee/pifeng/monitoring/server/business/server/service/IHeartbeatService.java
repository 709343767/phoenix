package com.gitee.pifeng.monitoring.server.business.server.service;

import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.common.dto.HeartbeatPackage;
import com.gitee.pifeng.monitoring.common.exception.NetException;

/**
 * <p>
 * 心跳服务接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/12 10:05
 */
public interface IHeartbeatService {

    /**
     * <p>
     * 处理心跳包
     * </p>
     *
     * @param heartbeatPackage 心跳包
     * @return {@link Result}
     * @throws NetException 获取网络信息异常
     * @author 皮锋
     * @custom.date 2020/3/12 10:18
     */
    Result dealHeartbeatPackage(HeartbeatPackage heartbeatPackage) throws NetException;
}
