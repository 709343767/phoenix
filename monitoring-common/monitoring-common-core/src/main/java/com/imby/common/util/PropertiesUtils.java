package com.imby.common.util;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * <p>
 * java读取.properties文件
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/2 17:59
 */
@Slf4j
public final class PropertiesUtils {

    /**
     * <p>
     * 屏蔽公有构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/3/2 18:01
     */
    private PropertiesUtils() {
    }

    /**
     * <p>
     * 把资源属性文件（properties）加载到缓存中
     * </p>
     *
     * @param filePath 文件路径
     * @return {@link Properties}
     * @throws IOException IO异常
     * @author 皮锋
     * @custom.date 2020年3月5日 上午11:41:13
     */
    public static Properties loadProperties(final String filePath) throws IOException {
        try {
            // 属性集合对象
            Properties properties = new Properties();
            // 获取路径并转换成流
            @Cleanup
            InputStream path = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
            assert path != null;
            @Cleanup
            InputStreamReader inputStreamReader = new InputStreamReader(path);
            @Cleanup
            BufferedReader bf = new BufferedReader(inputStreamReader);
            // 将属性文件流装载到Properties对象中
            properties.load(bf);
            return properties;
        } catch (IOException e) {
            log.error("读properties属性文件异常！", e);
            throw new IOException(e);
        }
    }

}