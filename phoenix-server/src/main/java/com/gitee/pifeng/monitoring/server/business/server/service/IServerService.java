package com.gitee.pifeng.monitoring.server.business.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.common.dto.ServerPackage;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServer;

import java.util.Date;

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

    /**
     * <p>
     * 把服务器信息添加或更新到数据库
     * </p>
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2021/1/21 13:21
     */
    void operateServer(ServerPackage serverPackage);

    /**
     * <p>
     * 清理服务器历史记录
     * </p>
     *
     * @param historyTime 时间点，清理这个时间点以前的数据
     * @return 清理记录数
     * @author 皮锋
     * @custom.date 2021/12/9 20:59
     */
    int clearHistoryData(Date historyTime);
}
