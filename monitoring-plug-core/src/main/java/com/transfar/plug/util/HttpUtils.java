package com.transfar.plug.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.common.base.Charsets;

import lombok.Cleanup;

// HttpClient是Apache Jakarta Common下的子项目，
// 用来提供高效的、最新的、功能丰富的支持HTTP协议的客户端编程工具包，并且它支持HTTP协议最新的版本和建议。
// HttpClient已经应用在很多的项目中，比如Apache Jakarta上很著名的另外两个开源项目
// Cactus和HTMLUnit都使用了HttpClient。

/**
 * <p>
 * Http工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月5日 下午5:29:46
 */
public class HttpUtils {

    /**
     * 功能：获取和配置一些外部的网络环境<br>
     * 其使用方法是：1.先用RequestConfig类的静态方法custom()获取equestConfig.Builder“配置器”，<br>
     * 2.然后再用其下各种方法配置网络环境；或者已经有配置好的RequestConfig对象（非RequestConfig.Builder）而将此对象拷贝（RequestConfig类的copy()方法）过来返回“配置器“而重新进行其它的网络环境的配置。<br>
     * 3.最终，再调用配置器的buillder()方法返回RequestConfig对象。<br>
     */
    @SuppressWarnings("deprecation")
    private RequestConfig defaultRequestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.BEST_MATCH)
            .setExpectContinueEnabled(true).setStaleConnectionCheckEnabled(true)
            .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
            .setProxyPreferredAuthSchemes(Collections.singletonList(AuthSchemes.BASIC)).build();

    /**
     * 请求配置
     */
    private RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig).setSocketTimeout(15000)
            .setConnectTimeout(15000).setConnectionRequestTimeout(15000)
            // .setProxy(new HttpHost("myotherproxy", 8080))
            .build();

    /**
     * 本地线程
     */
    private static ThreadLocal<HttpUtils> threadLocal = new ThreadLocal<>();

    /**
     * 编码方式
     */
    private static final String CHARSET = "UTF-8";

    /**
     * 文本类型
     */
    private static final String CONTENT_TYPE = "application/json";

    /**
     * 构造方法私有化
     */
    private HttpUtils() {
    }

    /**
     * <p>
     * 单例模式创建实例(线程安全的)
     * </p>
     *
     * @return {@link HttpUtils}
     * @author 皮锋
     * @custom.date 2020年3月5日 下午5:33:35
     */
    public static HttpUtils getInstance() {
        // 获取ThreadLocal中当前线程共享变量的值
        HttpUtils instance = threadLocal.get();
        if (instance == null) {
            instance = new HttpUtils();
            // 设置ThreadLocal中当前线程共享变量的值
            threadLocal.set(instance);
        }
        return instance;
    }

    /**
     * <p>
     * 发送post请求
     * </p>
     *
     * @param url  请求URL
     * @param json JSON字符串格式的数据
     * @return 返回数据
     * @throws IOException             IO异常
     * @throws ClientProtocolException 客户端协议异常
     * @author 皮锋
     * @custom.date 2020年3月5日 下午5:33:56
     */
    public String sendHttpPostByJSON(String url, String json) throws ClientProtocolException, IOException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        if (null != json) {
            // 解决中文乱码问题
            StringEntity entity = new StringEntity(json, Charsets.UTF_8);
            entity.setContentEncoding(CHARSET);
            entity.setContentType(CONTENT_TYPE);
            httpPost.setEntity(entity);
        }
        @Cleanup
        CloseableHttpClient httpClient = HttpClients.createDefault();
        @Cleanup
        CloseableHttpResponse response = httpClient.execute(httpPost);
        // post请求返回结果
        String result = null;
        // 成功
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            // 读取服务器返回过来的json字符串数据
            result = EntityUtils.toString(response.getEntity(), CHARSET);
        }
        return result;
    }

}
