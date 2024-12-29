package com.gitee.pifeng.monitoring.server.business.server.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gitee.pifeng.monitoring.common.domain.Alarm;
import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.common.dto.AlarmPackage;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorAlarmRecord;

import java.util.Date;

/**
 * <p>
 * 告警服务接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月10日 下午1:29:18
 */
public interface IAlarmService {

    /**
     * <p>
     * 处理告警包
     * </p>
     *
     * @param alarmPackage 告警包
     * @return {@link Result} 返回结果
     * @author 皮锋
     * @custom.date 2020年3月10日 下午1:33:55
     */
    Result dealAlarmPackage(AlarmPackage alarmPackage);

    /**
     * <p>
     * 把告警信息插入数据库
     * </p>
     *
     * @param alarm     告警信息
     * @param alarmUuid 告警代码
     * @author 皮锋
     * @custom.date 2020/5/13 16:21
     */
    void insertMonitorAlarmRecordToDb(Alarm alarm, String alarmUuid);

    /**
     * <p>
     * 根据 Wrapper 条件，查询总记录数
     * </p>
     *
     * @param monitorAlarmRecordLambdaQueryWrapper Wrapper 条件
     * @return 总记录数
     * @author 皮锋
     * @custom.date 2024/5/3 11:28
     */
    int selectCount(LambdaQueryWrapper<MonitorAlarmRecord> monitorAlarmRecordLambdaQueryWrapper);

    /**
     * <p>
     * 根据条件获取静默告警记录数
     * </p>
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 记录数
     * @author 皮锋
     * @custom.date 2024/11/4 12:19
     */
    int getSilenceAlarmCount(Date startTime, Date endTime);

}
