package com.gitee.pifeng.monitoring.ui.business.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorAlarmRecord;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 告警记录数据访问对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月7日 下午5:03:49
 */
public interface IMonitorAlarmRecordDao extends BaseMapper<MonitorAlarmRecord> {

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

    /**
     * <p>
     * 获取告警类型统计信息
     * </p>
     *
     * @return 告警类型统计信息
     * @author 皮锋
     * @custom.date 2020/9/23 9:53
     */
    List<Map<String, Object>> getAlarmRecordTypeStatistics();

    /**
     * <p>
     * 清空告警记录
     * </p>
     *
     * @author 皮锋
     * @custom.date 2022/7/13 10:09
     */
    void cleanupMonitorAlarmRecord();

}
