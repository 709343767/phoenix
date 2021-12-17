package com.gitee.pifeng.monitoring.server.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.impl.io.DefaultHttpResponseParserFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

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
     * <p>
     * 构造RestTemplate实例，把RestTemplate实例作为一个JavaBean交给Spring管理。
     * </p>
     *
     * @return {@link RestTemplate}
     * @author 皮锋
     * @custom.date 2020年3月4日 下午3:46:33
     */
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(this.httpRequestFactory());
        log.info("RestTemplate配置成功！");
        return restTemplate;
    }

    /**
     * <p>
     * 构造ClientHttpRequestFactory实例
     * </p>
     *
     * @return {@link ClientHttpRequestFactory}
     * @author 皮锋
     * @custom.date 2021/12/6 16:41
     */
    @Bean
    public ClientHttpRequestFactory httpRequestFactory() {
        return new HttpComponentsClientHttpRequestFactory(this.httpClient());
    }

    /**
     * <p>
     * 构造CloseableHttpClient实例
     * </p>
     *
     * @return {@link CloseableHttpClient}
     * @author 皮锋
     * @custom.date 2021/12/6 16:43
     */
    @Bean
    public CloseableHttpClient httpClient() {
        // 注册访问协议相关的Socket工厂
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                // 配置同时支持 HTTP 和 HTPPS
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", SSLConnectionSocketFactory.getSystemSocketFactory())
                .build();
        // HttpConnection工厂：配置写请求/解析响应处理器
        HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connectionFactory
                = new ManagedHttpClientConnectionFactory(DefaultHttpRequestWriterFactory.INSTANCE, DefaultHttpResponseParserFactory.INSTANCE);
        // DNS解析器
        DnsResolver dnsResolver = SystemDefaultDnsResolver.INSTANCE;
        // 创建池化连接管理器
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager(socketFactoryRegistry, connectionFactory, dnsResolver);
        // 默认Socket配置
        SocketConfig defaultSocketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
        manager.setDefaultSocketConfig(defaultSocketConfig);
        // 设置整个连接池的最大连接数
        manager.setMaxTotal(300);
        // 每个路由的默认最大连接，每个路由实际最大连接数由DefaultMaxPerRoute控制，而MaxTotal是整个池子的最大数
        // 设置过小无法支持大并发(ConnectionPoolTimeoutException) Timeout waiting for connection from pool
        // 每个路由的最大连接数
        manager.setDefaultMaxPerRoute(200);
        // 在从连接池获取连接时，连接不活跃多长时间后需要进行一次验证，默认为2s
        manager.setValidateAfterInactivity(30 * 1000);
        //默认请求配置
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                // 设置连接超时时间，15s
                .setConnectTimeout(15000)
                // 设置等待数据超时时间，15s
                .setSocketTimeout(15000)
                // 设置从连接池获取连接的等待超时时间,15s
                .setConnectionRequestTimeout(15000)
                .build();
        // 创建HttpClient
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(manager)
                // 连接池不是共享模式
                .setConnectionManagerShared(false)
                // 定期回收空闲连接
                .evictIdleConnections(60, TimeUnit.SECONDS)
                // 定期回收过期连接
                .evictExpiredConnections()
                // 连接存活时间，如果不设置，则根据长连接信息决定
                .setConnectionTimeToLive(60, TimeUnit.SECONDS)
                // 设置默认请求配置
                .setDefaultRequestConfig(defaultRequestConfig)
                // 连接重用策略，即是否能keepAlive
                .setConnectionReuseStrategy(DefaultConnectionReuseStrategy.INSTANCE)
                // 长连接配置，即获取长连接生产多长时间
                .setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE)
                // 设置重试次数，默认是3次
                .setRetryHandler(new DefaultHttpRequestRetryHandler(3, true))
                .build();
        // JVM 停止或重启时，关闭连接池释放掉连接(跟数据库连接池类似)
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (httpClient != null) {
                    httpClient.close();
                    log.info("HTTP连接关闭成功！");
                }
            } catch (IOException e) {
                log.error("HTTP连接关闭时发生错误：", e);
            }
        }));
        log.info("HTTP连接池初始化成功！");
        return httpClient;
    }

}
