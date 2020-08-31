package com.imby.server.business.server.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.imby.server.business.server.entity.MonitorNet;

/**
 * <p>
 * 网络信息服务接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/31 17:20
 */
public interface INetService {

    /**
     * <p>
     * 更新网络信息
     * </p>
     *
     * @param monitorNet          网络信息
     * @param lambdaUpdateWrapper 更新条件
     * @return 更新记录数
     * @author 皮锋
     * @custom.date 2020/8/31 17:33
     */
    int updateNet(MonitorNet monitorNet, LambdaUpdateWrapper<MonitorNet> lambdaUpdateWrapper);

}
