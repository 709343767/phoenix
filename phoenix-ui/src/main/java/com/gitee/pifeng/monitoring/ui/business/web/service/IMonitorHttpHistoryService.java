package com.gitee.pifeng.monitoring.ui.business.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorHttpHistory;
import com.gitee.pifeng.monitoring.ui.business.web.vo.HttpAvgTimeChartVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;

/**
 * <p>
 * HTTP信息历史记录服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-04-12
 */
public interface IMonitorHttpHistoryService extends IService<MonitorHttpHistory> {

    /**
     * <p>
     * 获取访问耗时图表信息
     * </p>
     *
     * @param id             HTTP ID
     * @param hostnameSource 主机名（来源）
     * @param urlTarget      URL地址（目的地）
     * @param method         请求方法
     * @param dateValue      时间
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2022/3/17 21:37
     */
    HttpAvgTimeChartVo getAvgTimeChartInfo(Long id, String hostnameSource, String urlTarget, String method, String dateValue);

    /**
     * <p>
     * 清理HTTP监控历史数据
     * </p>
     *
     * @param id   HTTP ID
     * @param time 时间
     * @return layUiAdmin响应对象：如果清理成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2021/7/20 20:52
     */
    LayUiAdminResultVo clearMonitorHttpHistory(Long id, String time);
}
