package com.gitee.pifeng.monitoring.server.business.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.common.dto.ServerPackage;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServerLoadAverage;

/**
 * <p>
 * 服务器平均负载服务层接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/6/17 14:53
 */
public interface IServerLoadAverageService extends IService<MonitorServerLoadAverage> {

    /**
     * <p>
     * 把服务器平均负载信息添加或更新到数据库
     * </p>
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2022/6/17 14:50
     */
    void operateServerLoadAverage(ServerPackage serverPackage);

}
