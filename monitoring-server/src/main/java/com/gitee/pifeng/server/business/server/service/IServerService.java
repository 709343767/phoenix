package com.gitee.pifeng.server.business.server.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.common.domain.Result;
import com.gitee.pifeng.common.dto.ServerPackage;
import com.gitee.pifeng.server.business.server.entity.MonitorServer;

/**
 * <p>
 * 服务器信息服务层接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/23 15:22
 */
public interface IServerService extends IService<MonitorServer> {

    /**
     * <p>
     * 处理服务器信息包
     * </p>
     *
     * @param serverPackage 服务器信息包
     * @return {@link Result}
     * @author 皮锋
     * @custom.date 2020/3/23 15:29
     */
    Result dealServerPackage(ServerPackage serverPackage);

    /**
     * <p>
     * 更新服务器信息
     * </p>
     *
     * @param monitorServer       服务器
     * @param lambdaUpdateWrapper 更新条件
     * @return 更新记录数
     * @author 皮锋
     * @custom.date 2020/6/29 15:27
     */
    int updateInstance(MonitorServer monitorServer, LambdaUpdateWrapper<MonitorServer> lambdaUpdateWrapper);

}
