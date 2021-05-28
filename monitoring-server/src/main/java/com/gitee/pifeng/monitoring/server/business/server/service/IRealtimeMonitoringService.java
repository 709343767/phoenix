package com.gitee.pifeng.monitoring.server.business.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.common.domain.Alarm;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorRealtimeMonitoring;

/**
 * <p>
 * 实时监控服务接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/1/29 14:54
 */
public interface IRealtimeMonitoringService extends IService<MonitorRealtimeMonitoring> {

    /**
     * <p>
     * 对告警进行前置判断，防止重复发送相同的告警
     * </p>
     *
     * @param alarm 告警信息
     * @return boolean
     * @author 皮锋
     * @custom.date 2021/2/1 11:20
     */
    boolean beforeAlarmJudge(Alarm alarm);

}
