package com.gitee.pifeng.monitoring.server.business.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.common.domain.Alarm;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorAlarmRecordDetail;

/**
 * <p>
 * 告警记录详情服务层接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2025/5/7 08:07
 */
public interface IAlarmRecordDetailService extends IService<MonitorAlarmRecordDetail> {

    /**
     * <p>
     * 执行告警操作
     * </p>
     * 1.把告警详情信息插入数据库；<br>
     * 2.发送告警；<br>
     * 3.更新告警详情信息中的告警发送结果；<br>
     * 4.更新告警记录信息中每种告警方式的告警发送结果。<br>
     *
     * @param alarm         告警信息
     * @param alarmRecordId 告警记录表主键ID
     * @param alarmCode     告警代码
     * @author 皮锋
     * @custom.date 2025/5/3 17:35
     */
    void executeAlarm(Alarm alarm, Long alarmRecordId, String alarmCode);

}