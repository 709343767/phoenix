package com.gitee.pifeng.monitoring.ui.business.web.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.constant.DateTimeStylesEnums;
import com.gitee.pifeng.monitoring.common.constant.ZeroOrOneConstants;
import com.gitee.pifeng.monitoring.common.util.DateTimeUtils;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorTcpIpHistoryDao;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorTcpIpHistory;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorTcpIpHistoryService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.TcpIpAvgTimeChartVo;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * TCP/IP信息历史记录服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-03-16
 */
@Service
public class MonitorTcpIpHistoryServiceImpl extends ServiceImpl<IMonitorTcpIpHistoryDao, MonitorTcpIpHistory> implements IMonitorTcpIpHistoryService {

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
    @Override
    public TcpIpAvgTimeChartVo getAvgTimeChartInfo(Long id, String ipSource, String ipTarget, Integer portTarget, String protocol, String dateValue) {
        LambdaQueryWrapper<MonitorTcpIpHistory> monitorTcpIpHistoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        monitorTcpIpHistoryLambdaQueryWrapper.eq(MonitorTcpIpHistory::getTcpipId, id);
        monitorTcpIpHistoryLambdaQueryWrapper.eq(MonitorTcpIpHistory::getIpSource, ipSource);
        monitorTcpIpHistoryLambdaQueryWrapper.eq(MonitorTcpIpHistory::getIpTarget, ipTarget);
        monitorTcpIpHistoryLambdaQueryWrapper.eq(MonitorTcpIpHistory::getPortTarget, portTarget);
        monitorTcpIpHistoryLambdaQueryWrapper.eq(MonitorTcpIpHistory::getProtocol, protocol);
        monitorTcpIpHistoryLambdaQueryWrapper.between(MonitorTcpIpHistory::getInsertTime,
                DateTimeUtils.string2Date(dateValue, DateTimeStylesEnums.YYYY_MM_DD),
                DateUtil.offsetDay(DateTimeUtils.string2Date(dateValue, DateTimeStylesEnums.YYYY_MM_DD), 1));
        List<MonitorTcpIpHistory> monitorTcpIpHistories = this.baseMapper.selectList(monitorTcpIpHistoryLambdaQueryWrapper);
        // 返回值
        TcpIpAvgTimeChartVo tcpIpAvgTimeChartVo = new TcpIpAvgTimeChartVo();
        List<TcpIpAvgTimeChartVo.All> allList = Lists.newArrayList();
        List<TcpIpAvgTimeChartVo.OffLine> offLineList = Lists.newArrayList();
        for (MonitorTcpIpHistory monitorTcpIpHistory : monitorTcpIpHistories) {
            // 所有
            TcpIpAvgTimeChartVo.All all = new TcpIpAvgTimeChartVo.All();
            all.setAvgTime(monitorTcpIpHistory.getAvgTime());
            all.setInsertTime(monitorTcpIpHistory.getInsertTime());
            allList.add(all);
            // 离线
            if (ZeroOrOneConstants.ZERO.equals(monitorTcpIpHistory.getStatus())) {
                TcpIpAvgTimeChartVo.OffLine offLine = new TcpIpAvgTimeChartVo.OffLine();
                offLine.setAvgTime(monitorTcpIpHistory.getAvgTime());
                offLine.setInsertTime(monitorTcpIpHistory.getInsertTime());
                offLineList.add(offLine);
            }
        }
        tcpIpAvgTimeChartVo.setAllList(allList);
        tcpIpAvgTimeChartVo.setOffLineList(offLineList);
        return tcpIpAvgTimeChartVo;
    }
}
