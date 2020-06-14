package com.imby.server.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * <p>
 * WebSocket配置
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/6/14 20:05
 */
@Configuration
@Slf4j
public class WebSocketConfig {

    /**
     * <p>
     * 自动注册使用@ServerEndpoint注解声明的websocket endpoint
     * </p>
     *
     * @return {@link ServerEndpointExporter}
     * @author 皮锋
     * @custom.date 2020/6/14 20:07
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        ServerEndpointExporter serverEndpointExporter = new ServerEndpointExporter();
        log.info("WebSocket配置成功！");
        return serverEndpointExporter;
    }

}
