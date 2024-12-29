package com.gitee.pifeng.monitoring.ui.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * WebMvc配置
 * </p>
 *
 * @author 皮锋
 * @custom.date 2024/9/25 9:12
 */
@Slf4j
@Profile("prod")
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * <p>
     * 处理静态资源
     * </p>
     *
     * @param registry {@link ResourceHandlerRegistry} 静态资源处理程序注册表
     * @author 皮锋
     * @custom.date 2024/9/25 9:31
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置静态资源文件的缓存策略
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .setCacheControl(CacheControl.maxAge(30, TimeUnit.DAYS).cachePublic());
        // 不缓存 /modules/ 文件夹下的资源
        // registry.addResourceHandler("/modules/**")
        //         .addResourceLocations("classpath:/static/modules/")
        //         .setCacheControl(CacheControl.noCache());

        // 解决访问接口文档doc.html页面404问题
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/")
                .setCacheControl(CacheControl.noCache());
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/")
                .setCacheControl(CacheControl.noCache());
        log.info("静态资源缓存策略配置成功！");
    }

}
