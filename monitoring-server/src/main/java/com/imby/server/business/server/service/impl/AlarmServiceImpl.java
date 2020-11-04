package com.imby.server.business.server.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.imby.common.constant.ResultMsgConstants;
import com.imby.common.constant.ZeroOrOneConstants;
import com.imby.common.domain.Alarm;
import com.imby.common.domain.Result;
import com.imby.common.dto.AlarmPackage;
import com.imby.server.business.server.dao.IMonitorAlarmDefinitionDao;
import com.imby.server.business.server.dao.IMonitorAlarmRecordDao;
import com.imby.server.business.server.domain.Mail;
import com.imby.server.business.server.domain.TransfarSms;
import com.imby.server.business.server.entity.MonitorAlarmDefinition;
import com.imby.server.business.server.entity.MonitorAlarmRecord;
import com.imby.server.business.server.property.MonitoringServerWebProperties;
import com.imby.server.business.server.service.IAlarmService;
import com.imby.server.business.server.service.IMailService;
import com.imby.server.business.server.service.ISmsService;
import com.imby.server.constant.AlarmWayEnums;
import com.imby.server.constant.EnterpriseConstants;
import com.imby.server.util.AlarmUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
     * 监控配置属性
     */
    @Autowired
    private MonitoringServerWebProperties config;

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
     * 处理告警包
     * </p>
     *
     * @param alarmPackage 告警包
     * @return {@link Result}
     * @author 皮锋
     * @custom.date 2020年3月10日 下午1:33:55
     */
    @Override
    public Result dealAlarmPackage(AlarmPackage alarmPackage) {
        // 返回结果
        Result result = new Result();
        // 处理告警信息
        this.dealAlarm(result, alarmPackage);
        return result;
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
        // 告警ID
        String alarmUuid = alarmPackage.getId();
        // 告警类型
        String alarmType = alarm.getAlarmType().name();
        // 告警级别
        String alarmLevel = alarm.getAlarmLevel().name();
        // 告警内容标题
        String alarmTitle = alarm.getTitle();
        // 告警内容
        String alarmMsg = alarm.getMsg();
        // 告警方式
        String[] alarmWays = this.config.getAlarmProperties().getWay();
        // 是否发送告警信息
        if (!this.isSendAlarm(result, alarm)) {
            return;
        }
        // 告警代码
        String code = alarm.getCode();
        // 如果有告警代码，查询数据库中此告警代码对应的告警级别、告警标题、告警内容，数据库中有就用数据库的
        if (StringUtils.isNotBlank(code)) {
            try {
                LambdaQueryWrapper<MonitorAlarmDefinition> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(MonitorAlarmDefinition::getCode, code);
                MonitorAlarmDefinition monitorAlarmDefinition = this.monitorAlarmDefinitionDao.selectOne(lambdaQueryWrapper);
                if (monitorAlarmDefinition != null) {
                    // 数据库中的告警级别
                    String dbLevel = monitorAlarmDefinition.getGrade();
                    if (StringUtils.isNotBlank(dbLevel)) {
                        alarmLevel = dbLevel;
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
        // 发送告警以及把告警记录计入数据库
        this.sendAlarmAndOperateDb(result, alarmUuid, alarmType, alarmLevel, alarmTitle, alarmMsg, alarmWays);
    }

    /**
     * <p>
     * 发送告警以及把告警记录计入数据库。
     * </p>
     * 1.把告警信息存入数据库；<br>
     * 2.发送告警；<br>
     * 3.更新数据库中的告警发送结果。<br>
     *
     * @param result     返回结果
     * @param alarmUuid  告警ID
     * @param alarmType  告警类型
     * @param alarmLevel 告警级别
     * @param alarmTitle 告警内容标题
     * @param alarmMsg   告警内容
     * @param alarmWays  告警方式
     * @author 皮锋
     * @custom.date 2020/9/14 12:56
     */
    private void sendAlarmAndOperateDb(Result result,
                                       String alarmUuid, String alarmType,
                                       String alarmLevel, String alarmTitle,
                                       String alarmMsg, String[] alarmWays) {
        // 发送告警前先把告警记录存入数据库
        int insertResult = this.insertMonitorAlarmRecordToDb(alarmUuid, alarmType, alarmLevel, alarmTitle, alarmMsg, alarmWays);
        if (insertResult > 0) {
            List<String> alarmTypeList = Arrays.asList(alarmWays);
            // 告警方式为短信告警和邮件告警
            if (alarmTypeList.contains(AlarmWayEnums.SMS.name()) && alarmTypeList.contains(AlarmWayEnums.MAIL.name())) {
                // 处理短信告警
                this.dealSmsAlarm(result, alarmTitle, alarmMsg, alarmLevel);
                // 告警发送完更新数据库中告警发送结果
                this.updateMonitorAlarmRecordToDb(result, alarmUuid, AlarmWayEnums.SMS);
                // 处理邮件告警
                this.dealMailAlarm(result, alarmTitle, alarmMsg, alarmLevel);
                // 告警发送完更新数据库中告警发送结果
                this.updateMonitorAlarmRecordToDb(result, alarmUuid, AlarmWayEnums.MAIL);
            } else if (alarmTypeList.contains(AlarmWayEnums.SMS.name())) {
                // 处理短信告警
                this.dealSmsAlarm(result, alarmTitle, alarmMsg, alarmLevel);
                // 告警发送完更新数据库中告警发送结果
                this.updateMonitorAlarmRecordToDb(result, alarmUuid, AlarmWayEnums.SMS);
            } else if (alarmTypeList.contains(AlarmWayEnums.MAIL.name())) {
                // 处理邮件告警
                this.dealMailAlarm(result, alarmTitle, alarmMsg, alarmLevel);
                // 告警发送完更新数据库中告警发送结果
                this.updateMonitorAlarmRecordToDb(result, alarmUuid, AlarmWayEnums.MAIL);
            }
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
        boolean isEnable = this.config.getAlarmProperties().isEnable();
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
        String configAlarmLevel = this.config.getAlarmProperties().getLevel();
        // 告警级别
        String level = alarm.getAlarmLevel().name();
        // 告警级别小于配置的告警级别，不做处理，直接返回
        if (!AlarmUtils.isAlarm(configAlarmLevel, level)) {
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
        String[] alarmWays = this.config.getAlarmProperties().getWay();
        // 没有配置告警方式，不做处理，直接返回
        if (ArrayUtil.isEmpty(alarmWays)) {
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
     * @param uuid          UUID，唯一不重复，可用作主键
     * @param alarmWayEnums 告警方式
     * @author 皮锋
     * @custom.date 2020/5/13 17:04
     */
    private void updateMonitorAlarmRecordToDb(Result result, String uuid, AlarmWayEnums alarmWayEnums) {
        MonitorAlarmRecord monitorAlarmRecord = new MonitorAlarmRecord();
        // 告警方式为短信
        if (alarmWayEnums == AlarmWayEnums.SMS) {
            // 更新状态
            if (result.isSuccess()) {
                monitorAlarmRecord.setSmsStatus(ZeroOrOneConstants.ONE);
            } else {
                monitorAlarmRecord.setSmsStatus(ZeroOrOneConstants.ZERO);
            }
        }
        // 告警方式为邮件
        if (alarmWayEnums == AlarmWayEnums.MAIL) {
            // 更新状态
            if (result.isSuccess()) {
                monitorAlarmRecord.setMailStatus(ZeroOrOneConstants.ONE);
            } else {
                monitorAlarmRecord.setMailStatus(ZeroOrOneConstants.ZERO);
            }
        }
        // 更新时间
        monitorAlarmRecord.setUpdateTime(new Date());
        LambdaUpdateWrapper<MonitorAlarmRecord> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(MonitorAlarmRecord::getCode, uuid);
        this.monitorAlarmRecordDao.update(monitorAlarmRecord, lambdaUpdateWrapper);
    }

    /**
     * <p>
     * 把告警信息插入数据库
     * </p>
     *
     * @param uuid       UUID，唯一不重复，可用作主键
     * @param alarmType  告警类型（SERVER、NET、INSTANCE、CUSTOM）
     * @param alarmLevel 告警级别（INFO、WARM、ERROR、FATAL）
     * @param alarmTitle 告警标题
     * @param alarmMsg   告警内容
     * @param alarmWays  告警方式（SMS、MAIL）
     * @return 插入记录数
     * @author 皮锋
     * @custom.date 2020/5/13 16:21
     */
    private int insertMonitorAlarmRecordToDb(String uuid, String alarmType,
                                             String alarmLevel, String alarmTitle,
                                             String alarmMsg, String[] alarmWays) {
        MonitorAlarmRecord monitorAlarmRecord = new MonitorAlarmRecord();
        monitorAlarmRecord.setCode(uuid);
        monitorAlarmRecord.setTitle(alarmTitle);
        monitorAlarmRecord.setContent(alarmMsg);
        monitorAlarmRecord.setLevel(alarmLevel);
        monitorAlarmRecord.setType(alarmType);
        monitorAlarmRecord.setWay(StringUtils.join(alarmWays, ";"));
        List<String> alarmTypeList = Arrays.asList(alarmWays);
        // 告警方式为短信告警和邮件告警
        if (alarmTypeList.contains(AlarmWayEnums.SMS.name()) && alarmTypeList.contains(AlarmWayEnums.MAIL.name())) {
            String[] phones = this.config.getAlarmProperties().getSmsProperties().getPhoneNumbers();
            String[] mails = this.config.getAlarmProperties().getMailProperties().getEmill();
            monitorAlarmRecord.setPhone(StringUtils.join(phones, ";"));
            monitorAlarmRecord.setMail(StringUtils.join(mails, ";"));
        }
        // 告警方式为短信告警
        else if (alarmTypeList.contains(AlarmWayEnums.SMS.name())) {
            String[] phones = this.config.getAlarmProperties().getSmsProperties().getPhoneNumbers();
            monitorAlarmRecord.setPhone(StringUtils.join(phones, ";"));
        }
        // 告警方式为邮件告警
        else if (alarmTypeList.contains(AlarmWayEnums.MAIL.name())) {
            String[] mails = this.config.getAlarmProperties().getMailProperties().getEmill();
            monitorAlarmRecord.setMail(StringUtils.join(mails, ";"));
        }
        monitorAlarmRecord.setInsertTime(new Date());
        return this.monitorAlarmRecordDao.insert(monitorAlarmRecord);
    }

    /**
     * <p>
     * 处理短信告警
     * </p>
     *
     * @param result     返回结果
     * @param alarmTitle 告警信息标题
     * @param msg        告警信息
     * @param level      告警级别
     * @author 皮锋
     * @custom.date 2020年3月10日 下午3:13:35
     */
    private void dealSmsAlarm(Result result, String alarmTitle, String msg, String level) {
        // 短信接口商家
        String enterprise = this.config.getAlarmProperties().getSmsProperties().getEnterprise();
        // 判断短信接口商家，不同的商家调用不同的接口
        if (StringUtils.equalsIgnoreCase(EnterpriseConstants.TRANSFAR, enterprise)) {
            // 调用创发短信接口
            this.callTransfarSmsApi(result, alarmTitle, msg, level);
        }
    }

    /**
     * <p>
     * 封装数据，调用创发公司的短信接口发送短信
     * </p>
     *
     * @param result     返回结果
     * @param alarmTitle 告警内容标题
     * @param msg        告警内容
     * @param level      告警级别
     * @author 皮锋
     * @custom.date 2020年3月10日 下午3:19:36
     */
    private void callTransfarSmsApi(Result result, String alarmTitle, String msg, String level) {
        String[] phones = this.config.getAlarmProperties().getSmsProperties().getPhoneNumbers();
        String enterprise = this.config.getAlarmProperties().getSmsProperties().getEnterprise();
        // 短信不支持<br>标签换行
        String text = msg.replace("<br>", "");
        TransfarSms transfarSms = TransfarSms.builder()
                .content(StringUtils.isBlank(alarmTitle) ? text : ("[" + alarmTitle + "]" + text))
                .type(level)
                .phone(TransfarSms.getPhones(phones))
                .identity(enterprise)
                .build();
        // 创发公司短信接口
        boolean b = this.smsService.sendSmsByTransfarApi(transfarSms);
        // 成功
        if (b) {
            this.wrapSuccessResult(result);
        }
        // 失败
        else {
            String expMsg = "调用创发公司的短信接口发送短信失败！";
            this.wrapFailResult(result, expMsg);
        }
    }

    /**
     * <p>
     * 处理邮件告警
     * </p>
     *
     * @param result     返回结果
     * @param alarmTitle 告警信息标题
     * @param msg        告警信息
     * @param level      告警级别
     * @author 皮锋
     * @custom.date 2020/4/13 13:23
     */
    private void dealMailAlarm(Result result, String alarmTitle, String msg, String level) {
        Mail mail = Mail.builder()
                .email(this.config.getAlarmProperties().getMailProperties().getEmill())
                .title(alarmTitle)
                .content(msg)
                .level(level)
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
