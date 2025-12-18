package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.constant.ZeroOrOneConstants;
import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmReasonEnums;
import com.gitee.pifeng.monitoring.common.constant.monitortype.MonitorSubTypeEnums;
import com.gitee.pifeng.monitoring.common.constant.monitortype.MonitorTypeEnums;
import com.gitee.pifeng.monitoring.common.domain.Alarm;
import com.gitee.pifeng.monitoring.plug.core.InstanceGenerator;
import com.gitee.pifeng.monitoring.server.business.server.core.MysqlDistributedLock;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorRealtimeMonitoringDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorRealtimeMonitoring;
import com.gitee.pifeng.monitoring.server.business.server.service.IRealtimeMonitoringService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
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
@Slf4j
@Service
public class RealtimeMonitoringServiceImpl extends ServiceImpl<IMonitorRealtimeMonitoringDao, MonitorRealtimeMonitoring> implements IRealtimeMonitoringService {

    /**
     * MySQL实现的分布式锁
     */
    @Autowired
    private MysqlDistributedLock mysqlDistributedLock;

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
    @Override
    @Retryable
    @Transactional(rollbackFor = Throwable.class, timeout = 10)
    public boolean beforeAlarmJudge(Alarm alarm) {
        // 计时器
        TimeInterval timer = DateUtil.timer();
        // 监控类型
        MonitorTypeEnums monitorTypeEnum = alarm.getMonitorType();
        // 告警原因
        AlarmReasonEnums alarmReasonEnum = alarm.getAlarmReason();
        // 自定义业务告警 || 没有设置告警原因==>直接放过
        if (monitorTypeEnum == MonitorTypeEnums.CUSTOM || alarmReasonEnum == AlarmReasonEnums.IGNORE) {
            return true;
        }
        // 监控类型名
        String typeEnumName = monitorTypeEnum.name();
        // 告警代码
        String alarmCode = alarm.getCode();
        if (StringUtils.isEmpty(alarmCode)) {
            return true;
        }
        // 生成细粒度锁键（type:code）
        String lockKey = "alarm_judge:" + typeEnumName + ":" + alarmCode;
        // 锁持有者
        String instanceId = InstanceGenerator.getInstanceId();
        // 是否获取到分布式锁
        boolean lockAcquired = false;
        try {
            // 尝试获取锁：最多等待 5 秒，锁自动过期 15 秒
            // 注意：分布式锁在 Spring 事务提交前持有，需确保事务执行时间 < 锁过期时间（15秒）
            // 高并发场景下 Redisson 分布式锁是更优选择
            lockAcquired = this.mysqlDistributedLock.tryLock(lockKey, instanceId, 15, 5);
            if (!lockAcquired) {
                // 获取锁超时，放弃本次告警判断（避免并发冲突）
                log.warn("尝试获取分布式锁超时：lockKey={}，instanceId={}", lockKey, instanceId);
                return false;
            }
            // 监控子类型
            MonitorSubTypeEnums monitorSubType = alarm.getMonitorSubType();
            // 被告警主体唯一ID
            String alertedEntityId = alarm.getAlertedEntityId();
            // 查询数据库中有没有此实时监控信息
            LambdaQueryWrapper<MonitorRealtimeMonitoring> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(MonitorRealtimeMonitoring::getType, typeEnumName);
            lambdaQueryWrapper.eq(MonitorRealtimeMonitoring::getCode, alarmCode);
            MonitorRealtimeMonitoring monitorRealtimeMonitoringDb = this.baseMapper.selectOne(lambdaQueryWrapper);
            // 一.数据库中没有此实时监控信息
            if (monitorRealtimeMonitoringDb == null) {
                MonitorRealtimeMonitoring monitorRealtimeMonitoring = MonitorRealtimeMonitoring.builder()
                        .type(typeEnumName)
                        .subType(monitorSubType.name())
                        .code(alarmCode)
                        .alertedEntityId(alertedEntityId)
                        // 如果是“正常变异常”告警，把是否告警设置为true
                        .isSentAlarm(alarmReasonEnum == AlarmReasonEnums.NORMAL_2_ABNORMAL ? ZeroOrOneConstants.ONE : ZeroOrOneConstants.ZERO)
                        .insertTime(new Date())
                        .build();
                this.baseMapper.insert(monitorRealtimeMonitoring);
                // 发现（应用程序、服务器、...） 或者 正常变异常，需要告警；否则，不发送告警
                return (alarmReasonEnum == AlarmReasonEnums.DISCOVERY || alarmReasonEnum == AlarmReasonEnums.NORMAL_2_ABNORMAL);
            }
            // 二.数据库中有此实时监控信息
            MonitorRealtimeMonitoring monitorRealtimeMonitoring = MonitorRealtimeMonitoring.builder()
                    .type(typeEnumName)
                    .code(alarmCode)
                    // 如果是“正常变异常”告警，把是否告警设置为true
                    .isSentAlarm(alarmReasonEnum == AlarmReasonEnums.NORMAL_2_ABNORMAL ? ZeroOrOneConstants.ONE : ZeroOrOneConstants.ZERO)
                    .updateTime(new Date())
                    .build();
            LambdaUpdateWrapper<MonitorRealtimeMonitoring> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(MonitorRealtimeMonitoring::getType, typeEnumName);
            lambdaUpdateWrapper.eq(MonitorRealtimeMonitoring::getCode, alarmCode);
            // 更新实时监控信息
            this.baseMapper.update(monitorRealtimeMonitoring, lambdaUpdateWrapper);
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
        } finally {
            if (lockAcquired) {
                try {
                    boolean released = this.mysqlDistributedLock.releaseLock(lockKey, instanceId);
                    if (!released) {
                        log.warn("尝试释放分布式锁失败（可能已被自动清理）：lockKey={}，instanceId={}", lockKey, instanceId);
                    }
                } catch (Exception e) {
                    // 可记录日志，但不要抛出异常影响主流程
                    log.error("尝试释放分布式锁失败：lockKey={}，instanceId={}", lockKey, instanceId, e);
                }
            }
            // 时间差（毫秒）
            String betweenDay = timer.intervalPretty();
            // 临界值
            int criticalValue = 10;
            if (timer.intervalSecond() > criticalValue) {
                log.warn("告警前置判断耗时：{}", betweenDay);
            }
        }
    }

}