package com.gitee.pifeng.monitoring.ui.config;

import com.gitee.pifeng.monitoring.ui.thirdauth.cas.CasClientConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
@EnableConfigurationProperties(CasClientConfigurationProperties.class)
public class CasSecurityConfig {
}
