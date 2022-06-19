package com.gitee.pifeng.monitoring.ui.business.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorServerLoadAverageHistoryDao;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorServerLoadAverageHistory;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorServerLoadAverageHistoryService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.ServerDetailPageServerLoadAverageChartVo;
import com.gitee.pifeng.monitoring.ui.core.CalculateDateTime;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务器平均负载历史记录服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-06-17
 */
@Service
public class MonitorServerLoadAverageHistoryServiceImpl extends ServiceImpl<IMonitorServerLoadAverageHistoryDao, MonitorServerLoadAverageHistory> implements IMonitorServerLoadAverageHistoryService {

    /**
     * <p>
     * 获取服务器详情页面服务器平均负载图表信息
     * </p>
     *
     * @param ip   服务器IP地址
     * @param time 时间
     * @return 服务器详情页面服务器平均负载图表信息表现层对象
     * @author 皮锋
     * @custom.date 2022/6/19 14:54
     */
    @Override
    public List<ServerDetailPageServerLoadAverageChartVo> getServerDetailPageServerLoadAverageChartInfo(String ip, String time) {
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
        return this.baseMapper.getServerDetailPageServerLoadAverageChartInfo(params);
    }

}
