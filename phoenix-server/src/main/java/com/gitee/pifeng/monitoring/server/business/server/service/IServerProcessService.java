package com.gitee.pifeng.monitoring.server.business.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.common.dto.ServerPackage;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServerProcess;

/**
 * <p>
 * 服务器进程信息服务层接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/4/5 14:10
 */
public interface IServerProcessService extends IService<MonitorServerProcess> {

    /**
     * <p>
     * 把服务器进程信息添加或更新到数据库
     * </p>
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2021/9/15 14:49
     */
    void operateServerProcess(ServerPackage serverPackage);

}
