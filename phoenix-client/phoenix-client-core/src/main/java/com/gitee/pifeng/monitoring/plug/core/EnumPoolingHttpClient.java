package com.gitee.pifeng.monitoring.plug.core;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.util.CharsetUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gitee.pifeng.monitoring.common.constant.HttpMediaTypeConstants;
import com.gitee.pifeng.monitoring.common.util.ArrayUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
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
import org.apache.http.conn.ssl.NoopHostnameVerifier;
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
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * HTTP线程池客户端
 * </p>
 * 参考：<a href="https://blog.csdn.net/lovomap151/article/details/78879904">HttpClient连接池的简单实现</a>
 *
 * @author 皮锋
 * @custom.date 2021/12/5 18:33
 * @since 1.0.0
 */
@Slf4j
public class EnumPoolingHttpClient {

    /**
     * 构造方法私有化
     */
    private EnumPoolingHttpClient() {
    }

    /**
     * 枚举类型是线程安全的，并且只会装载一次
     */
    private enum Singleton {
        /**
         * 实例
         */
        INSTANCE;

        private final EnumPoolingHttpClient instance;

        Singleton() {
            instance = new EnumPoolingHttpClient();
        }

        /**
         * <p>
         * 创建实例
         * </p>
         *
         * @return {@link EnumPoolingHttpClient}
         * @author 皮锋
         * @custom.date 2020/8/22 9:11
         */
        private EnumPoolingHttpClient getInstance() {
            return instance;
        }
    }

    /**
     * <p>
     * 创建实例
     * </p>
     *
     * @return {@link EnumPoolingHttpClient}
     * @author 皮锋
     * @custom.date 2020/8/22 9:11
     */
    public static EnumPoolingHttpClient getInstance() {
        return Singleton.INSTANCE.getInstance();
    }

    /**
     * HTTP客户端
     */
    private static final CloseableHttpClient HTTP_CLIENT;

    static {
        SSLConnectionSocketFactory sslConnectionSocketFactory;
        try {
            // 创建并配置 SSLContext
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, (chain, authType) -> true).build();
            String[] designativeTlsVersions = new String[]{"TLSv1", "TLSv1.1", "TLSv1.2", "TLSv1.3"};
            String[] supportedProtocols = sslContext.getSupportedSSLParameters().getProtocols();
            String[] allTlsVersions = ArrayUtils.mergeAndDeduplicateStrings(designativeTlsVersions, supportedProtocols);
            sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, allTlsVersions, null, NoopHostnameVerifier.INSTANCE);
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            // 拿到默认的SSLConnectionSocketFactory
            sslConnectionSocketFactory = SSLConnectionSocketFactory.getSystemSocketFactory();
        }
        // 注册访问协议相关的Socket工厂
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                // 配置同时支持 HTTP 和 HTPPS
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                // 参考：https://blog.csdn.net/u011537073/article/details/81216229
                .register("https", sslConnectionSocketFactory)
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
        int connectTimeout = ConfigLoader.getMonitoringProperties().getComm().getHttp().getConnectTimeout();
        int socketTimeout = ConfigLoader.getMonitoringProperties().getComm().getHttp().getSocketTimeout();
        int connectionRequestTimeout = ConfigLoader.getMonitoringProperties().getComm().getHttp().getConnectionRequestTimeout();
        //默认请求配置
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                // 设置连接超时时间，15s
                .setConnectTimeout(connectTimeout)
                // 设置等待数据超时时间，15s
                .setSocketTimeout(socketTimeout)
                // 设置从连接池获取连接的等待超时时间,15s
                .setConnectionRequestTimeout(connectionRequestTimeout)
                // 禁用自动重定向
                .setRedirectsEnabled(false)
                .build();
        log.info("http connect timeout:{}ms", connectTimeout);
        log.info("http socket timeout:{}ms", socketTimeout);
        log.info("http connection request timeout:{}ms", connectionRequestTimeout);
        // 创建HttpClient
        HTTP_CLIENT = HttpClients.custom()
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
        log.info("客户端HTTP连接池初始化成功！");
    }

    /**
     * <p>
     * 关闭HTTP连接池释放掉连接(跟数据库连接池类似)
     * </p>
     *
     * @author 皮锋
     * @custom.date 2023/5/31 15:06
     */
    public void close() {
        try {
            if (HTTP_CLIENT != null) {
                HTTP_CLIENT.close();
                log.info("客户端HTTP连接池关闭成功！");
            }
        } catch (IOException e) {
            log.error("客户端HTTP连接池关闭时发生错误：", e);
        }
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
        // 创建httpPost请求对象
        HttpPost httpPost = new HttpPost(url);
        try {
            if (StringUtils.isNotBlank(json)) {
                // 解决中文乱码问题
                StringEntity entity = new StringEntity(json, StandardCharsets.UTF_8);
                entity.setContentEncoding(CharsetUtil.UTF_8);
                entity.setContentType(HttpMediaTypeConstants.APPLICATION_JSON);
                httpPost.setEntity(entity);
            }
            @Cleanup
            CloseableHttpResponse response = HTTP_CLIENT.execute(httpPost);
            // post请求返回结果
            String result = null;
            // 成功
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // 读取服务器返回过来的json字符串数据
                result = EntityUtils.toString(response.getEntity(), CharsetUtil.UTF_8);
            }
            return result;
        } finally {
            // 释放连接
            httpPost.releaseConnection();
        }
    }

    /**
     * <p>
     * 发送post请求
     * </p>
     *
     * @param url             请求URL
     * @param contentType     媒体类型
     * @param headerParameter 请求头参数
     * @param bodyParameter   请求体参数
     * @return 返回Map，key解释：<br>
     * 1.statusCode：状态码；<br>
     * 2.avgTime：平均时间（毫秒）；<br>
     * 3.excMessage：异常信息；<br>
     * 4.result：结果<br>
     * @author 皮锋
     * @custom.date 2022/4/15 13:03
     */
    public Map<String, Object> sendHttpPost(String url, String contentType, String headerParameter, String bodyParameter) {
        // http状态码
        int statusCode = 500;
        // 异常信息
        String excMessage = null;
        // get请求返回结果
        String result = null;
        // 创建httpPost请求对象
        HttpPost httpPost = new HttpPost(url);
        // 计时器
        TimeInterval timer = DateUtil.timer();
        try {
            // 设置请求头
            httpPost.setHeader("Content-Type", contentType);
            if (StringUtils.isNotBlank(headerParameter)) {
                List<JSONObject> tempHeaderParameters = JSONArray.parseArray(headerParameter, JSONObject.class);
                if (CollectionUtils.isNotEmpty(tempHeaderParameters)) {
                    for (JSONObject param : tempHeaderParameters) {
                        httpPost.setHeader(param.getString("key"), param.getString("value"));
                    }
                }
            }
            // 设置请求体
            if (StringUtils.equalsIgnoreCase(HttpMediaTypeConstants.APPLICATION_FORM_URLENCODED, contentType)) {
                // 创建表单参数列表
                List<NameValuePair> params = Lists.newArrayList();
                if (StringUtils.isNotBlank(bodyParameter)) {
                    List<JSONObject> tempBodyParameters = JSONArray.parseArray(bodyParameter, JSONObject.class);
                    if (CollectionUtils.isNotEmpty(tempBodyParameters)) {
                        for (JSONObject param : tempBodyParameters) {
                            params.add(new BasicNameValuePair(param.getString("key"), param.getString("value")));
                        }
                    }
                }
                // 设置表单参数
                httpPost.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));
            } else if (StringUtils.equalsIgnoreCase(HttpMediaTypeConstants.APPLICATION_JSON, contentType)) {
                if (StringUtils.isNotBlank(bodyParameter)) {
                    // 解决中文乱码问题
                    StringEntity entity = new StringEntity(bodyParameter, StandardCharsets.UTF_8);
                    entity.setContentEncoding(CharsetUtil.UTF_8);
                    entity.setContentType(contentType);
                    httpPost.setEntity(entity);
                }
            }
            @Cleanup
            CloseableHttpResponse response = HTTP_CLIENT.execute(httpPost);
            statusCode = response.getStatusLine().getStatusCode();
            // 成功
            if (statusCode == HttpStatus.SC_OK) {
                // 读取服务器返回过来的json字符串数据
                result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug("发送post请求异常：{}", e.getMessage());
            }
            excMessage = e.getMessage();
        } finally {
            // 释放连接
            httpPost.releaseConnection();
        }
        // 时间差（毫秒）
        long avgTime = timer.interval();
        // 返回结果
        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("statusCode", statusCode);
        resultMap.put("avgTime", avgTime);
        resultMap.put("excMessage", excMessage);
        resultMap.put("result", result);
        if (log.isDebugEnabled()) {
            log.debug("发送post（{}）请求结果：{}", url, JSON.toJSONString(resultMap));
        }
        return resultMap;
    }

    /**
     * <p>
     * 发送get请求
     * </p>
     *
     * @param url             请求URL
     * @param headerParameter 请求头参数
     * @return 返回Map，key解释：<br>
     * 1.statusCode：状态码；<br>
     * 2.avgTime：平均时间（毫秒）；<br>
     * 3.excMessage：异常信息；<br>
     * 4.result：结果<br>
     * @author 皮锋
     * @custom.date 2022/4/13 12:38
     */
    public Map<String, Object> sendHttpGet(String url, String headerParameter) {
        // http状态码
        int statusCode = 500;
        // 异常信息
        String excMessage = null;
        // get请求返回结果
        String result = null;
        // 创建httpGet请求对象
        HttpGet httpget = new HttpGet(url);
        // 计时器
        TimeInterval timer = DateUtil.timer();
        try {
            // 设置请求头
            if (StringUtils.isNotBlank(headerParameter)) {
                List<JSONObject> tempHeaderParameters = JSONArray.parseArray(headerParameter, JSONObject.class);
                if (CollectionUtils.isNotEmpty(tempHeaderParameters)) {
                    for (JSONObject param : tempHeaderParameters) {
                        httpget.setHeader(param.getString("key"), param.getString("value"));
                    }
                }
            }
            @Cleanup
            CloseableHttpResponse response = HTTP_CLIENT.execute(httpget);
            statusCode = response.getStatusLine().getStatusCode();
            // 成功
            if (statusCode == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity(), CharsetUtil.UTF_8);
            }
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug("发送get请求异常：{}", e.getMessage());
            }
            excMessage = e.getMessage();
        } finally {
            // 释放连接
            httpget.releaseConnection();
        }
        // 时间差（毫秒）
        long avgTime = timer.interval();
        // 返回结果
        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("statusCode", statusCode);
        resultMap.put("avgTime", avgTime);
        resultMap.put("excMessage", excMessage);
        resultMap.put("result", result);
        if (log.isDebugEnabled()) {
            log.debug("发送get（{}）请求结果：{}", url, JSON.toJSONString(resultMap));
        }
        return resultMap;
    }

}
