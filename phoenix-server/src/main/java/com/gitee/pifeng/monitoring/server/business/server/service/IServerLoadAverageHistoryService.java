package com.gitee.pifeng.monitoring.server.business.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.common.dto.ServerPackage;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServerLoadAverageHistory;

/**
 * <p>
 * 服务器平均负载历史记录服务层接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/6/17 15:20
 */
public interface IServerLoadAverageHistoryService extends IService<MonitorServerLoadAverageHistory> {

    /**
     * <p>
     * 把服务器平均负载历史记录添加到数据库
     * </p>
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2022/6/17 14:50
     */
    void operateServerLoadAverageHistory(ServerPackage serverPackage);

}
