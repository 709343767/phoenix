package com.gitee.pifeng.monitoring.server.business.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.common.dto.ServerPackage;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServerCpu;

/**
 * <p>
 * 服务器CPU信息服务层接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/2/4 9:14
 */
public interface IServerCpuService extends IService<MonitorServerCpu> {

    /**
     * <p>
     * 把服务器CPU信息添加或更新到数据库
     * </p>
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2021/1/25 9:48
     */
    void operateServerCpu(ServerPackage serverPackage);

}
