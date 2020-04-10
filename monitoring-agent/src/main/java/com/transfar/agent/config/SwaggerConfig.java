package com.transfar.agent.config;

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
    private static final String BASE_PACKAGE = "com.transfar.agent.business";

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
     * @return {@link ApiInfo}
     * @author 皮锋
     * @custom.date 2020年1月20日 上午9:58:47
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 页面标题
                .title("监控代理程序")
                // 版本号
                .version("0.0.1")
                // 描述
                .description("1.把请求到此监控代理程序的请求向上传递到服务端；2.定时向服务端发送心跳包；3.定时向服务端发送此监控代理程序所在服务器的服务器信息。")
                // 构建
                .build();
    }
}
