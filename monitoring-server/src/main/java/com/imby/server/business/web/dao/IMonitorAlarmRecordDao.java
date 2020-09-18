package com.imby.server.business.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imby.server.business.web.entity.MonitorAlarmRecord;
import com.imby.server.business.web.vo.LastFewDaysAlarmRecordStatisticsVo;

import java.util.List;

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
     * @return {@link LastFewDaysAlarmRecordStatisticsVo}
     * @author 皮锋
     * @custom.date 2020/9/18 12:42
     */
    List<LastFewDaysAlarmRecordStatisticsVo> getLast7DaysAlarmRecordStatistics();

}
