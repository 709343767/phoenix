package com.gitee.pifeng.monitoring.ui.business.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorServerLoadAverageHistory;
import com.gitee.pifeng.monitoring.ui.business.web.vo.ServerDetailPageServerLoadAverageChartVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务器平均负载历史记录数据访问对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-06-17
 */
public interface IMonitorServerLoadAverageHistoryDao extends BaseMapper<MonitorServerLoadAverageHistory> {

    /**
     * <p>
     * 获取服务器详情页面服务器平均负载图表信息
     * </p>
     *
     * @param params 请求参数
     * @return 服务器详情页面服务器平均负载图表信息表现层对象
     * @author 皮锋
     * @custom.date 2022/6/19 14:58
     */
    List<ServerDetailPageServerLoadAverageChartVo> getServerDetailPageServerLoadAverageChartInfo(@Param("params") Map<String, Object> params);
}
