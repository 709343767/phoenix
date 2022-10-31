package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmReasonEnums;
import com.gitee.pifeng.monitoring.common.constant.MonitorTypeEnums;
import com.gitee.pifeng.monitoring.common.constant.ZeroOrOneConstants;
import com.gitee.pifeng.monitoring.common.domain.Alarm;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorRealtimeMonitoringDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorRealtimeMonitoring;
import com.gitee.pifeng.monitoring.server.business.server.service.IRealtimeMonitoringService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 * 实时监控服务接口实现
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/1/29 14:56
 */
@Service
public class RealtimeMonitoringServiceImpl extends ServiceImpl<IMonitorRealtimeMonitoringDao, MonitorRealtimeMonitoring> implements IRealtimeMonitoringService {

    /**
     * 实时监控数据访问对象
     */
    @Autowired
    private IMonitorRealtimeMonitoringDao monitorRealtimeMonitoringDao;

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
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public boolean beforeAlarmJudge(Alarm alarm) {
        synchronized (RealtimeMonitoringServiceImpl.class) {
            // 监控类型
            MonitorTypeEnums monitorTypeEnum = alarm.getMonitorType();
            // 告警原因
            AlarmReasonEnums alarmReasonEnum = alarm.getAlarmReason();
            // 告警代码
            String alarmCode = alarm.getCode();
            // 自定义业务告警 || 没有设置告警原因==>直接放过
            if (monitorTypeEnum == MonitorTypeEnums.CUSTOM || alarmReasonEnum == AlarmReasonEnums.IGNORE) {
                return true;
            }
            // 查询数据库中有没有此实时监控信息
            LambdaQueryWrapper<MonitorRealtimeMonitoring> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(MonitorRealtimeMonitoring::getType, monitorTypeEnum.name());
            lambdaQueryWrapper.eq(MonitorRealtimeMonitoring::getCode, alarmCode);
            MonitorRealtimeMonitoring monitorRealtimeMonitoringDb = this.monitorRealtimeMonitoringDao.selectOne(lambdaQueryWrapper);
            // 一.数据库中没有此实时监控信息
            if (monitorRealtimeMonitoringDb == null) {
                MonitorRealtimeMonitoring monitorRealtimeMonitoring = MonitorRealtimeMonitoring.builder()
                        .type(monitorTypeEnum.name())
                        .code(alarmCode)
                        // 如果是“正常变异常”告警，把是否告警设置为true
                        .isSentAlarm(alarmReasonEnum == AlarmReasonEnums.NORMAL_2_ABNORMAL ? ZeroOrOneConstants.ONE : ZeroOrOneConstants.ZERO)
                        .insertTime(new Date())
                        .build();
                this.monitorRealtimeMonitoringDao.insert(monitorRealtimeMonitoring);
                // 发现（应用程序、服务器、...） 或者 正常变异常，需要告警；否则，不发送告警
                return (alarmReasonEnum == AlarmReasonEnums.DISCOVERY || alarmReasonEnum == AlarmReasonEnums.NORMAL_2_ABNORMAL);
            }
            // 二.数据库中有此实时监控信息
            MonitorRealtimeMonitoring monitorRealtimeMonitoring = MonitorRealtimeMonitoring.builder()
                    .type(monitorTypeEnum.name())
                    .code(alarmCode)
                    // 如果是“正常变异常”告警，把是否告警设置为true
                    .isSentAlarm(alarmReasonEnum == AlarmReasonEnums.NORMAL_2_ABNORMAL ? ZeroOrOneConstants.ONE : ZeroOrOneConstants.ZERO)
                    .updateTime(new Date())
                    .build();
            LambdaUpdateWrapper<MonitorRealtimeMonitoring> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(MonitorRealtimeMonitoring::getType, monitorTypeEnum.name());
            lambdaUpdateWrapper.eq(MonitorRealtimeMonitoring::getCode, alarmCode);
            // 更新实时监控信息
            this.monitorRealtimeMonitoringDao.update(monitorRealtimeMonitoring, lambdaUpdateWrapper);
            // 是否已经发送了告警
            boolean isSentAlarm = StringUtils.equals(monitorRealtimeMonitoringDb.getIsSentAlarm(), ZeroOrOneConstants.ONE);
            // 判断是否允许发送告警
            if (alarmReasonEnum == AlarmReasonEnums.NORMAL_2_ABNORMAL) {
                return !isSentAlarm;
            }
            if (alarmReasonEnum == AlarmReasonEnums.ABNORMAL_2_NORMAL) {
                return isSentAlarm;
            }
            return false;
        }
    }

}