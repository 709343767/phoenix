package com.gitee.pifeng.monitoring.ui.property.auth;

import com.gitee.pifeng.monitoring.ui.property.auth.selfauth.SelfAuthProperties;
import com.gitee.pifeng.monitoring.ui.property.auth.thirdauth.ThirdAuthProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * <p>
 * 认证属性
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/3/13 11:33
 */
@Data
@ConfigurationProperties(prefix = "phoenix.auth")
@EnableConfigurationProperties({SelfAuthProperties.class, ThirdAuthProperties.class})
public class AuthProperties {

    /**
     * 认证类型枚举，默认自己认证
     */
    private AuthTypeEnums type = AuthTypeEnums.SELF;

}