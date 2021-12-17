package com.gitee.pifeng.monitoring.ui.business.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorServerMemoryHistory;
import com.gitee.pifeng.monitoring.ui.business.web.vo.ServerDetailPageServerMemoryChartVo;

import java.util.List;

/**
 * <p>
 * 服务器内存历史记录服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-01-24
 */
public interface IMonitorServerMemoryHistoryService extends IService<MonitorServerMemoryHistory> {

    /**
     * <p>
     * 获取服务器详情页面服务器内存图表信息
     * </p>
     *
     * @param ip   服务器IP地址
     * @param time 时间
     * @return 服务器详情页面服务器内存图表信息表现层对象
     * @author 皮锋
     * @custom.date 2020/10/21 12:42
     */
    List<ServerDetailPageServerMemoryChartVo> getServerDetailPageServerMemoryChartInfo(String ip, String time);

}
