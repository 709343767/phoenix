package com.gitee.pifeng.monitoring.ui.thirdauth.property;

import com.gitee.pifeng.monitoring.ui.thirdauth.cas.CasConfigurationProperties;
import com.gitee.pifeng.monitoring.ui.thirdauth.constant.ThirdAuthTypeEnums;
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
@ConfigurationProperties(prefix = "third-auth", ignoreUnknownFields = false)
@EnableConfigurationProperties(CasConfigurationProperties.class)
public class ThirdAuthProperties {

    /**
     * 是否启用第三方认证
     */
    private Boolean enable;

    /**
     * 第三方认证类型枚举
     */
    private ThirdAuthTypeEnums type;

}
