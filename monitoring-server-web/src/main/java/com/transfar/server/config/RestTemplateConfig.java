package com.transfar.server.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;

/**
 * <p>
 * 配置RestTemplate，RestTemplate是Spring提供的用于访问Rest服务的客户端，提供了多种便捷访问远程Http服务的方法，能够大大提高客户端的编写效率
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年1月20日 上午10:58:31
 */
@Configuration
@Slf4j
public class RestTemplateConfig implements WebMvcConfigurer {

    /**
     * <p>
     * 构造RestTemplate实例，把RestTemplate实例作为一个JavaBean交给Spring管理
     * </p>
     *
     * @return {@link RestTemplate}
     * @author 皮锋
     * @custom.date 2020年3月4日 下午3:46:33
     */
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplateBuilder()//
                // .basicAuthorization("username", "password")
                // 5秒
                .setConnectTimeout(Duration.ofSeconds(5))
                // 5秒
                .setReadTimeout(Duration.ofSeconds(5))//
                .build();
        log.info("RestTemplate配置成功！");
        return restTemplate;
    }

    /**
     * <p>
     * favorPathExtension表示支持后缀匹配，属性ignoreAcceptHeader默认为fasle，表示accept-header匹配，defaultContentType开启默认匹配。<br>
     * 根据以上条件进行一一匹配最终，得到相关并符合的策略初始化ContentNegotiationManager
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020年3月4日 下午3:46:33
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
    }

}
