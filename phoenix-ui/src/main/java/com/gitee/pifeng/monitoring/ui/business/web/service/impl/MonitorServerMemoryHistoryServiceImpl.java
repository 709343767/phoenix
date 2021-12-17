package com.gitee.pifeng.monitoring.ui.business.web.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorServerMemoryHistoryDao;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorServerMemoryHistory;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorServerMemoryHistoryService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.ServerDetailPageServerMemoryChartVo;
import com.gitee.pifeng.monitoring.ui.core.CalculateDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务器内存历史记录服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-01-24
 */
@Service
public class MonitorServerMemoryHistoryServiceImpl extends ServiceImpl<IMonitorServerMemoryHistoryDao, MonitorServerMemoryHistory> implements IMonitorServerMemoryHistoryService {

    /**
     * 服务器内存历史记录数据访问对象
     */
    @Autowired
    private IMonitorServerMemoryHistoryDao monitorServerMemoryHistoryDao;

    /**
     * <p>
     * 获取服务器详情页面服务器内存图表信息
     * </p>
     *
     * @param ip   服务器IP地址
     * @param time 时间
     * @return 服务器详情页面服务器内存图表信息表现层对象
     * @author 皮锋
     * @custom.date 2020/10/21 12:42
     */
    @Override
    public List<ServerDetailPageServerMemoryChartVo> getServerDetailPageServerMemoryChartInfo(String ip, String time) {
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
        List<ServerDetailPageServerMemoryChartVo> serverDetailPageServerMemoryChartVos = this.monitorServerMemoryHistoryDao.getServerDetailPageServerMemoryChartInfo(params);
        // 除数（1024 * 1024 * 1024 = 1073741824）
        Double v2 = 1073741824D;
        for (ServerDetailPageServerMemoryChartVo serverDetailPageServerMemoryChartVo : serverDetailPageServerMemoryChartVos) {
            // 转GB
            serverDetailPageServerMemoryChartVo.setMemFree(NumberUtil.div(serverDetailPageServerMemoryChartVo.getMemFree(), v2, 2));
            // 转GB
            serverDetailPageServerMemoryChartVo.setMemTotal(NumberUtil.div(serverDetailPageServerMemoryChartVo.getMemTotal(), v2, 2));
            // 转GB
            serverDetailPageServerMemoryChartVo.setMemUsed(NumberUtil.div(serverDetailPageServerMemoryChartVo.getMemUsed(), v2, 2));
            // 乘以100后，保留两位小数
            serverDetailPageServerMemoryChartVo.setMenUsedPercent(NumberUtil.round(serverDetailPageServerMemoryChartVo.getMenUsedPercent() * 100D, 2).doubleValue());
            // 转GB
            serverDetailPageServerMemoryChartVo.setSwapFree(NumberUtil.div(serverDetailPageServerMemoryChartVo.getSwapFree(), v2, 2));
            // 转GB
            serverDetailPageServerMemoryChartVo.setSwapTotal(NumberUtil.div(serverDetailPageServerMemoryChartVo.getSwapTotal(), v2, 2));
            // 转GB
            serverDetailPageServerMemoryChartVo.setSwapUsed(NumberUtil.div(serverDetailPageServerMemoryChartVo.getSwapUsed(), v2, 2));
            // 乘以100后，保留两位小数
            serverDetailPageServerMemoryChartVo.setSwapUsedPercent(NumberUtil.round(serverDetailPageServerMemoryChartVo.getSwapUsedPercent() * 100D, 2).doubleValue());
        }
        return serverDetailPageServerMemoryChartVos;
    }

}
