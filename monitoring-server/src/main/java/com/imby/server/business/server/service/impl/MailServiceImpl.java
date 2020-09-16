package com.imby.server.business.server.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.imby.server.business.server.domain.Mail;
import com.imby.server.business.server.service.IMailService;

import javax.mail.internet.MimeMessage;

/**
 * <p>
 * 邮箱服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/4/13 11:34
 */
@Service
@Slf4j
public class MailServiceImpl implements IMailService {

    /**
     * template模板引擎
     */
    @Autowired
    private TemplateEngine templateEngine;

    /**
     * Spring Boot提供了一个发送邮件的简单抽象，使用的是下面这个接口，这里直接注入即可使用
     */
    @Autowired
    private JavaMailSender mailSender;

    /**
     * 发件人的邮箱
     */
    @Value("${spring.mail.username}")
    private String from;

    /**
     * <p>
     * 发送HTML告警模板邮件
     * </p>
     *
     * @param mail 邮件实体对象
     * @return boolean
     * @author 皮锋
     * @custom.date 2020/4/13 11:40
     */
    @Override
    public boolean sendAlarmTemplateMail(Mail mail) {
        try {
            MimeMessage mimeMessage = this.mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            // 发送人的邮箱
            messageHelper.setFrom(this.from);
            //发给谁，对方邮箱
            messageHelper.setTo(mail.getEmail());
            // 标题
            messageHelper.setSubject(mail.getTitle());
            // 使用模板thymeleaf
            Context context = new Context();
            //定义模板数据
            context.setVariable("title", mail.getTitle());
            context.setVariable("content", mail.getContent());
            context.setVariable("level", mail.getLevel());
            context.setVariables(mail.getAttachment());
            // 获取thymeleaf的html模板
            String emailContent = this.templateEngine.process("mail/mail-alarm-template", context);
            log.info("告警邮件HTML：\r\n{}", emailContent);
            messageHelper.setText(emailContent, true);
            // 发送邮件
            this.mailSender.send(mimeMessage);
            return true;
        } catch (Exception e) {
            log.error("HTML模板邮件发送失败！", e);
            return false;
        }
    }
}
