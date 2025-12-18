package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gitee.pifeng.monitoring.common.constant.ResultMsgConstants;
import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmLevelEnums;
import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmWayEnums;
import com.gitee.pifeng.monitoring.common.constant.alarm.NoTSendAlarmReasonEnums;
import com.gitee.pifeng.monitoring.common.constant.monitortype.MonitorTypeEnums;
import com.gitee.pifeng.monitoring.common.domain.Alarm;
import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.common.dto.AlarmPackage;
import com.gitee.pifeng.monitoring.server.business.server.core.MonitoringConfigPropertiesLoader;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorAlarmDefinition;
import com.gitee.pifeng.monitoring.server.business.server.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalTime;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * <p>
 * 告警服务实现
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月10日 下午1:30:07
 */
@Slf4j
@Service
public class AlarmServiceImpl implements IAlarmService {

    /**
     * 监控配置属性加载器
     */
    @Autowired
    private MonitoringConfigPropertiesLoader monitoringConfigPropertiesLoader;

    /**
     * 实时监控服务接口
     */
    @Autowired
    private IRealtimeMonitoringService realtimeMonitoringService;

    /**
     * 告警定义服务接口
     */
    @Autowired
    private IAlarmDefinitionService alarmDefinitionService;

    /**
     * 告警记录服务接口
     */
    @Autowired
    private IAlarmRecordService alarmRecordService;

    /**
     * 告警记录详情服务层接口
     */
    @Autowired
    private IAlarmRecordDetailService alarmRecordDetailService;

    /**
     * 线程池
     */
    @Autowired
    @Qualifier("alarmThreadPoolExecutor")
    private ThreadPoolExecutor alarmThreadPoolExecutor;

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
    @Override
    public Result dealAlarmPackage(AlarmPackage alarmPackage) {
        // 返回结果，先默认成功，失败的后面会修改
        Result result = Result.builder().isSuccess(true).msg(ResultMsgConstants.SUCCESS).build();
        // 处理告警信息
        this.dealAlarm(result, alarmPackage);
        return result;
    }

    /**
     * <p>
     * 处理告警信息
     * </p>
     *
     * @param result       {@link Result} 返回结果
     * @param alarmPackage 告警包
     * @author 皮锋
     * @custom.date 2020/4/2 11:49
     */
    private void dealAlarm(Result result, AlarmPackage alarmPackage) {
        // 获取告警信息
        Alarm alarm = alarmPackage.getAlarm();
        // 对告警进行前置判断，防止重复发送相同的告警
        if (!this.realtimeMonitoringService.beforeAlarmJudge(alarm)) {
            String expMsg = "前置判断——>不满足告警条件！";
            this.wrapFailResult(result, expMsg);
            return;
        }
        // 告警代码，使用UUID
        String alarmCode = alarmPackage.getId();
        // 监控类型
        MonitorTypeEnums monitorTypeEnum = alarm.getMonitorType();
        // 告警代码
        String code = alarm.getCode();
        // 如果告警类型是自定义的业务告警，并且有告警代码，查询数据库中此告警代码对应的告警级别、告警标题、告警内容，数据库中有就用数据库的
        if (monitorTypeEnum == MonitorTypeEnums.CUSTOM && StringUtils.isNotBlank(code)) {
            try {
                LambdaQueryWrapper<MonitorAlarmDefinition> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(MonitorAlarmDefinition::getCode, code);
                MonitorAlarmDefinition monitorAlarmDefinition = this.alarmDefinitionService.getOne(lambdaQueryWrapper);
                if (monitorAlarmDefinition != null) {
                    // 数据库中的告警级别
                    String dbLevel = monitorAlarmDefinition.getGrade();
                    if (StringUtils.isNotBlank(dbLevel)) {
                        AlarmLevelEnums alarmLevelEnum = AlarmLevelEnums.str2Enum(dbLevel);
                        alarm.setAlarmLevel(alarmLevelEnum);
                    }
                    // 数据库中的告警标题
                    String dbTitle = monitorAlarmDefinition.getTitle();
                    if (StringUtils.isNotBlank(dbTitle)) {
                        alarm.setTitle(dbTitle);
                    }
                    // 数据库中的告警内容
                    String dbMsg = monitorAlarmDefinition.getContent();
                    if (StringUtils.isNotBlank(dbMsg)) {
                        alarm.setMsg(dbMsg);
                    }
                }
            } catch (Exception e) {
                String expMsg = "根据告警代码从数据库中查询告警定义失败！";
                this.wrapFailResult(result, expMsg);
                return;
            }
        }
        // 不管发不发送告警信息，先把告警记录存入数据库
        Long alarmRecordId = this.alarmRecordService.insertMonitorAlarmRecordToDb(alarm, alarmCode);
        // 是否发送告警通知信息
        boolean isSendAlarm = this.isSendAlarm(result, alarm, alarmCode);
        // 不发送告警通知，直接结束
        if (!isSendAlarm) {
            return;
        }
        // 注意：走到此处，就确定发送告警了，不管发送是否成功，result都是返回成功，代表处理此次告警成功了，发送告警和处理告警是两回事，
        // 因此采用异步的方式发送告警，防止阻塞此方法
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    asyncExecuteAlarm(alarm, alarmRecordId, alarmCode);
                }
            });
        } else {
            this.asyncExecuteAlarm(alarm, alarmRecordId, alarmCode);
        }
    }

    /**
     * <p>
     * 异步执行告警操作
     * </p>
     *
     * @param alarm         告警信息
     * @param alarmRecordId 告警记录表主键ID
     * @param alarmCode     告警代码
     * @author 皮锋
     * @custom.date 2025/12/17 10:27
     */
    private void asyncExecuteAlarm(Alarm alarm, Long alarmRecordId, String alarmCode) {
        this.alarmThreadPoolExecutor.execute(() -> {
            try {
                // 执行告警操作
                this.alarmRecordDetailService.executeAlarm(alarm, alarmRecordId, alarmCode);
            } catch (Exception e) {
                log.error("执行告警操作异常：{}", e.getMessage(), e);
            }
        });
    }

    /**
     * <p>
     * 是否发送告警信息
     * </p>
     *
     * @param result    {@link Result} 返回结果
     * @param alarm     告警信息
     * @param alarmCode 告警代码
     * @return 是 或 否
     * @author 皮锋
     * @custom.date 2020/9/14 12:37
     */
    private boolean isSendAlarm(Result result, Alarm alarm, String alarmCode) {
        // 告警开关是否打开
        boolean isEnable = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getAlarmProperties().isEnable();
        // 告警开关没有打开，不做处理，直接返回
        if (!isEnable) {
            String expMsg = "告警开关没有打开，不发送告警信息！";
            this.wrapFailResult(result, expMsg);
            // 写入不发送告警原因
            this.alarmRecordService.updateMonitorAlarmRecord(NoTSendAlarmReasonEnums.ALARM_SWITCH_OFF, alarmCode);
            return false;
        }
        // 是否开启告警静默
        boolean silenceEnable = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getAlarmProperties().isSilenceEnable();
        if (silenceEnable && !alarm.isIgnoreSilence()) {
            LocalTime currentTime = LocalTime.now();
            LocalTime silenceStartTime = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getAlarmProperties().getSilenceStartTime();
            LocalTime silenceEndTime = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getAlarmProperties().getSilenceEndTime();
            if (!currentTime.isBefore(silenceStartTime) || !currentTime.isAfter(silenceEndTime)) {
                String expMsg = "告警静默时间段，不发送告警信息！";
                this.wrapFailResult(result, expMsg);
                // 写入不发送告警原因
                this.alarmRecordService.updateMonitorAlarmRecord(NoTSendAlarmReasonEnums.SILENCE_ALARM_PERIOD, alarmCode);
                return false;
            }
        }
        // 是否是测试告警信息
        boolean isTest = alarm.isTest();
        // 是测试告警信息，不做处理，直接返回
        if (isTest) {
            String expMsg = "当前为测试信息，不发送告警信息！";
            this.wrapFailResult(result, expMsg);
            // 写入不发送告警原因
            this.alarmRecordService.updateMonitorAlarmRecord(NoTSendAlarmReasonEnums.TEST_MSG, alarmCode);
            return false;
        }
        // 告警级别
        AlarmLevelEnums configAlarmLevelEnum = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getAlarmProperties().getLevelEnum();
        // 告警级别
        AlarmLevelEnums levelEnum = alarm.getAlarmLevel();
        // 告警级别小于配置的告警级别，不做处理，直接返回
        if (!AlarmLevelEnums.isAlarm(configAlarmLevelEnum, levelEnum)) {
            String expMsg = "小于配置的告警级别，不发送告警信息！";
            this.wrapFailResult(result, expMsg);
            // 写入不发送告警原因
            this.alarmRecordService.updateMonitorAlarmRecord(NoTSendAlarmReasonEnums.LESS_THAN_THE_CONFIGURED_ALARM_LEVEL, alarmCode);
            return false;
        }
        // 告警内容标题
        String alarmTitle = alarm.getTitle();
        // 没有告警标题，不做处理，直接返回
        if (StringUtils.isBlank(alarmTitle)) {
            String expMsg = "告警标题为空，不发送告警信息！";
            this.wrapFailResult(result, expMsg);
            // 写入不发送告警原因
            this.alarmRecordService.updateMonitorAlarmRecord(NoTSendAlarmReasonEnums.ALARM_TITLE_EMPTY, alarmCode);
            return false;
        }
        // 告警内容
        String msg = alarm.getMsg();
        // 没有告警内容，不做处理，直接返回
        if (StringUtils.isBlank(msg)) {
            String expMsg = "告警内容为空，不发送告警信息！";
            this.wrapFailResult(result, expMsg);
            // 写入不发送告警原因
            this.alarmRecordService.updateMonitorAlarmRecord(NoTSendAlarmReasonEnums.ALARM_CONTENT_EMPTY, alarmCode);
            return false;
        }
        // 告警方式
        AlarmWayEnums[] alarmWayEnums = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getAlarmProperties().getWayEnums();
        // 没有配置告警方式，不做处理，直接返回
        if (ArrayUtil.isEmpty(alarmWayEnums)) {
            String expMsg = "没有配置告警方式，不发送告警信息！";
            this.wrapFailResult(result, expMsg);
            // 写入不发送告警原因
            this.alarmRecordService.updateMonitorAlarmRecord(NoTSendAlarmReasonEnums.NO_ALARM_MODE_CONFIGURED, alarmCode);
            return false;
        }
        return true;
    }

    /**
     * <p>
     * 封装失败时的返回结果。
     * </p>
     *
     * @param result {@link Result} 返回结果
     * @param msg    结果信息
     * @author 皮锋
     * @custom.date 2020/9/13 21:35
     */
    private void wrapFailResult(Result result, String msg) {
        if (log.isDebugEnabled()) {
            log.debug(msg);
        }
        result.setSuccess(false);
        result.setMsg(msg);
    }

}