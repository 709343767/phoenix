package com.gitee.pifeng.monitoring.ui.thirdauth.cas;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * CAS配置属性
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/3/11 16:51
 */
@Data
@ConfigurationProperties(prefix = "cas", ignoreUnknownFields = false)
public class CasConfigurationProperties {

    /**
     * 秘钥
     */
    @NotNull
    private String key;

    /**
     * cas服务端地址
     */
    @NotNull
    private String serverUrlPrefix;

    /**
     * cas登录地址
     */
    @NotNull
    private String serverLoginUrl;

    /**
     * cas登出地址
     */
    @NotNull
    private String serverLogoutUrl;

    /**
     * cas客户端地址
     */
    @NotNull
    private String clientHostUrl;

    /**
     * CAS协议验证类型
     */
    private ValidationTypeEnums validationType;
}
