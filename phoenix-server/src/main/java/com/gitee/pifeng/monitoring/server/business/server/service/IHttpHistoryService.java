package com.gitee.pifeng.monitoring.server.business.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorHttpHistory;

import java.util.Date;

/**
 * <p>
 * HTTP信息历史记录服务接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/4/13 9:06
 */
public interface IHttpHistoryService extends IService<MonitorHttpHistory> {

    /**
     * <p>
     * 清理HTTP历史记录
     * </p>
     *
     * @param historyTime 时间点，清理这个时间点以前的数据
     * @return 清理记录数
     * @author 皮锋
     * @custom.date 2022/4/25 11:30
     */
    int clearHistoryData(Date historyTime);
}
