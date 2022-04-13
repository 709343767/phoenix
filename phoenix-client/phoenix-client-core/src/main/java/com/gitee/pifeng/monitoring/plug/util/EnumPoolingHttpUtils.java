package com.gitee.pifeng.monitoring.plug.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.util.CharsetUtil;
import com.gitee.pifeng.monitoring.plug.core.ConfigLoader;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
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
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultHttpResponseParserFactory;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * HTTP线程池工具类
 * </p>
 * 参考：https://blog.csdn.net/lovomap151/article/details/78879904
 *
 * @author 皮锋
 * @custom.date 2021/12/5 18:33
 * @since 1.0.0
 */
@Slf4j
public class EnumPoolingHttpUtils {

    /**
     * 构造方法私有化
     */
    private EnumPoolingHttpUtils() {
    }

    /**
     * 枚举类型是线程安全的，并且只会装载一次
     */
    private enum Singleton {
        /**
         * 实例
         */
        INSTANCE;

        private final EnumPoolingHttpUtils instance;

        Singleton() {
            instance = new EnumPoolingHttpUtils();
        }

        /**
         * <p>
         * 创建实例
         * </p>
         *
         * @return {@link EnumPoolingHttpUtils}
         * @author 皮锋
         * @custom.date 2020/8/22 9:11
         */
        private EnumPoolingHttpUtils getInstance() {
            return instance;
        }
    }

    /**
     * <p>
     * 创建实例
     * </p>
     *
     * @return {@link EnumPoolingHttpUtils}
     * @author 皮锋
     * @custom.date 2020/8/22 9:11
     */
    public static EnumPoolingHttpUtils getInstance() {
        return Singleton.INSTANCE.getInstance();
    }

    /**
     * HTTP客户端
     */
    private static CloseableHttpClient httpClient;

    static {
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
        int connectTimeout = ConfigLoader.MONITORING_PROPERTIES.getServerProperties().getConnectTimeout();
        int socketTimeout = ConfigLoader.MONITORING_PROPERTIES.getServerProperties().getSocketTimeout();
        int connectionRequestTimeout = ConfigLoader.MONITORING_PROPERTIES.getServerProperties().getConnectionRequestTimeout();
        //默认请求配置
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                // 设置连接超时时间，15s
                .setConnectTimeout(connectTimeout)
                // 设置等待数据超时时间，15s
                .setSocketTimeout(socketTimeout)
                // 设置从连接池获取连接的等待超时时间,15s
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .build();
        log.info("http connect timeout:{}ms", connectTimeout);
        log.info("http socket timeout:{}ms", socketTimeout);
        log.info("http connection request timeout:{}ms", connectionRequestTimeout);
        // 创建HttpClient
        httpClient = HttpClients.custom()
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
                    log.info("HTTP连接池关闭成功！");
                }
            } catch (IOException e) {
                log.error("HTTP连接池关闭时发生错误：", e);
            }
        }));
        log.info("HTTP连接池初始化成功！");
    }

    /**
     * <p>
     * 发送post请求
     * </p>
     *
     * @param url  请求URL
     * @param json JSON字符串格式的数据
     * @return 返回数据
     * @throws IOException IO异常
     * @author 皮锋
     * @custom.date 2020年3月5日 下午5:33:56
     */
    public String sendHttpPostByJson(String url, String json) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        if (null != json) {
            // 解决中文乱码问题
            StringEntity entity = new StringEntity(json, StandardCharsets.UTF_8);
            entity.setContentEncoding(CharsetUtil.UTF_8);
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
        }
        @Cleanup
        CloseableHttpResponse response = httpClient.execute(httpPost);
        // post请求返回结果
        String result = null;
        // 成功
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            // 读取服务器返回过来的json字符串数据
            result = EntityUtils.toString(response.getEntity(), CharsetUtil.UTF_8);
        }
        // 释放连接
        httpPost.releaseConnection();
        return result;
    }

    /**
     * <p>
     * 发送get请求
     * </p>
     *
     * @param url 请求URL
     * @return 返回Map，key解释：<br>
     * 1.statusCode：状态码；<br>
     * 2.avgTime：平均时间（毫秒）；<br>
     * 3.result：结果<br>
     * @author 皮锋
     * @custom.date 2022/4/13 12:38
     */
    public Map<String, Object> sendHttpGet(String url) {
        // http状态码
        int statusCode = 500;
        // get请求返回结果
        String result = null;
        // 计时器
        TimeInterval timer = DateUtil.timer();
        try {
            HttpGet httpget = new HttpGet(url);
            @Cleanup
            CloseableHttpResponse response = httpClient.execute(httpget);
            statusCode = response.getStatusLine().getStatusCode();
            // 成功
            if (statusCode == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity(), CharsetUtil.UTF_8);
            }
            // 释放连接
            httpget.releaseConnection();
        } catch (Exception e) {
            log.debug("发送get请求异常：{}", e.getMessage());
        }
        // 时间差（毫秒）
        long avgTime = timer.interval();
        // 返回结果
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("statusCode", statusCode);
        resultMap.put("avgTime", avgTime);
        resultMap.put("result", result);
        return resultMap;
    }

}
