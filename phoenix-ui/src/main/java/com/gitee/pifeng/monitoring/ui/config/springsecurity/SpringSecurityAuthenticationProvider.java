package com.gitee.pifeng.monitoring.ui.config.springsecurity;

import cn.hutool.captcha.ICaptcha;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorUserService;
import com.gitee.pifeng.monitoring.ui.exception.VerificationCodeException;
import com.gitee.pifeng.monitoring.ui.property.auth.selfauth.LoginCaptchaProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 * SpringSecurity身份认证提供者
 * </p>
 *
 * @author 皮锋
 * @custom.date 2023/4/18 8:02
 */
@Component
@ConditionalOnBean(SpringSecurityConfig.class)
public class SpringSecurityAuthenticationProvider extends DaoAuthenticationProvider {

    /**
     * 登录验证码属性
     */
    @Autowired
    private LoginCaptchaProperties loginCaptchaProperties;

    /**
     * <p>
     * 构造方法注入 {@link UserDetailsService} 和 {@link PasswordEncoder}
     * </p>
     *
     * @param monitorUserService 监控用户服务类
     * @param passwordEncoder    密码编码器
     * @author 皮锋
     * @custom.date 2023/4/18 8:06
     */
    public SpringSecurityAuthenticationProvider(IMonitorUserService monitorUserService, PasswordEncoder passwordEncoder) {
        this.setUserDetailsService(monitorUserService);
        this.setPasswordEncoder(passwordEncoder);
    }

    /**
     * <p>
     * 身份认证检查
     * </p>
     *
     * @param userDetails    用户信息
     * @param authentication 身份验证令牌
     * @author 皮锋
     * @custom.date 2023/4/18 8:10
     */
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) {
        // 是否启用登录验证码
        if (Objects.equals(this.loginCaptchaProperties.getEnable(), true)) {
            // 获取验证码
            SpringSecurityWebAuthenticationDetails details = (SpringSecurityWebAuthenticationDetails) authentication.getDetails();
            // 用户传过来的验证码
            String captcha = details.getCaptcha();
            // 保存在session中的验证码
            ICaptcha savedCaptcha = details.getSavedCaptcha();
            // 保存在session中的验证码超时时长
            LocalDateTime captchaExpireTime = details.getCaptchaExpireTime();
            // 校验不通过，抛出异常
            if (StringUtils.isEmpty(captcha)) {
                throw new VerificationCodeException(VerificationCodeException.VerificationCodeExceptionEnums.CANNOT_BE_EMPTY);
            }
            if (savedCaptcha == null || captchaExpireTime == null) {
                throw new VerificationCodeException(VerificationCodeException.VerificationCodeExceptionEnums.DOES_NOT_EXIST);
            }
            if (LocalDateTime.now().isAfter(captchaExpireTime)) {
                throw new VerificationCodeException(VerificationCodeException.VerificationCodeExceptionEnums.HAS_EXPIRED);
            }
            if (!savedCaptcha.verify(captcha)) {
                throw new VerificationCodeException(VerificationCodeException.VerificationCodeExceptionEnums.VERIFICATION_FAILED);
            }
        }
        // 调用父类方法继续完成密码验证
        super.additionalAuthenticationChecks(userDetails, authentication);
    }

}
