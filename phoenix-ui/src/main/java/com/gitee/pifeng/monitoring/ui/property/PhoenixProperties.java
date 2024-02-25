package com.gitee.pifeng.monitoring.ui.property;

import com.gitee.pifeng.monitoring.ui.property.auth.AuthProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * <p>
 * phoenix配置属性
 * </p>
 *
 * @author 皮锋
 * @custom.date 2024/2/23 17:12
 */
@Data
@ConfigurationProperties(prefix = "phoenix")
@EnableConfigurationProperties(AuthProperties.class)
public class PhoenixProperties {
}
