package com.gitee.pifeng.monitoring.ui.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * cas + springSecurity安全访问配置。
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/3/11 17:28
 */
@Configuration
// 开启了第三方认证，而且第三方认证为cas
@ConditionalOnExpression("${third-auth.enable:false}&&'cas'.equalsIgnoreCase('${third-auth.type}')")
public class SpringSecurityCasConfig {
}
