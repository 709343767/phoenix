package com.gitee.pifeng.monitoring.ui.business.web.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorServerCpuHistoryDao;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorServerCpuHistory;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorServerCpuHistoryService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.ServerDetailPageServerCpuChartVo;
import com.gitee.pifeng.monitoring.ui.core.CalculateDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务器CPU历史记录服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-01-24
 */
@Service
public class MonitorServerCpuHistoryServiceImpl extends ServiceImpl<IMonitorServerCpuHistoryDao, MonitorServerCpuHistory> implements IMonitorServerCpuHistoryService {

    /**
     * 服务器CPU历史记录数据访问对象
     */
    @Autowired
    private IMonitorServerCpuHistoryDao monitorServerCpuHistoryDao;

    /**
     * <p>
     * 获取服务器详情页面服务器CPU图表信息
     * </p>
     *
     * @param ip   服务器IP地址
     * @param time 时间
     * @return 服务器详情页面服务器CPU图表信息表现层对象
     * @author 皮锋
     * @custom.date 2020/10/19 14:22
     */
    @Override
    public List<ServerDetailPageServerCpuChartVo> getServerDetailPageServerCpuChartInfo(String ip, String time) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("ip", ip);
        // 计算时间
        CalculateDateTime calculateDateTime = new CalculateDateTime(time).invoke();
        // 开始时间
        Date startTime = calculateDateTime.getStartTime();
        // 结束时间
        Date endTime = calculateDateTime.getEndTime();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        List<ServerDetailPageServerCpuChartVo> serverDetailPageServerCpuChartVos = this.monitorServerCpuHistoryDao.getServerDetailPageServerCpuChartInfo(params);
        for (ServerDetailPageServerCpuChartVo serverDetailPageServerCpuChartVo : serverDetailPageServerCpuChartVos) {
            // 乘以100，保留两位小数
            Double cpuCombined = serverDetailPageServerCpuChartVo.getCpuCombined();
            if (cpuCombined != null) {
                serverDetailPageServerCpuChartVo.setCpuCombined(NumberUtil.round(cpuCombined * 100D, 2).doubleValue());
            }
            Double cpuIdle = serverDetailPageServerCpuChartVo.getCpuIdle();
            if (cpuIdle != null) {
                serverDetailPageServerCpuChartVo.setCpuIdle(NumberUtil.round(cpuIdle * 100D, 2).doubleValue());
            }
            Double cpuNice = serverDetailPageServerCpuChartVo.getCpuNice();
            if (cpuNice != null) {
                serverDetailPageServerCpuChartVo.setCpuNice(NumberUtil.round(cpuNice * 100D, 2).doubleValue());
            }
            Double cpuSys = serverDetailPageServerCpuChartVo.getCpuSys();
            if (cpuSys != null) {
                serverDetailPageServerCpuChartVo.setCpuSys(NumberUtil.round(cpuSys * 100D, 2).doubleValue());
            }
            Double cpuUser = serverDetailPageServerCpuChartVo.getCpuUser();
            if (cpuUser != null) {
                serverDetailPageServerCpuChartVo.setCpuUser(NumberUtil.round(cpuUser * 100D, 2).doubleValue());
            }
            Double cpuWait = serverDetailPageServerCpuChartVo.getCpuWait();
            if (cpuWait != null) {
                serverDetailPageServerCpuChartVo.setCpuWait(NumberUtil.round(cpuWait * 100D, 2).doubleValue());
            }
        }
        return serverDetailPageServerCpuChartVos;
    }

}
