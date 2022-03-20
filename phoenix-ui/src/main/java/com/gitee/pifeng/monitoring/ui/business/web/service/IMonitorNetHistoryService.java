package com.gitee.pifeng.monitoring.ui.business.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorNetHistory;
import com.gitee.pifeng.monitoring.ui.business.web.vo.NetworkAvgTimeChartVo;

/**
 * <p>
 * 网络信息历史记录服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-03-20
 */
public interface IMonitorNetHistoryService extends IService<MonitorNetHistory> {

    /**
     * <p>
     * 获取PING耗时图表信息
     * </p>
     *
     * @param id        TCP/IP ID
     * @param ipSource  IP地址（来源）
     * @param ipTarget  IP地址（目的地）
     * @param dateValue 时间
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2022/3/17 21:37
     */
    NetworkAvgTimeChartVo getAvgTimeChartInfo(Long id, String ipSource, String ipTarget, String dateValue);
}
