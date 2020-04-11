package com.transfar.server.business.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.transfar.common.constant.ResultMsgConstants;
import com.transfar.common.domain.Alarm;
import com.transfar.common.domain.Result;
import com.transfar.common.dto.AlarmPackage;
import com.transfar.server.business.server.dao.IMonitorAlarmDefinitionDao;
import com.transfar.server.business.server.domain.TransfarSms;
import com.transfar.server.business.server.entity.MonitorAlarmDefinition;
import com.transfar.server.business.server.service.IAlarmService;
import com.transfar.server.business.server.service.ISmsService;
import com.transfar.server.constant.AlarmWayEnums;
import com.transfar.server.constant.EnterpriseConstants;
import com.transfar.server.property.MonitoringServerWebProperties;
import com.transfar.server.util.AlarmUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        // 获取告警信息
        Alarm alarm = alarmPackage.getAlarm();
        // 处理告警消息
        this.dealAlarm(alarm, result);
        return result;
    }

    /**
     * <p>
     * 处理告警消息
     * </p>
     *
     * @param alarm  告警
     * @param result 返回结果
     * @author 皮锋
     * @custom.date 2020/4/2 11:49
     */
    private void dealAlarm(Alarm alarm, Result result) {
        // 告警开关是否打开
        boolean enable = this.config.getAlarmProperties().isEnable();
        if (!enable) {
            String msg = "告警开关没有打开，不发送告警消息！";
            log.warn(msg);
            result.setSuccess(false);
            result.setMsg(msg);
            // 停止往下执行
            return;
        }
        // 是测试告警信息，不做处理，直接返回
        if (alarm.isTest()) {
            String msg = "当前为测试信息，不发送告警消息！";
            log.warn(msg);
            result.setSuccess(false);
            result.setMsg(msg);
            // 停止往下执行
            return;
        }
        // 告警级别
        String level = alarm.getAlarmLevel().name();
        // 告警代码
        String code = alarm.getCode();
        // 如果有告警代码，查询数据库中此告警代码对应的告警级别，数据库中的告警级别优先级最高
        if (StringUtils.isNotBlank(code)) {
            try {
                LambdaQueryWrapper<MonitorAlarmDefinition> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(MonitorAlarmDefinition::getCode, code);
                MonitorAlarmDefinition monitorAlarmDefinition = this.monitorAlarmDefinitionDao
                        .selectOne(lambdaQueryWrapper);
                if (monitorAlarmDefinition != null) {
                    String dbLevel = monitorAlarmDefinition.getGrade();
                    if (StringUtils.isNotBlank(dbLevel)) {
                        level = dbLevel;
                    }
                }
            } catch (Exception e) {
                String msg = "根据告警代码从数据库中查询告警级别失败！";
                log.warn(msg);
                result.setSuccess(false);
                result.setMsg(msg);
                // 停止往下执行
                return;
            }
        }
        // 告警级别小于配置的告警级别，不做处理，直接返回
        String configAlarmLevel = this.config.getAlarmProperties().getLevel();
        if (!AlarmUtils.isAlarm(configAlarmLevel, level)) {
            String msg = "小于配置的告警级别，不发送告警消息！";
            log.warn(msg);
            result.setSuccess(false);
            result.setMsg(msg);
            // 停止往下执行
            return;
        }
        // 告警内容
        String msg = alarm.getMsg();
        // 没有告警内容，不做处理，直接返回
        if (StringUtils.isBlank(msg)) {
            String msg2 = "告警内容为空，不发送告警消息！";
            log.warn(msg2);
            result.setSuccess(false);
            result.setMsg(msg2);
            // 停止往下执行
            return;
        }
        // 告警内容标题
        String alarmTitle = alarm.getTitle();
        // 告警方式
        String alarmType = this.config.getAlarmProperties().getType();
        // 告警方式为短信告警
        if (StringUtils.equalsIgnoreCase(alarmType, AlarmWayEnums.SMS.name())) {
            // 处理短信告警
            this.dealSmsAlarm(alarmTitle, msg, level, result);
        } else if (StringUtils.equalsIgnoreCase(alarmType, AlarmWayEnums.MAIL.name())) {
            // 处理邮件告警
        }
    }

    /**
     * <p>
     * 处理短信告警
     * </p>
     *
     * @param alarmTitle 告警信息标题
     * @param msg        告警信息
     * @param level      告警级别
     * @param result     返回结果
     * @author 皮锋
     * @custom.date 2020年3月10日 下午3:13:35
     */
    private void dealSmsAlarm(String alarmTitle, String msg, String level, Result result) {
        // 短信接口商家
        String enterprise = this.config.getAlarmProperties().getSmsProperties().getEnterprise();
        // 判断短信接口商家，不同的商家调用不同的接口
        if (StringUtils.equalsIgnoreCase(EnterpriseConstants.TRANSFAR, enterprise)) {
            // 调用创发短信接口
            this.callTransfarSmsApi(alarmTitle, msg, level, result);
        }
    }

    /**
     * <p>
     * 封装数据，调用创发公司的短信接口发送短信
     * </p>
     *
     * @param alarmTitle 告警内容标题
     * @param msg        告警内容
     * @param level      告警级别
     * @param result     返回结果
     * @author 皮锋
     * @custom.date 2020年3月10日 下午3:19:36
     */
    private void callTransfarSmsApi(String alarmTitle, String msg, String level, Result result) {
        String[] phones = this.config.getAlarmProperties().getSmsProperties().getPhoneNumbers();
        String enterprise = this.config.getAlarmProperties().getSmsProperties().getEnterprise();
        TransfarSms transfarSms = TransfarSms.builder()//
                .content(StringUtils.isBlank(alarmTitle) ? msg : ("[" + alarmTitle + "]" + msg))//
                .type(level)//
                .phone(TransfarSms.getPhones(phones))//
                .identity(enterprise)//
                .build();
        // 创发公司短信接口
        String str = this.smsService.sendSmsByTransfarApi(transfarSms);
        // 短信发送成功
        boolean b = (!StringUtils.equalsIgnoreCase("null", str)) && StringUtils.isNotBlank(str);
        // 成功
        if (b) {
            result.setSuccess(true);
            result.setMsg(ResultMsgConstants.SUCCESS);
        }
        // 失败
        else {
            result.setSuccess(false);
            result.setMsg("调用创发公司的短信接口发送短信失败！");
        }
    }

}
