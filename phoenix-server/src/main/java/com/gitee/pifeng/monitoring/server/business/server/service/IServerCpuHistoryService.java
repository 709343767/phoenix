package com.gitee.pifeng.monitoring.server.business.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.common.dto.ServerPackage;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServerCpuHistory;

/**
 * <p>
 * 服务器CPU历史记录信息服务层接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/4/5 15:15
 */
public interface IServerCpuHistoryService extends IService<MonitorServerCpuHistory> {

    /**
     * <p>
     * 把服务器CPU历史记录添加到数据库
     * </p>
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2020/5/11 17:19
     */
    void operateServerCpuHistory(ServerPackage serverPackage);

}
