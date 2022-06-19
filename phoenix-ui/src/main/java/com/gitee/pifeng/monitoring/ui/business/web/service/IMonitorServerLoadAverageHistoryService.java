package com.gitee.pifeng.monitoring.ui.business.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorServerLoadAverageHistory;
import com.gitee.pifeng.monitoring.ui.business.web.vo.ServerDetailPageServerLoadAverageChartVo;

import java.util.List;

/**
 * <p>
 * 服务器平均负载历史记录服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-06-17
 */
public interface IMonitorServerLoadAverageHistoryService extends IService<MonitorServerLoadAverageHistory> {

    /**
     * <p>
     * 获取服务器详情页面服务器平均负载图表信息
     * </p>
     *
     * @param ip   服务器IP地址
     * @param time 时间
     * @return 服务器详情页面服务器平均负载图表信息表现层对象
     * @author 皮锋
     * @custom.date 2022/6/19 14:54
     */
    List<ServerDetailPageServerLoadAverageChartVo> getServerDetailPageServerLoadAverageChartInfo(String ip, String time);
}
