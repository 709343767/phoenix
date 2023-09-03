package com.gitee.pifeng.monitoring.ui.config.springsecurity;

import cn.hutool.captcha.ICaptcha;
import com.gitee.pifeng.monitoring.ui.constant.CaptchaConstants;
import com.gitee.pifeng.monitoring.ui.exception.VerificationCodeException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * <p>
 * SpringSecurity专门用于校验验证码的过滤器，已经废弃，已使用自定义认证这种更为优雅的方式实现
 * </p>
 *
 * @author 皮锋
 * @custom.date 2023/3/21 8:49
 */
@Deprecated
// @Component
public class SpringSecurityVerificationCodeFilter extends OncePerRequestFilter {

    /**
     * SpringSecurity认证失败处理器
     */
    // @Autowired
    private SpringSecurityAuthenticationFailureHandler authenticationFailureHandler;

    /**
     * Same contract as for {@code doFilter}, but guaranteed to be
     * just invoked once per request within a single request thread.
     * See {@link #shouldNotFilterAsyncDispatch()} for details.
     * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
     * default ServletRequest and ServletResponse ones.
     *
     * @param request     {@link HttpServletRequest}
     * @param response    {@link HttpServletResponse}
     * @param filterChain 过滤器链 {@link FilterChain}
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 只有登录请求校验验证码
        if (StringUtils.contains(request.getRequestURI(), "/doLogin")
                && HttpMethod.POST.matches(request.getMethod())) {
            try {
                // 校验验证码
                this.verificationCode(request);
            } catch (VerificationCodeException e) {
                this.authenticationFailureHandler.onAuthenticationFailure(request, response, e);
            }
        }
        filterChain.doFilter(request, response);
    }

    /**
     * <p>
     * 校验验证码
     * </p>
     *
     * @param request {@link HttpServletRequest}
     * @author 皮锋
     * @custom.date 2023/3/21 12:33
     */
    private void verificationCode(HttpServletRequest request) throws VerificationCodeException {
        String requestCode = request.getParameter(CaptchaConstants.CAPTCHA);
        HttpSession session = request.getSession();
        ICaptcha captcha = (ICaptcha) session.getAttribute(CaptchaConstants.CAPTCHA);
        LocalDateTime captchaExpireTime = (LocalDateTime) session.getAttribute(CaptchaConstants.CAPTCHA_EXPIRE_TIME);
        // 随手清除验证码，无论是失败还是成功，客户端在登录失败时刷新验证码
        if (captcha != null) {
            session.removeAttribute(CaptchaConstants.CAPTCHA);
        }
        if (captchaExpireTime != null) {
            session.removeAttribute(CaptchaConstants.CAPTCHA_EXPIRE_TIME);
        }
        // 校验不通过，抛出异常
        if (StringUtils.isEmpty(requestCode)) {
            throw new VerificationCodeException(VerificationCodeException.VerificationCodeExceptionEnums.CANNOT_BE_EMPTY);
        }
        if (captcha == null || captchaExpireTime == null) {
            throw new VerificationCodeException(VerificationCodeException.VerificationCodeExceptionEnums.DOES_NOT_EXIST);
        }
        if (LocalDateTime.now().isAfter(captchaExpireTime)) {
            throw new VerificationCodeException(VerificationCodeException.VerificationCodeExceptionEnums.HAS_EXPIRED);
        }
        if (!captcha.verify(requestCode)) {
            throw new VerificationCodeException(VerificationCodeException.VerificationCodeExceptionEnums.VERIFICATION_FAILED);
        }
    }

}
