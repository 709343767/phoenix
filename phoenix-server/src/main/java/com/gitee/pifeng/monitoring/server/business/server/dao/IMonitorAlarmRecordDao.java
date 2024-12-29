package com.gitee.pifeng.monitoring.server.business.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorAlarmRecord;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * <p>
 * 监控告警记录数据访问对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/5/13 15:13
 */
public interface IMonitorAlarmRecordDao extends BaseMapper<MonitorAlarmRecord> {

    /**
     * <p>
     * 根据条件获取静默告警记录数
     * </p>
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 记录数
     * @author 皮锋
     * @custom.date 2024/11/4 12:26
     */
    int getSilenceAlarmCount(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

}
