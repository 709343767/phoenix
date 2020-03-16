package com.transfar.server.business.server.service.impl;

import com.transfar.common.dto.AlarmPackage;
import com.transfar.server.business.server.domain.TransfarSms;
import com.transfar.server.business.server.service.IAlarmService;
import com.transfar.server.business.server.service.ISmsService;
import com.transfar.server.constant.AlarmTypeEnums;
import com.transfar.server.constant.EnterpriseConstants;
import com.transfar.server.property.MonitoringServerWebProperties;

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
     * <p>
     * 处理告警包
     * </p>
     *
     * @param alarmPackage 告警包
     * @return Boolean
     * @author 皮锋
     * @custom.date 2020年3月10日 下午1:33:55
     */
    @Override
    public Boolean dealAlarmPackage(AlarmPackage alarmPackage) {
        // 返回结果
        boolean result = false;
        //告警开关是否打开
        boolean enable = config.getAlarmProperties().isEnable();
        if (!enable) {
            // 停止往下执行
            return false;
        }
        // 是测试告警信息，不做处理，直接返回true，代表告警成功
        if (alarmPackage.isTest()) {
            // 停止往下执行
            return true;
        }
        // 告警级别
        String level = alarmPackage.getLevel();
        // 告警内容
        String msg = alarmPackage.getMsg();
        // 没有告警内容，不做处理，直接返回false
        if (StringUtils.isBlank(msg)) {
            // 停止往下执行
            return false;
        }
        // 告警内容标题
        String alarmTitle = alarmPackage.getTitle();
        // 告警方式
        String alarmType = this.config.getAlarmProperties().getType();
        // 告警方式为短信告警
        if (StringUtils.equalsIgnoreCase(alarmType, AlarmTypeEnums.SMS.name())) {
            // 处理短信告警
            result = this.dealSmsAlarm(alarmTitle, msg, level);
        } else if (StringUtils.equalsIgnoreCase(alarmType, AlarmTypeEnums.MAIL.name())) {
            // 处理邮件告警
        }
        return result;
    }

    /**
     * <p>
     * 处理短信告警
     * </p>
     *
     * @param alarmTitle 告警信息标题
     * @param msg        告警信息
     * @param level      告警级别
     * @return boolean
     * @author 皮锋
     * @custom.date 2020年3月10日 下午3:13:35
     */
    private boolean dealSmsAlarm(String alarmTitle, String msg, String level) {
        // 返回结果
        boolean result = false;
        // 短信接口商家
        String enterprise = this.config.getAlarmProperties().getSmsProperties().getEnterprise();
        // 判断短信接口商家，不同的商家调用不同的接口
        if (StringUtils.equalsIgnoreCase(EnterpriseConstants.TRANSFAR, enterprise)) {
            // 调用创发短信接口
            result = this.callTransfarSmsApi(alarmTitle, msg, level);
        }
        return result;
    }

    /**
     * <p>
     * 封装数据，调用创发公司的短信接口发送短信
     * </p>
     *
     * @param alarmTitle 告警内容标题
     * @param msg        告警内容
     * @param level      告警级别
     * @return boolean
     * @author 皮锋
     * @custom.date 2020年3月10日 下午3:19:36
     */
    private boolean callTransfarSmsApi(String alarmTitle, String msg, String level) {
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
        return (!StringUtils.equalsIgnoreCase("null", str)) && StringUtils.isNotBlank(str);
    }

}
