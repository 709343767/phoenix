package com.gitee.pifeng.monitoring.ui.thirdauth.cas;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * CAS客户端配置属性
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/3/11 16:51
 */
@ConfigurationProperties(prefix = "cas", ignoreUnknownFields = false)
@Data
public class CasClientConfigurationProperties {

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
     * cas客户端地址
     */
    @NotNull
    private String clientHostUrl;

    /**
     * CAS协议验证类型
     */
    private ValidationTypeEnum validationType;
}
