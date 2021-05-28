package com.gitee.pifeng.monitoring.plug.util;

import lombok.Cleanup;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;

/**
 * <p>
 * 枚举的方式实现Http单例工具类
 * </p>
 * 推荐使用：<br>
 * 1.线程安全；<br>
 * 2.防止反射攻击；<br>
 * 3.防止反序列化攻击。<br>
 *
 * @author 皮锋
 * @custom.date 2020/8/22 9:04
 * @since v0.0.2
 */
public class EnumHttpUtils {

    /**
     * 功能：获取和配置一些外部的网络环境<br>
     * 其使用方法是：<br>
     * 1.先用RequestConfig类的静态方法custom()获取equestConfig.Builder“配置器”，<br>
     * 2.然后再用其下各种方法配置网络环境；或者已经有配置好的RequestConfig对象（非RequestConfig.Builder）而将此对象拷贝（RequestConfig类的copy()方法）过来返回“配置器“而重新进行其它的网络环境的配置。<br>
     * 3.最终，再调用配置器的buillder()方法返回RequestConfig对象。<br>
     */
    private final RequestConfig defaultRequestConfig = RequestConfig.custom()
            .setCookieSpec(CookieSpecs.STANDARD)
            .setExpectContinueEnabled(true)
            .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
            .setProxyPreferredAuthSchemes(Collections.singletonList(AuthSchemes.BASIC)).build();

    /**
     * 请求配置
     */
    private final RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig).setSocketTimeout(15000)
            .setConnectTimeout(15000).setConnectionRequestTimeout(15000)
            // .setProxy(new HttpHost("myotherproxy", 8080))
            .build();

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
    private EnumHttpUtils() {
    }

    /**
     * 枚举类型是线程安全的，并且只会装载一次
     */
    private enum Singleton {
        /**
         * 实例
         */
        INSTANCE;

        private final EnumHttpUtils instance;

        Singleton() {
            instance = new EnumHttpUtils();
        }

        /**
         * <p>
         * 创建实例
         * </p>
         *
         * @return {@link EnumHttpUtils}
         * @author 皮锋
         * @custom.date 2020/8/22 9:11
         */
        private EnumHttpUtils getInstance() {
            return instance;
        }
    }

    /**
     * <p>
     * 创建实例
     * </p>
     *
     * @return {@link EnumHttpUtils}
     * @author 皮锋
     * @custom.date 2020/8/22 9:11
     */
    public static EnumHttpUtils getInstance() {
        return Singleton.INSTANCE.getInstance();
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
        httpPost.setConfig(requestConfig);
        if (null != json) {
            // 解决中文乱码问题
            StringEntity entity = new StringEntity(json, StandardCharsets.UTF_8);
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
        // 释放连接
        httpPost.releaseConnection();
        return result;
    }

}
