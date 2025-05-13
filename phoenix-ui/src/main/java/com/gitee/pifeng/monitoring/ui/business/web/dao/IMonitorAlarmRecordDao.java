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
     * 获取告警类型统计信息
     * </p>
     *
     * @return 告警类型统计信息
     * @author 皮锋
     * @custom.date 2020/9/23 9:53
     */
    List<Map<String, Object>> getAlarmRecordTypeStatistics();

}
