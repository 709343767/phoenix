package com.gitee.pifeng.monitoring.ui.business.web.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.constant.DateTimeStylesEnums;
import com.gitee.pifeng.monitoring.common.util.DateTimeUtils;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorHttpHistoryDao;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorHttpHistory;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorHttpHistoryService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.HttpAvgTimeChartVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.constant.TimeSelectConstants;
import com.gitee.pifeng.monitoring.ui.constant.WebResponseConstants;
import com.gitee.pifeng.monitoring.ui.core.CalculateDateTime;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * HTTP信息历史记录服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-04-12
 */
@Service
public class MonitorHttpHistoryServiceImpl extends ServiceImpl<IMonitorHttpHistoryDao, MonitorHttpHistory> implements IMonitorHttpHistoryService {

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
    @Override
    public HttpAvgTimeChartVo getAvgTimeChartInfo(Long id, String hostnameSource, String urlTarget, String method, String dateValue) {
        LambdaQueryWrapper<MonitorHttpHistory> monitorHttpHistoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        monitorHttpHistoryLambdaQueryWrapper.eq(MonitorHttpHistory::getHttpId, id);
        monitorHttpHistoryLambdaQueryWrapper.eq(MonitorHttpHistory::getHostnameSource, hostnameSource);
        monitorHttpHistoryLambdaQueryWrapper.eq(MonitorHttpHistory::getUrlTarget, urlTarget);
        monitorHttpHistoryLambdaQueryWrapper.eq(MonitorHttpHistory::getMethod, method);
        monitorHttpHistoryLambdaQueryWrapper.between(MonitorHttpHistory::getInsertTime,
                DateTimeUtils.string2Date(dateValue, DateTimeStylesEnums.YYYY_MM_DD),
                DateUtil.offsetDay(DateTimeUtils.string2Date(dateValue, DateTimeStylesEnums.YYYY_MM_DD), 1));
        List<MonitorHttpHistory> monitorHttpHistories = this.baseMapper.selectList(monitorHttpHistoryLambdaQueryWrapper);
        // 返回值
        HttpAvgTimeChartVo httpAvgTimeChartVo = new HttpAvgTimeChartVo();
        List<HttpAvgTimeChartVo.All> allList = Lists.newArrayList();
        List<HttpAvgTimeChartVo.Exc> excList = Lists.newArrayList();
        for (MonitorHttpHistory monitorHttpHistory : monitorHttpHistories) {
            // 所有
            HttpAvgTimeChartVo.All all = new HttpAvgTimeChartVo.All();
            all.setAvgTime(monitorHttpHistory.getAvgTime());
            all.setStatus(monitorHttpHistory.getStatus());
            all.setInsertTime(monitorHttpHistory.getInsertTime());
            allList.add(all);
            // 异常
            if (200 != monitorHttpHistory.getStatus()) {
                HttpAvgTimeChartVo.Exc exc = new HttpAvgTimeChartVo.Exc();
                exc.setAvgTime(monitorHttpHistory.getAvgTime());
                exc.setStatus(monitorHttpHistory.getStatus());
                exc.setInsertTime(monitorHttpHistory.getInsertTime());
                excList.add(exc);
            }
        }
        httpAvgTimeChartVo.setAllList(allList);
        httpAvgTimeChartVo.setExcList(excList);
        return httpAvgTimeChartVo;
    }

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
    @Override
    public LayUiAdminResultVo clearMonitorHttpHistory(Long id, String time) {
        // 时间为空
        if (StringUtils.isBlank(time)) {
            return LayUiAdminResultVo.ok(WebResponseConstants.REQUIRED_IS_NULL);
        }
        CalculateDateTime calculateDateTime = new CalculateDateTime(time).invoke();
        // 清理时间
        Date clearTime = calculateDateTime.getStartTime();
        // 清理所有时间点的数据，相当于清理当前时间前的数据
        if (StringUtils.equalsIgnoreCase(time, TimeSelectConstants.ALL)) {
            clearTime = new Date();
        }
        LambdaUpdateWrapper<MonitorHttpHistory> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(MonitorHttpHistory::getHttpId, id);
        lambdaUpdateWrapper.lt(MonitorHttpHistory::getInsertTime, clearTime);
        this.baseMapper.delete(lambdaUpdateWrapper);
        return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
    }
}
