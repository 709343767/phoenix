package com.gitee.pifeng.monitoring.ui.config.springsecurity;

import com.gitee.pifeng.monitoring.ui.exception.VerificationCodeException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * SpringSecurity认证失败处理器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2023/3/21 13:07
 */
@Component
@ConditionalOnBean(SpringSecurityConfig.class)
public class SpringSecurityAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    /**
     * <p>
     * 处理认证失败
     * </p>
     *
     * @param request   {@link HttpServletRequest}
     * @param response  {@link HttpServletResponse}
     * @param exception {@link AuthenticationException} 认证异常
     * @author 皮锋
     * @custom.date 2023/3/22 10:02
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        // 重定向的地址
        String url = "/login?error=true";
        if (exception instanceof VerificationCodeException) {
            String message = exception.getMessage();
            // 图形验证码不能为空
            if (StringUtils.equals(message, VerificationCodeException.VerificationCodeExceptionEnums.CANNOT_BE_EMPTY.getMsg())) {
                url = "/login?captchaCannotBeEmpty=true";
            }
            // 图形验证码不存在
            if (StringUtils.equals(message, VerificationCodeException.VerificationCodeExceptionEnums.DOES_NOT_EXIST.getMsg())) {
                url = "/login?captchaDoesNotExist=true";
            }
            // 图形验证码已过期
            if (StringUtils.equals(message, VerificationCodeException.VerificationCodeExceptionEnums.HAS_EXPIRED.getMsg())) {
                url = "/login?captchaHasExpired=true";
            }
            // 图形验证码校验失败
            if (StringUtils.equals(message, VerificationCodeException.VerificationCodeExceptionEnums.VERIFICATION_FAILED.getMsg())) {
                url = "/login?captchaVerificationFailed=true";
            }
        }
        super.setDefaultFailureUrl(url);
        super.onAuthenticationFailure(request, response, exception);
    }

}
