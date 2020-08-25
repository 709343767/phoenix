package com.imby.common.web.toolkit;

import io.undertow.server.DefaultByteBufferPool;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;

/**
 * <p>
 * 去掉Undertow的“Buffer pool was not set on WebSocketDeploymentInfo, the default pool will be used”警告。
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/25 10:19
 */
public class CustomizationBeanHandler implements WebServerFactoryCustomizer<UndertowServletWebServerFactory> {

    /**
     * <p>
     * 自定义指定的{@link WebServerFactory}。
     * </p>
     *
     * @param factory web服务器工厂
     * @author 皮锋
     * @custom.date 2020/8/25 10:21
     */
    @Override
    public void customize(UndertowServletWebServerFactory factory) {
        factory.addDeploymentInfoCustomizers(deploymentInfo -> {
            WebSocketDeploymentInfo webSocketDeploymentInfo = new WebSocketDeploymentInfo();
            webSocketDeploymentInfo.setBuffers(new DefaultByteBufferPool(false, 1024));
            deploymentInfo.addServletContextAttribute("io.undertow.websockets.jsr.WebSocketDeploymentInfo", webSocketDeploymentInfo);
        });
    }
}
