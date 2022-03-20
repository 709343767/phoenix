package com.gitee.pifeng.monitoring.ui.business.web.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.constant.DateTimeStylesEnums;
import com.gitee.pifeng.monitoring.common.constant.ZeroOrOneConstants;
import com.gitee.pifeng.monitoring.common.util.DateTimeUtils;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorNetHistoryDao;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorNetHistory;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorNetHistoryService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.NetworkAvgTimeChartVo;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 网络信息历史记录服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-03-20
 */
@Service
public class MonitorNetHistoryServiceImpl extends ServiceImpl<IMonitorNetHistoryDao, MonitorNetHistory> implements IMonitorNetHistoryService {

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
    @Override
    public NetworkAvgTimeChartVo getAvgTimeChartInfo(Long id, String ipSource, String ipTarget, String dateValue) {
        LambdaQueryWrapper<MonitorNetHistory> monitorNetHistoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        monitorNetHistoryLambdaQueryWrapper.eq(MonitorNetHistory::getNetId, id);
        monitorNetHistoryLambdaQueryWrapper.eq(MonitorNetHistory::getIpSource, ipSource);
        monitorNetHistoryLambdaQueryWrapper.eq(MonitorNetHistory::getIpTarget, ipTarget);
        monitorNetHistoryLambdaQueryWrapper.between(MonitorNetHistory::getInsertTime,
                DateTimeUtils.string2Date(dateValue, DateTimeStylesEnums.YYYY_MM_DD),
                DateUtil.offsetDay(DateTimeUtils.string2Date(dateValue, DateTimeStylesEnums.YYYY_MM_DD), 1));
        List<MonitorNetHistory> monitorNetHistories = this.baseMapper.selectList(monitorNetHistoryLambdaQueryWrapper);
        // 返回值
        NetworkAvgTimeChartVo networkAvgTimeChartVo = new NetworkAvgTimeChartVo();
        List<NetworkAvgTimeChartVo.All> allList = Lists.newArrayList();
        List<NetworkAvgTimeChartVo.OffLine> offLineList = Lists.newArrayList();
        for (MonitorNetHistory monitorNetHistory : monitorNetHistories) {
            // 所有
            NetworkAvgTimeChartVo.All all = new NetworkAvgTimeChartVo.All();
            all.setAvgTime(monitorNetHistory.getAvgTime());
            all.setInsertTime(monitorNetHistory.getInsertTime());
            allList.add(all);
            // 离线
            if (ZeroOrOneConstants.ZERO.equals(monitorNetHistory.getStatus())) {
                NetworkAvgTimeChartVo.OffLine offLine = new NetworkAvgTimeChartVo.OffLine();
                offLine.setAvgTime(monitorNetHistory.getAvgTime());
                offLine.setInsertTime(monitorNetHistory.getInsertTime());
                offLineList.add(offLine);
            }
        }
        networkAvgTimeChartVo.setAllList(allList);
        networkAvgTimeChartVo.setOffLineList(offLineList);
        return networkAvgTimeChartVo;
    }
}
