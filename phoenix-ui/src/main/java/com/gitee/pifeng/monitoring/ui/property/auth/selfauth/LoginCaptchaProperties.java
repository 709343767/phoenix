package com.gitee.pifeng.monitoring.ui.property.auth.selfauth;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * 登录验证码属性
 * </p>
 *
 * @author 皮锋
 * @custom.date 2024/2/25 18:47
 */
@Data
@ConfigurationProperties(prefix = "phoenix.auth.self-auth.login-captcha", ignoreUnknownFields = false)
public class LoginCaptchaProperties {

    /**
     * 是否启用登录验证码
     */
    private Boolean enable;

}
