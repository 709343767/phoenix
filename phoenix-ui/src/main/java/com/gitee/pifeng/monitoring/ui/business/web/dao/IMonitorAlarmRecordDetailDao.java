package com.gitee.pifeng.monitoring.ui.business.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorAlarmRecordDetail;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 告警记录详情数据访问对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2025-05-03
 */
public interface IMonitorAlarmRecordDetailDao extends BaseMapper<MonitorAlarmRecordDetail> {

    /**
     * <p>
     * 获取最近7天的告警统计信息
     * </p>
     *
     * @return 最近7天的告警统计信息
     * @author 皮锋
     * @custom.date 2020/9/18 12:42
     */
    List<Map<String, Object>> getLast7DaysAlarmRecordStatistics();

    /**
     * <p>
     * 告警成功率统计
     * </p>
     *
     * @return 告警成功率统计信息
     * @author 皮锋
     * @custom.date 2020/9/22 17:08
     */
    Map<String, Object> getAlarmRecordSuccessRateStatistics();

}
