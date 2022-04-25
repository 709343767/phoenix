package com.gitee.pifeng.monitoring.server.business.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorNetHistory;

import java.util.Date;

/**
 * <p>
 * 网络信息历史记录服务接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-03-20
 */
public interface INetHistoryService extends IService<MonitorNetHistory> {

    /**
     * <p>
     * 清理网络历史记录
     * </p>
     *
     * @param historyTime 时间点，清理这个时间点以前的数据
     * @return 清理记录数
     * @author 皮锋
     * @custom.date 2022/4/25 11:20
     */
    int clearHistoryData(Date historyTime);
}
