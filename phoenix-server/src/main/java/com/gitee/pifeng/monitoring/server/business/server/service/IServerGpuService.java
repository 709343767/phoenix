package com.gitee.pifeng.monitoring.server.business.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.common.dto.ServerPackage;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServerGpu;

/**
 * <p>
 * 服务器GPU信息服务层接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2025-06-09
 */
public interface IServerGpuService extends IService<MonitorServerGpu> {

    /**
     * <p>
     * 把服务器GPU信息添加或更新到数据库
     * </p>
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2025/6/9 10:27
     */
    void operateServerGpu(ServerPackage serverPackage);

}
