package com.imby.server.business.server.service;

import com.imby.server.business.server.domain.Mail;

/**
 * <p>
 * 邮箱服务接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/4/13 11:34
 */
public interface IMailService {

    /**
     * <p>
     * 发送HTML告警模板邮件
     * </p>
     *
     * @param mail 邮件实体对象
     * @return boolean
     * @author 皮锋
     * @custom.date 2020/4/13 11:37
     */
    boolean sendAlarmTemplateMail(Mail mail);

}
