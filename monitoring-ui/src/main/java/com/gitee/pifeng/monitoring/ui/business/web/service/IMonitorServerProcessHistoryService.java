package com.gitee.pifeng.monitoring.ui.business.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorServerProcessHistory;
import com.gitee.pifeng.monitoring.ui.business.web.vo.ServerDetailPageServerProcessChartVo;

import java.util.List;

/**
 * <p>
 * 服务器进程历史记录服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-09-15
 */
public interface IMonitorServerProcessHistoryService extends IService<MonitorServerProcessHistory> {

    /**
     * <p>
     * 获取服务器详情页面服务器进程图表信息
     * </p>
     *
     * @param ip   服务器IP地址
     * @param time 时间
     * @return 服务器详情页面服务器进程图表信息表现层对象
     * @author 皮锋
     * @custom.date 2021/9/18 12:59
     */
    List<ServerDetailPageServerProcessChartVo> getServerDetailPageServerProcessChartInfo(String ip, String time);

}
