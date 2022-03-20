package com.gitee.pifeng.monitoring.ui.business.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorTcpIpHistory;
import com.gitee.pifeng.monitoring.ui.business.web.vo.TcpIpAvgTimeChartVo;

/**
 * <p>
 * TCP/IP信息历史记录服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-03-16
 */
public interface IMonitorTcpIpHistoryService extends IService<MonitorTcpIpHistory> {

    /**
     * <p>
     * 获取TCPIP连接耗时图表信息
     * </p>
     *
     * @param id         TCP/IP ID
     * @param ipSource   IP地址（来源）
     * @param ipTarget   IP地址（目的地）
     * @param portTarget 端口号
     * @param protocol   协议
     * @param dateValue  时间
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2022/3/17 21:37
     */
    TcpIpAvgTimeChartVo getAvgTimeChartInfo(Long id, String ipSource, String ipTarget, Integer portTarget, String protocol, String dateValue);
}
