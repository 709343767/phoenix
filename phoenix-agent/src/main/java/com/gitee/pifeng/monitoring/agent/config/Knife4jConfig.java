package com.gitee.pifeng.monitoring.agent.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

/**
 * <p>
 * 配置knife4j，用于生成、描述、调用和可视化restful风格的web服务。
 * </p>
 * <a href="https://zhuanlan.zhihu.com/p/529772728?utm_id=0">从SpringFox迁移到SpringDoc</a>
 *
 * @author 皮锋
 * @custom.date 2019年10月30日 下午8:45:23
 */
@Configuration
public class Knife4jConfig {

    /**
     * <p>
     * 配置{@link OpenAPI}
     * </p>
     *
     * @param applicationName 应用程序名字
     * @param buildProperties 构建属性信息
     * @return {@link OpenAPI}
     * @author 皮锋
     * @custom.date 2023/7/12 16:07
     */
    @Bean
    public OpenAPI openApi(@Value("${spring.application.name}") String applicationName, ObjectProvider<BuildProperties> buildProperties) {
        return new OpenAPI().info(new Info()
                .title(applicationName)
                .contact(new Contact().name("皮锋"))
                .description("“phoenix”是一个灵活可配置的开源监控平台，主要用于监控应用程序、服务器、docker、数据库、网络、tcp端口和http接口，在发现异常时实时推送告警信息，并且提供了可视化系统进行配置、管理、查看。")
                .version(Optional.ofNullable(buildProperties.getIfAvailable()).map(BuildProperties::getVersion).orElse("1.0.0"))
                .termsOfService("https://gitee.com/monitoring-platform")
                .license(new License().name("GPL-3.0").url("https://spdx.org/licenses/GPL-3.0.html")));
    }

}
