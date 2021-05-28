package com.gitee.pifeng.monitoring.server.business.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.common.dto.ServerPackage;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServer;

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

}
