package com.gitee.pifeng.monitoring.ui.config.springsecurity;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * SpringSecurity Web身份验证信息数据源
 * </p>
 *
 * @author 皮锋
 * @custom.date 2023/4/18 8:25
 */
@Component
public class SpringSecurityWebAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, SpringSecurityWebAuthenticationDetails> {

    @Override
    public SpringSecurityWebAuthenticationDetails buildDetails(HttpServletRequest request) {
        return new SpringSecurityWebAuthenticationDetails(request);
    }

}
