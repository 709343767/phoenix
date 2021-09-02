package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.gitee.pifeng.monitoring.common.constant.*;
import com.gitee.pifeng.monitoring.common.domain.Alarm;
import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.common.dto.AlarmPackage;
import com.gitee.pifeng.monitoring.server.business.server.core.MonitoringConfigPropertiesLoader;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorAlarmDefinitionDao;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorAlarmRecordDao;
import com.gitee.pifeng.monitoring.server.business.server.domain.Mail;
import com.gitee.pifeng.monitoring.server.business.server.domain.Sms;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorAlarmDefinition;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorAlarmRecord;
import com.gitee.pifeng.monitoring.server.business.server.service.IAlarmService;
import com.gitee.pifeng.monitoring.server.business.server.service.IMailService;
import com.gitee.pifeng.monitoring.server.business.server.service.IRealtimeMonitoringService;
import com.gitee.pifeng.monitoring.server.business.server.service.ISmsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

/**
 * <p>
 * 告警服务实现
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月10日 下午1:30:07
 */
@Service
@Slf4j
public class AlarmServiceImpl implements IAlarmService {

    /**
     * 实时监控服务接口
     */
    @Autowired
    private IRealtimeMonitoringService realtimeMonitoringService;

    /**
     * 短信服务接口
     */
    @Autowired
    private ISmsService smsService;

    /**
     * 邮箱服务接口
     */
    @Autowired
    private IMailService mailService;

    /**
     * 监控告警定义数据访问对象
     */
    @Autowired
    private IMonitorAlarmDefinitionDao monitorAlarmDefinitionDao;

    /**
     * 监控告警记录数据访问对象
     */
    @Autowired
    private IMonitorAlarmRecordDao monitorAlarmRecordDao;

    /**
     * <p>
     * 异步处理告警包
     * </p>
     *
     * @param alarmPackage 告警包
     * @return {@link Future}
     * @author 皮锋
     * @custom.date 2020年3月10日 下午1:33:55
     */
    @Async
    @Override
    public Future<Result> dealAlarmPackage(AlarmPackage alarmPackage) {
        // 返回结果
        Result result = new Result();
        // 处理告警信息
        this.dealAlarm(result, alarmPackage);
        return new AsyncResult<>(result);
    }

    /**
     * <p>
     * 处理告警信息
     * </p>
     *
     * @param result       返回结果
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
        // 告警代码
        String alarmUuid = alarmPackage.getId();
        // 监控类型
        MonitorTypeEnums monitorTypeEnum = alarm.getMonitorType();
        // 告警级别
        AlarmLevelEnums alarmLevelEnum = alarm.getAlarmLevel();
        // 告警方式
        AlarmWayEnums[] alarmWayEnums = MonitoringConfigPropertiesLoader.getMonitoringProperties().getAlarmProperties().getWayEnums();
        // 告警代码
        String code = alarm.getCode();
        // 告警内容标题
        String alarmTitle = alarm.getTitle();
        // 告警内容
        String alarmMsg = alarm.getMsg();
        // 如果告警类型是自定义的业务告警，并且有告警代码，查询数据库中此告警代码对应的告警级别、告警标题、告警内容，数据库中有就用数据库的
        if (monitorTypeEnum == MonitorTypeEnums.CUSTOM && StringUtils.isNotBlank(code)) {
            try {
                LambdaQueryWrapper<MonitorAlarmDefinition> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(MonitorAlarmDefinition::getCode, code);
                MonitorAlarmDefinition monitorAlarmDefinition = this.monitorAlarmDefinitionDao.selectOne(lambdaQueryWrapper);
                if (monitorAlarmDefinition != null) {
                    // 数据库中的告警级别
                    String dbLevel = monitorAlarmDefinition.getGrade();
                    if (StringUtils.isNotBlank(dbLevel)) {
                        alarmLevelEnum = AlarmLevelEnums.str2Enum(dbLevel);
                    }
                    // 数据库中的告警标题
                    String dbTitle = monitorAlarmDefinition.getTitle();
                    if (StringUtils.isNotBlank(dbTitle)) {
                        alarmTitle = dbTitle;
                    }
                    // 数据库中的告警内容
                    String dbMsg = monitorAlarmDefinition.getContent();
                    if (StringUtils.isNotBlank(dbMsg)) {
                        alarmMsg = dbMsg;
                    }
                }
            } catch (Exception e) {
                String expMsg = "根据告警代码从数据库中查询告警定义失败！";
                this.wrapFailResult(result, expMsg);
                return;
            }
        }
        // 不管发不发送告警信息，先把告警记录存入数据库
        this.insertMonitorAlarmRecordToDb(alarmUuid, code, alarmTitle, alarmMsg, monitorTypeEnum, alarmLevelEnum, alarmWayEnums);
        // 是否发送告警信息
        if (!this.isSendAlarm(result, alarm)) {
            return;
        }
        // 发送告警以及把告警记录计入数据库
        this.sendAlarmAndOperateDb(result, alarmUuid, alarmTitle, alarmMsg, alarmLevelEnum, alarmWayEnums);
    }

    /**
     * <p>
     * 发送告警以及把告警记录计入数据库。
     * </p>
     * 1.把告警信息存入数据库；<br>
     * 2.发送告警；<br>
     * 3.更新数据库中的告警发送结果。<br>
     *
     * @param result         返回结果
     * @param alarmUuid      告警代码
     * @param alarmTitle     告警内容标题
     * @param alarmMsg       告警内容
     * @param alarmLevelEnum 告警级别
     * @param alarmWayEnums  告警方式
     * @author 皮锋
     * @custom.date 2020/9/14 12:56
     */
    private void sendAlarmAndOperateDb(Result result,
                                       String alarmUuid, String alarmTitle, String alarmMsg,
                                       AlarmLevelEnums alarmLevelEnum, AlarmWayEnums[] alarmWayEnums) {
        List<AlarmWayEnums> alarmWayEnumsList = Arrays.asList(alarmWayEnums);
        // 告警方式为短信告警和邮件告警
        if (alarmWayEnumsList.contains(AlarmWayEnums.SMS) && alarmWayEnumsList.contains(AlarmWayEnums.MAIL)) {
            // 处理短信告警
            this.dealSmsAlarm(result, alarmTitle, alarmMsg, alarmLevelEnum);
            // 告警发送完更新数据库中告警发送结果
            this.updateMonitorAlarmRecordToDb(result, alarmUuid, AlarmWayEnums.SMS);
            // 处理邮件告警
            this.dealMailAlarm(result, alarmTitle, alarmMsg, alarmLevelEnum);
            // 告警发送完更新数据库中告警发送结果
            this.updateMonitorAlarmRecordToDb(result, alarmUuid, AlarmWayEnums.MAIL);
        } else if (alarmWayEnumsList.contains(AlarmWayEnums.SMS)) {
            // 处理短信告警
            this.dealSmsAlarm(result, alarmTitle, alarmMsg, alarmLevelEnum);
            // 告警发送完更新数据库中告警发送结果
            this.updateMonitorAlarmRecordToDb(result, alarmUuid, AlarmWayEnums.SMS);
        } else if (alarmWayEnumsList.contains(AlarmWayEnums.MAIL)) {
            // 处理邮件告警
            this.dealMailAlarm(result, alarmTitle, alarmMsg, alarmLevelEnum);
            // 告警发送完更新数据库中告警发送结果
            this.updateMonitorAlarmRecordToDb(result, alarmUuid, AlarmWayEnums.MAIL);
        }
    }

    /**
     * <p>
     * 是否发送告警信息
     * </p>
     *
     * @param result 返回结果
     * @param alarm  告警信息
     * @return 是 或 否
     * @author 皮锋
     * @custom.date 2020/9/14 12:37
     */
    private boolean isSendAlarm(Result result, Alarm alarm) {
        // 告警开关是否打开
        boolean isEnable = MonitoringConfigPropertiesLoader.getMonitoringProperties().getAlarmProperties().isEnable();
        // 告警开关没有打开，不做处理，直接返回
        if (!isEnable) {
            String expMsg = "告警开关没有打开，不发送告警信息！";
            this.wrapFailResult(result, expMsg);
            return false;
        }
        // 是否是测试告警信息
        boolean isTest = alarm.isTest();
        // 是测试告警信息，不做处理，直接返回
        if (isTest) {
            String expMsg = "当前为测试信息，不发送告警信息！";
            this.wrapFailResult(result, expMsg);
            return false;
        }
        // 告警级别
        AlarmLevelEnums configAlarmLevelEnum = MonitoringConfigPropertiesLoader.getMonitoringProperties().getAlarmProperties().getLevelEnum();
        // 告警级别
        AlarmLevelEnums levelEnum = alarm.getAlarmLevel();
        // 告警级别小于配置的告警级别，不做处理，直接返回
        if (!AlarmLevelEnums.isAlarm(configAlarmLevelEnum, levelEnum)) {
            String expMsg = "小于配置的告警级别，不发送告警信息！";
            this.wrapFailResult(result, expMsg);
            return false;
        }
        // 告警内容标题
        String alarmTitle = alarm.getTitle();
        // 没有告警标题，不做处理，直接返回
        if (StringUtils.isBlank(alarmTitle)) {
            String expMsg = "告警标题为空，不发送告警信息！";
            this.wrapFailResult(result, expMsg);
            return false;
        }
        // 告警内容
        String msg = alarm.getMsg();
        // 没有告警内容，不做处理，直接返回
        if (StringUtils.isBlank(msg)) {
            String expMsg = "告警内容为空，不发送告警信息！";
            this.wrapFailResult(result, expMsg);
            return false;
        }
        // 告警方式
        AlarmWayEnums[] alarmWayEnums = MonitoringConfigPropertiesLoader.getMonitoringProperties().getAlarmProperties().getWayEnums();
        // 没有配置告警方式，不做处理，直接返回
        if (ArrayUtil.isEmpty(alarmWayEnums)) {
            String expMsg = "没有配置告警方式，不发送告警信息！";
            this.wrapFailResult(result, expMsg);
            return false;
        }
        return true;
    }

    /**
     * <p>
     * 封装失败时的返回结果。
     * </p>
     *
     * @param result 返回结果
     * @param msg    结果信息
     * @author 皮锋
     * @custom.date 2020/9/13 21:35
     */
    private void wrapFailResult(Result result, String msg) {
        log.warn(msg);
        result.setSuccess(false);
        result.setMsg(msg);
    }

    /**
     * <p>
     * 封装成功时的返回结果。
     * </p>
     *
     * @param result 返回结果
     * @author 皮锋
     * @custom.date 2020/9/13 21:35
     */
    private void wrapSuccessResult(Result result) {
        result.setSuccess(true);
        result.setMsg(ResultMsgConstants.SUCCESS);
    }

    /**
     * <p>
     * 更新数据库中告警信息发送状态
     * </p>
     *
     * @param result        返回结果
     * @param uuid          告警代码
     * @param alarmWayEnums 告警方式
     * @author 皮锋
     * @custom.date 2020/5/13 17:04
     */
    private void updateMonitorAlarmRecordToDb(Result result, String uuid, AlarmWayEnums alarmWayEnums) {
        MonitorAlarmRecord monitorAlarmRecord = new MonitorAlarmRecord();
        // 更新状态
        if (result.isSuccess()) {
            monitorAlarmRecord.setStatus(ZeroOrOneConstants.ONE);
        } else {
            monitorAlarmRecord.setStatus(ZeroOrOneConstants.ZERO);
        }
        // 更新时间
        monitorAlarmRecord.setUpdateTime(new Date());
        LambdaUpdateWrapper<MonitorAlarmRecord> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(MonitorAlarmRecord::getCode, uuid);
        lambdaUpdateWrapper.eq(MonitorAlarmRecord::getWay, alarmWayEnums);
        this.monitorAlarmRecordDao.update(monitorAlarmRecord, lambdaUpdateWrapper);
    }

    /**
     * <p>
     * 把告警信息插入数据库
     * </p>
     *
     * @param alarmUuid       告警代码
     * @param alarmDefCode    告警定义编码
     * @param alarmTitle      告警标题
     * @param alarmMsg        告警内容
     * @param monitorTypeEnum 监控类型（SERVER、NET、INSTANCE、CUSTOM）
     * @param alarmLevelEnum  告警级别（INFO、WARM、ERROR、FATAL）
     * @param alarmWayEnums   告警方式（SMS、MAIL、...）
     * @author 皮锋
     * @custom.date 2020/5/13 16:21
     */
    private void insertMonitorAlarmRecordToDb(String alarmUuid, String alarmDefCode, String alarmTitle, String alarmMsg,
                                              MonitorTypeEnums monitorTypeEnum, AlarmLevelEnums alarmLevelEnum,
                                              AlarmWayEnums[] alarmWayEnums) {
        MonitorAlarmRecord monitorAlarmRecord = new MonitorAlarmRecord();
        monitorAlarmRecord.setCode(alarmUuid);
        monitorAlarmRecord.setAlarmDefCode(alarmDefCode);
        monitorAlarmRecord.setTitle(alarmTitle);
        monitorAlarmRecord.setContent(alarmMsg);
        monitorAlarmRecord.setLevel(alarmLevelEnum != null ? alarmLevelEnum.name() : null);
        monitorAlarmRecord.setType(monitorTypeEnum != null ? monitorTypeEnum.name() : null);
        // 告警方式
        for (AlarmWayEnums alarmWayEnum : alarmWayEnums) {
            if (alarmLevelEnum == null) {
                continue;
            }
            // 告警方式为短信告警
            if (alarmWayEnum == AlarmWayEnums.SMS) {
                String[] phones = MonitoringConfigPropertiesLoader.getMonitoringProperties().getAlarmProperties().getSmsProperties().getPhoneNumbers();
                monitorAlarmRecord.setNumber(StringUtils.join(phones, ";"));
            }
            // 告警方式为邮件告警
            else if (alarmWayEnum == AlarmWayEnums.MAIL) {
                String[] mails = MonitoringConfigPropertiesLoader.getMonitoringProperties().getAlarmProperties().getMailProperties().getEmills();
                monitorAlarmRecord.setNumber(StringUtils.join(mails, ";"));
            }
            monitorAlarmRecord.setWay(alarmWayEnum.name());
            monitorAlarmRecord.setInsertTime(new Date());
            this.monitorAlarmRecordDao.insert(monitorAlarmRecord);
        }
    }

    /**
     * <p>
     * 处理短信告警
     * </p>
     *
     * @param result         返回结果
     * @param title          告警信息标题
     * @param msg            告警信息
     * @param alarmLevelEnum 告警级别
     * @author 皮锋
     * @custom.date 2020年3月10日 下午3:13:35
     */
    private void dealSmsAlarm(Result result, String title, String msg, AlarmLevelEnums alarmLevelEnum) {
        Sms sms = Sms.builder()
                .phones(MonitoringConfigPropertiesLoader.getMonitoringProperties().getAlarmProperties().getSmsProperties().getPhoneNumbers())
                .title(title)
                .content(msg)
                .level(alarmLevelEnum != null ? alarmLevelEnum.name() : null)
                .build();
        boolean b = this.smsService.sendAlarmTemplateSms(sms);
        // 成功
        if (b) {
            this.wrapSuccessResult(result);
        }
        // 失败
        else {
            String expMsg = "发送短信失败！";
            this.wrapFailResult(result, expMsg);
        }
    }

    /**
     * <p>
     * 处理邮件告警
     * </p>
     *
     * @param result         返回结果
     * @param title          告警信息标题
     * @param msg            告警信息
     * @param alarmLevelEnum 告警级别
     * @author 皮锋
     * @custom.date 2020/4/13 13:23
     */
    private void dealMailAlarm(Result result, String title, String msg, AlarmLevelEnums alarmLevelEnum) {
        Mail mail = Mail.builder()
                .email(MonitoringConfigPropertiesLoader.getMonitoringProperties().getAlarmProperties().getMailProperties().getEmills())
                .title(title)
                .content(msg)
                .level(alarmLevelEnum != null ? alarmLevelEnum.name() : null)
                .build();
        boolean b = this.mailService.sendAlarmTemplateMail(mail);
        // 成功
        if (b) {
            this.wrapSuccessResult(result);
        }
        // 失败
        else {
            String expMsg = "发送电子邮件失败！";
            this.wrapFailResult(result, expMsg);
        }
    }

}
