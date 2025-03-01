package com.gitee.pifeng.monitoring.server.business.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.common.dto.ServerPackage;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServerOs;

/**
 * <p>
 * 服务器操作系统信息服务层接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/4/5 14:21
 */
public interface IServerOsService extends IService<MonitorServerOs> {

    /**
     * <p>
     * 把服务器操作系统信息添加或更新到数据库
     * </p>
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2020/5/11 16:01
     */
    void operateServerOs(ServerPackage serverPackage);

}
