package com.transfar.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <p>
 * 配置Swagger，用于生成、描述、调用和可视化restful风格的web服务
 * </p>
 *
 * @author 皮锋
 * @custom.date 2019年10月30日 下午8:45:23
 */
@Configuration
@EnableSwagger2
@Slf4j
public class SwaggerConfig {

    /**
     * Swagger要扫描的包路径
     */
    private static final String BASE_PACKAGE = "com.transfar.server.business";

    /**
     * <p>
     * 创建rest风格的Swagger api
     * </p>
     *
     * @return Docket
     * @author 皮锋
     * @custom.date 2020年1月20日 上午10:28:13
     */
    @Bean
    public Docket createRestApi() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
                // 包路径
                .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
                //
                .paths(PathSelectors.any())
                //
                .build();
        log.info("Swagger配置成功！");
        return docket;
        // return new Docket(DocumentationType.SWAGGER_2)//
        // .apiInfo(apiInfo())//
        // .select()//
        // .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))//
        // .build();
    }

    /**
     * <p>
     * 构建详细api文档信息，包括标题、版本号、描述
     * </p>
     *
     * @return ApiInfo
     * @author 皮锋
     * @custom.date 2020年1月20日 上午9:58:47
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 页面标题
                .title("监控服务端程序")
                // 版本号
                .version("0.0.1")
                // 描述
                .description("与监控代理程序或监控客户端程序通信，获取监控信息，维护被监控的应用列表，并且展示监控信息，发送告警通知。")
                // 构建
                .build();
    }
}
