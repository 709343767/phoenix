package com.gitee.pifeng.monitoring.server.business.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.common.dto.ServerPackage;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServerDiskHistory;

/**
 * <p>
 * 服务器磁盘历史记录服务层接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/4/5 15:30
 */
public interface IServerDiskHistoryService extends IService<MonitorServerDiskHistory> {

    /**
     * <p>
     * 把服务器磁盘历史记录添加到数据库
     * </p>
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2020/5/12 10:29
     */
    void operateServerDiskHistory(ServerPackage serverPackage);

}
