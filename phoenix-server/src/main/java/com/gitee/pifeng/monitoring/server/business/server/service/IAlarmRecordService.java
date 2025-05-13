package com.gitee.pifeng.monitoring.server.business.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.common.constant.alarm.NoTSendAlarmReasonEnums;
import com.gitee.pifeng.monitoring.common.domain.Alarm;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorAlarmRecord;

import java.util.Date;

/**
 * <p>
 * 告警记录服务接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月10日 下午1:29:18
 */
public interface IAlarmRecordService extends IService<MonitorAlarmRecord> {

    /**
     * <p>
     * 把告警信息插入数据库
     * </p>
     *
     * @param alarm     告警信息
     * @param alarmCode 告警代码
     * @return 告警记录插入后的主键ID
     * @author 皮锋
     * @custom.date 2020/5/13 16:21
     */
    Long insertMonitorAlarmRecordToDb(Alarm alarm, String alarmCode);

    /**
     * <p>
     * 更新告警记录
     * </p>
     *
     * @param notSendAlarmReasonEnum 不发送告警原因
     * @param alarmCode              告警代码
     * @author 皮锋
     * @custom.date 2025/5/8 22:44
     */
    void updateMonitorAlarmRecord(NoTSendAlarmReasonEnums notSendAlarmReasonEnum, String alarmCode);

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
