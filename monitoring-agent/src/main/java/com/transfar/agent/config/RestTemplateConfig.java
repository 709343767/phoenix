package com.transfar.agent.config;

import com.transfar.common.property.MonitoringProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

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
public class RestTemplateConfig {

    /**
     * 监控属性
     */
    @Autowired
    private MonitoringProperties monitoringProperties;

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
        String rootUri = this.monitoringProperties.getServerProperties().getUrl();
        RestTemplate restTemplate = new RestTemplateBuilder()//
                // .basicAuthorization("username", "password")
                // 15秒
                .setConnectTimeout(Duration.ofSeconds(15))
                // 15秒
                .setReadTimeout(Duration.ofSeconds(15))//
                .rootUri(rootUri) //
                .build();
        log.info("RestTemplate配置成功！");
        return restTemplate;
    }

}
