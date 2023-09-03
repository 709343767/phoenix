package com.gitee.pifeng.monitoring.ui.config.springsecurity;

import cn.hutool.captcha.ICaptcha;
import com.gitee.pifeng.monitoring.ui.constant.CaptchaConstants;
import lombok.Getter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

/**
 * <p>
 * SpringSecurity Web身份验证信息持有者
 * </p>
 *
 * @author 皮锋
 * @custom.date 2023/4/18 8:17
 */
@Getter
public class SpringSecurityWebAuthenticationDetails extends WebAuthenticationDetails {

    private static final long serialVersionUID = 6676973841211763574L;

    /**
     * 前端传过来的验证码
     */
    private String captcha;

    /**
     * 保存在session中的验证码
     */
    private ICaptcha savedCaptcha;

    /**
     * 验证码超时时长
     */
    private LocalDateTime captchaExpireTime;

    /**
     * <p>
     * 补充用户提交的验证码和session中保存的验证码以及验证码超时时长
     * </p>
     *
     * @param request {@link HttpServletRequest}
     * @author 皮锋
     * @custom.date 2023/4/18 8:19
     */
    public SpringSecurityWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        this.captcha = request.getParameter(CaptchaConstants.CAPTCHA);
        HttpSession session = request.getSession();
        this.savedCaptcha = (ICaptcha) session.getAttribute(CaptchaConstants.CAPTCHA);
        // 随手清除验证码，无论是失败还是成功，客户端在登录失败时刷新验证码
        if (this.savedCaptcha != null) {
            session.removeAttribute(CaptchaConstants.CAPTCHA);
        }
        this.captchaExpireTime = (LocalDateTime) session.getAttribute(CaptchaConstants.CAPTCHA_EXPIRE_TIME);
        if (this.captchaExpireTime != null) {
            session.removeAttribute(CaptchaConstants.CAPTCHA_EXPIRE_TIME);
        }
    }

}
