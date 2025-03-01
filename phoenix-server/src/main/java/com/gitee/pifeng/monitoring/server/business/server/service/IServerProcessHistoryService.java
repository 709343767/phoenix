package com.gitee.pifeng.monitoring.server.business.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.common.dto.ServerPackage;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServerProcessHistory;

/**
 * <p>
 * 服务器进程历史记录信息服务层接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/4/5 15:34
 */
public interface IServerProcessHistoryService extends IService<MonitorServerProcessHistory> {

    /**
     * <p>
     * 把服务器进程历史记录添加到数据库
     * </p>
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2021/9/15 15:04
     */
    void operateServerProcessHistory(ServerPackage serverPackage);

}
