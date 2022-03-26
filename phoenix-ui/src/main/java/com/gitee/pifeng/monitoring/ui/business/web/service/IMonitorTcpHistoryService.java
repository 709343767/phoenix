package com.gitee.pifeng.monitoring.ui.business.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorTcpHistory;
import com.gitee.pifeng.monitoring.ui.business.web.vo.TcpAvgTimeChartVo;

/**
 * <p>
 * TCP信息历史记录服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-03-16
 */
public interface IMonitorTcpHistoryService extends IService<MonitorTcpHistory> {

    /**
     * <p>
     * 获取TCP连接耗时图表信息
     * </p>
     *
     * @param id             TCP ID
     * @param hostnameSource 主机名（来源）
     * @param hostnameTarget 主机名（目的地）
     * @param portTarget     端口号
     * @param dateValue      时间
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2022/3/17 21:37
     */
    TcpAvgTimeChartVo getAvgTimeChartInfo(Long id, String hostnameSource, String hostnameTarget, Integer portTarget, String dateValue);
}
