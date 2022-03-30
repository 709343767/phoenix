package com.gitee.pifeng.monitoring.ui.business.web.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.constant.DateTimeStylesEnums;
import com.gitee.pifeng.monitoring.common.constant.ZeroOrOneConstants;
import com.gitee.pifeng.monitoring.common.util.DateTimeUtils;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorTcpHistoryDao;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorTcpHistory;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorTcpHistoryService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.TcpAvgTimeChartVo;
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
 * TCP信息历史记录服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-03-16
 */
@Service
public class MonitorTcpHistoryServiceImpl extends ServiceImpl<IMonitorTcpHistoryDao, MonitorTcpHistory> implements IMonitorTcpHistoryService {

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
    @Override
    public TcpAvgTimeChartVo getAvgTimeChartInfo(Long id, String hostnameSource, String hostnameTarget, Integer portTarget, String dateValue) {
        LambdaQueryWrapper<MonitorTcpHistory> monitorTcpHistoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        monitorTcpHistoryLambdaQueryWrapper.eq(MonitorTcpHistory::getTcpId, id);
        monitorTcpHistoryLambdaQueryWrapper.eq(MonitorTcpHistory::getHostnameSource, hostnameSource);
        monitorTcpHistoryLambdaQueryWrapper.eq(MonitorTcpHistory::getHostnameTarget, hostnameTarget);
        monitorTcpHistoryLambdaQueryWrapper.eq(MonitorTcpHistory::getPortTarget, portTarget);
        monitorTcpHistoryLambdaQueryWrapper.between(MonitorTcpHistory::getInsertTime,
                DateTimeUtils.string2Date(dateValue, DateTimeStylesEnums.YYYY_MM_DD),
                DateUtil.offsetDay(DateTimeUtils.string2Date(dateValue, DateTimeStylesEnums.YYYY_MM_DD), 1));
        List<MonitorTcpHistory> monitorTcpHistories = this.baseMapper.selectList(monitorTcpHistoryLambdaQueryWrapper);
        // 返回值
        TcpAvgTimeChartVo tcpAvgTimeChartVo = new TcpAvgTimeChartVo();
        List<TcpAvgTimeChartVo.All> allList = Lists.newArrayList();
        List<TcpAvgTimeChartVo.OffLine> offLineList = Lists.newArrayList();
        for (MonitorTcpHistory monitorTcpHistory : monitorTcpHistories) {
            // 所有
            TcpAvgTimeChartVo.All all = new TcpAvgTimeChartVo.All();
            all.setAvgTime(monitorTcpHistory.getAvgTime());
            all.setInsertTime(monitorTcpHistory.getInsertTime());
            allList.add(all);
            // 离线
            if (ZeroOrOneConstants.ZERO.equals(monitorTcpHistory.getStatus())) {
                TcpAvgTimeChartVo.OffLine offLine = new TcpAvgTimeChartVo.OffLine();
                offLine.setAvgTime(monitorTcpHistory.getAvgTime());
                offLine.setInsertTime(monitorTcpHistory.getInsertTime());
                offLineList.add(offLine);
            }
        }
        tcpAvgTimeChartVo.setAllList(allList);
        tcpAvgTimeChartVo.setOffLineList(offLineList);
        return tcpAvgTimeChartVo;
    }

    /**
     * <p>
     * 清理TCP监控历史数据
     * </p>
     *
     * @param id   TCP ID
     * @param time 时间
     * @return layUiAdmin响应对象：如果清理成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2022/3/30 12:51
     */
    @Override
    public LayUiAdminResultVo clearMonitorTcpHistory(String id, String time) {
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
        LambdaUpdateWrapper<MonitorTcpHistory> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(MonitorTcpHistory::getTcpId, id);
        lambdaUpdateWrapper.lt(MonitorTcpHistory::getInsertTime, clearTime);
        this.baseMapper.delete(lambdaUpdateWrapper);
        return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
    }
}
