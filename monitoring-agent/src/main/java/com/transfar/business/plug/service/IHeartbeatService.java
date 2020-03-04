package com.transfar.business.plug.service;

import com.transfar.dto.HeartbeatDto;

/**
 * <p>
 * 心跳服务接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月4日 下午1:42:57
 */
public interface IHeartbeatService {

    /**
     * <p>
     * 处理心跳包
     * </p>
     *
     * @param heartbeatDto 心跳对象
     * @return HeartbeatDto
     * @author 皮锋
     * @custom.date 2020年3月4日 下午1:47:28
     */
    HeartbeatDto dealHeartbeatPackage(HeartbeatDto heartbeatDto);

}
