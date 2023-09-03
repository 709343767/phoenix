package com.gitee.pifeng.monitoring.server.business.server.service;

import com.gitee.pifeng.monitoring.common.domain.Alarm;
import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.common.dto.AlarmPackage;

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

}
