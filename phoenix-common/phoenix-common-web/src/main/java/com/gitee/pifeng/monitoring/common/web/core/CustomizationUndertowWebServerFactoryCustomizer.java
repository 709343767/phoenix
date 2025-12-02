package com.gitee.pifeng.monitoring.common.web.core;

import io.undertow.UndertowOptions;
import io.undertow.server.DefaultByteBufferPool;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;

/**
 * <p>
 * 自定义 {@link WebServerFactoryCustomizer} 配置类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/25 10:19
 */
public class CustomizationUndertowWebServerFactoryCustomizer implements WebServerFactoryCustomizer<UndertowServletWebServerFactory> {

    /**
     * 全局唯一的 ByteBufferPool
     */
    private static final DefaultByteBufferPool BYTE_BUFFER_POOL = new DefaultByteBufferPool(false, 8192);

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
            // WebSocket 配置，去掉Undertow的“Buffer pool was not set on WebSocketDeploymentInfo, the default pool will be used”警告
            WebSocketDeploymentInfo webSocketDeploymentInfo = new WebSocketDeploymentInfo();
            webSocketDeploymentInfo.setBuffers(BYTE_BUFFER_POOL);
            deploymentInfo.addServletContextAttribute("io.undertow.websockets.jsr.WebSocketDeploymentInfo", webSocketDeploymentInfo);
        });

        // 三个超时配置
        factory.addBuilderCustomizers(builder -> {
            // 连接在 无任何读写活动 状态下最多保持 5 分钟，之后自动关闭
            builder.setSocketOption(UndertowOptions.IDLE_TIMEOUT, 300_000);
            // 10 秒内必须完成 HTTP 请求头解析
            builder.setSocketOption(UndertowOptions.REQUEST_PARSE_TIMEOUT, 10_000);
            // 连接建立后 10 秒内没发 HTTP 请求 就关闭（防慢连接攻击）
            builder.setSocketOption(UndertowOptions.NO_REQUEST_TIMEOUT, 10_000);
        });
    }

}
