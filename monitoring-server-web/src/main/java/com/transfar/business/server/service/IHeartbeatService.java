package com.transfar.business.server.service;

import com.transfar.dto.HeartbeatPackage;

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
     * @return boolean
     * @author 皮锋
     * @custom.date 2020/3/12 10:18
     */
    boolean dealHeartbeatPackage(HeartbeatPackage heartbeatPackage);
}
