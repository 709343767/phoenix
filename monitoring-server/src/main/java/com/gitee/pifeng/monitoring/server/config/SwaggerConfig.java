package com.gitee.pifeng.monitoring.server.config;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <p>
 * 配置Swagger，用于生成、描述、调用和可视化restful风格的web服务。
 * </p>
 *
 * @author 皮锋
 * @custom.date 2019年10月30日 下午8:45:23
 */
@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
@Slf4j
@Profile({"dev", "test"})
public class SwaggerConfig {

    /**
     * Swagger要扫描的包路径
     */
    private static final String BASE_PACKAGE = "com.gitee.pifeng.monitoring.server.business";

    /**
     * <p>
     * 创建rest风格的Swagger api
     * </p>
     *
     * @return {@link Docket}
     * @author 皮锋
     * @custom.date 2020年1月20日 上午10:28:13
     */
    @Bean
    public Docket createRestApi() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                // 分组名称（swagger-ui-layer不支持分组）
                // .groupName("1.x")
                //
                .select()
                // 包路径
                .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
                //
                .paths(PathSelectors.any())
                //
                .build();
        log.info("Swagger配置成功！");
        return docket;
    }

    /**
     * <p>
     * 构建详细api文档信息，包括标题、版本号、描述
     * </p>
     *
     * @return {@link ApiInfo}
     * @author 皮锋
     * @custom.date 2020年1月20日 上午9:58:47
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 页面标题
                .title("监控服务端程序")
                // 作者
                .contact(new Contact("皮锋", "https://gitee.com/monitoring-platform", "709343767@qq.com"))
                // 服务Url
                .termsOfServiceUrl("无")
                // 版本号
                .version("LATEST-SNAPSHOT")
                // 描述
                .description("“monitoring”是一个灵活可配置的开源监控平台，主要用于监控应用程序、服务器、数据库和网络，通过实时收集、汇聚和分析监控信息，实现在发现异常时立刻推送告警信息，并且提供了可视化系统进行配置、管理、查看。持续更新中...")
                // 构建
                .build();
    }

}
