package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.gitee.pifeng.monitoring.common.constant.ResultMsgConstants;
import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmLevelEnums;
import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmWayEnums;
import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.server.business.server.core.MonitoringConfigPropertiesLoader;
import com.gitee.pifeng.monitoring.server.business.server.domain.Mail;
import com.gitee.pifeng.monitoring.server.business.server.domain.Sms;
import com.gitee.pifeng.monitoring.server.business.server.service.IMailService;
import com.gitee.pifeng.monitoring.server.business.server.service.ISmsService;
import com.gitee.pifeng.monitoring.server.business.server.service.ITemplateMsgSendFacadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 发送模板消息门面服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2023/5/24 8:19
 */
@Service
public class TemplateMsgSendFacadeServiceImpl implements ITemplateMsgSendFacadeService {

    /**
     * 监控配置属性加载器
     */
    @Autowired
    private MonitoringConfigPropertiesLoader monitoringConfigPropertiesLoader;

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
     * <p>
     * 发送模板文本消息
     * </p>
     *
     * @param alarmWay     告警方式
     * @param alarmLevel   告警级别
     * @param alarmTitle   告警标题
     * @param alarmContent 告警内容
     * @return {@link Result} 返回结果
     * @author 皮锋
     * @custom.date 2023/5/24 10:39
     */
    @Override
    public Result sendTemplateTextMsg(AlarmWayEnums alarmWay, AlarmLevelEnums alarmLevel, String alarmTitle, String alarmContent) {
        Result.ResultBuilder builder = Result.builder();
        switch (alarmWay) {
            case SMS:
                Sms sms = Sms.builder()
                        .phones(this.monitoringConfigPropertiesLoader.getMonitoringProperties().getAlarmProperties().getSmsProperties().getPhoneNumbers())
                        .title(alarmTitle)
                        .content(alarmContent)
                        .level(alarmLevel != null ? alarmLevel.name() : null)
                        .build();
                boolean smsSuc = this.smsService.sendAlarmTemplateSms(sms);
                builder.isSuccess(smsSuc).msg(smsSuc ? ResultMsgConstants.SUCCESS : "发送短信失败！");
                break;
            case MAIL:
                Mail mail = Mail.builder()
                        .email(this.monitoringConfigPropertiesLoader.getMonitoringProperties().getAlarmProperties().getMailProperties().getEmills())
                        .title(alarmTitle)
                        .content(alarmContent)
                        .level(alarmLevel != null ? alarmLevel.name() : null)
                        .build();
                boolean mailSuc = this.mailService.sendAlarmTemplateMail(mail);
                builder.isSuccess(mailSuc).msg(mailSuc ? ResultMsgConstants.SUCCESS : "发送电子邮件失败！");
                break;
            default:
                break;
        }
        return builder.build();
    }

}
