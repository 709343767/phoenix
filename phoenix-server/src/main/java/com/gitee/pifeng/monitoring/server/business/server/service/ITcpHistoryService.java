package com.gitee.pifeng.monitoring.server.business.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorTcpHistory;

import java.util.Date;

/**
 * <p>
 * TCP信息历史记录服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-03-16
 */
public interface ITcpHistoryService extends IService<MonitorTcpHistory> {

    /**
     * <p>
     * 清理TCP历史记录
     * </p>
     *
     * @param historyTime 时间点，清理这个时间点以前的数据
     * @return 清理记录数
     * @author 皮锋
     * @custom.date 2022/4/25 11:29
     */
    int clearHistoryData(Date historyTime);
}
