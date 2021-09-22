package com.gitee.pifeng.monitoring.ui.business.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorServerProcessHistoryDao;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorServerProcessHistory;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorServerProcessHistoryService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.ServerDetailPageServerProcessChartVo;
import com.gitee.pifeng.monitoring.ui.core.CalculateDateTime;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务器进程历史记录服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-09-15
 */
@Service
public class MonitorServerProcessHistoryServiceImpl extends ServiceImpl<IMonitorServerProcessHistoryDao, MonitorServerProcessHistory> implements IMonitorServerProcessHistoryService {

    /**
     * 服务器进程历史记录信息数据访问对象
     */
    @Autowired
    private IMonitorServerProcessHistoryDao monitorServerProcessHistoryDao;

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
    @Override
    public List<ServerDetailPageServerProcessChartVo> getServerDetailPageServerProcessChartInfo(String ip, String time) {
        // 计算时间
        CalculateDateTime calculateDateTime = new CalculateDateTime(time).invoke();
        // 开始时间
        Date startTime = calculateDateTime.getStartTime();
        // 结束时间
        Date endTime = calculateDateTime.getEndTime();
        // 查询数据库
        LambdaQueryWrapper<MonitorServerProcessHistory> monitorServerProcessHistoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        monitorServerProcessHistoryLambdaQueryWrapper.eq(MonitorServerProcessHistory::getIp, ip);
        if (startTime != null && endTime != null) {
            monitorServerProcessHistoryLambdaQueryWrapper.between(MonitorServerProcessHistory::getInsertTime, startTime, endTime);
        }
        List<MonitorServerProcessHistory> monitorServerProcessHistories = this.monitorServerProcessHistoryDao.selectList(monitorServerProcessHistoryLambdaQueryWrapper);
        // 返回值
        List<ServerDetailPageServerProcessChartVo> serverDetailPageServerProcessChartVos = Lists.newArrayList();
        for (MonitorServerProcessHistory monitorServerProcessHistory : monitorServerProcessHistories) {
            ServerDetailPageServerProcessChartVo serverDetailPageServerProcessChartVo = ServerDetailPageServerProcessChartVo.builder().build();
            serverDetailPageServerProcessChartVo.setProcessNum(monitorServerProcessHistory.getProcessNum());
            serverDetailPageServerProcessChartVo.setInsertTime(monitorServerProcessHistory.getInsertTime());
            serverDetailPageServerProcessChartVos.add(serverDetailPageServerProcessChartVo);
        }
        return serverDetailPageServerProcessChartVos;
    }

}
