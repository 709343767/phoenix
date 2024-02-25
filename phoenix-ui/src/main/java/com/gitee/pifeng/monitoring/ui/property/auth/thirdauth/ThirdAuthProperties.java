package com.gitee.pifeng.monitoring.ui.property.auth.thirdauth;

import com.gitee.pifeng.monitoring.ui.property.auth.thirdauth.cas.CasConfigurationProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * <p>
 * 第三方认证属性
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/3/13 11:33
 */
@Data
@ConfigurationProperties(prefix = "phoenix.auth.third-auth")
@EnableConfigurationProperties(CasConfigurationProperties.class)
public class ThirdAuthProperties {

    /**
     * 第三方认证类型枚举
     */
    private ThirdAuthTypeEnums type;

}
