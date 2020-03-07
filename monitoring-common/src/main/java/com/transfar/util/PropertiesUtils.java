package com.transfar.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.ResourceBundle;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

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
     * 屏蔽共有构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/3/2 18:01
     */
    private PropertiesUtils() {
    }

    /**
     * <p>
     * 读取资源属性文件（properties），无缓存方式
     * </p>
     *
     * @param filePath 文件路径
     * @param param    属性参数
     * @return 属性值
     * @throws IOException IO异常
     * @author 皮锋
     * @custom.date 2020/3/2 18:07
     */
    public static String readPropertyNoCache(final String filePath, final String param) throws Exception {
        try {
            String url = PropertiesUtils.class.getResource("/").getPath() + filePath;
            Properties prop = new Properties();
            @Cleanup
            FileInputStream fileInputStream = new FileInputStream(url);
            @Cleanup
            InputStream in = new BufferedInputStream(fileInputStream);
            // 将属性文件流装载到Properties对象中
            // prop.load(in);
            @Cleanup
            InputStreamReader inputStreamReader = new InputStreamReader(in, StandardCharsets.UTF_8);
            prop.load(inputStreamReader);
            return prop.getProperty(param);
        } catch (IOException e) {
            log.error("读properties属性文件异常！", e);
            throw new IOException(e);
        }
    }

    /**
     * <p>
     * 读取资源属性文件（properties）
     * </p>
     *
     * @param filePath 文件路径
     * @param param    属性参数
     * @return 属性值
     * @throws IOException IO异常
     * @author 皮锋
     * @custom.date 2020/3/2 18:07
     */
    public static String readProperty(final String filePath, final String param) throws IOException {
        // 属性集合对象
        Properties properties = new Properties();
        // 获取路径并转换成流
        try {
            @Cleanup
            InputStream path = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
            assert path != null;
            @Cleanup
            InputStreamReader inputStreamReader = new InputStreamReader(path);
            @Cleanup
            BufferedReader bf = new BufferedReader(inputStreamReader);
            // 将属性文件流装载到Properties对象中
            properties.load(bf);
            return properties.getProperty(param);
        } catch (IOException e) {
            log.error("读properties属性文件异常！", e);
            throw new IOException(e);
        }
    }

    /**
     * <p>
     * 把资源属性文件（properties）加载到缓存中
     * </p>
     *
     * @param filePath 文件路径
     * @return Properties
     * @throws IOException IO异常
     * @author 皮锋
     * @custom.date 2020年3月5日 上午11:41:13
     */
    public static Properties loadProperties(final String filePath) throws IOException {
        // 属性集合对象
        Properties properties = new Properties();
        // 获取路径并转换成流
        try {
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

    /**
     * <p>
     * 读取资源属性文件（properties）
     * </p>
     *
     * @param filePath 文件路径
     * @param param    属性参数
     * @return 属性值
     * @author 皮锋
     * @custom.date 2020/3/2 18:07
     */
    public static String getProperty(final String filePath, final String param) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(filePath);
        return resourceBundle.getString(param);
    }

    // public static void main(String[] args) {
    // 1.读取资源属性文件（properties），然后根据.properties文件的名称信息（本地化信息）
    // String result1 = getProperty("conf/db", "jdbc_url");
    // System.out.println(result1);
    // 2.使用Properties类读Properties属性文件，用IO流的方式
    // String result2 = readProperty("conf/db.properties", "jdbc_url");
    // System.out.println(result2);

    // 写Properties文件
    // writeProperties("conf/db.properties", "jdbc_url",
    // "jdbc:mysql://localhost:3306/test");

    // 3.使用无缓存的方式读取Properties属性文件
    // String result3 = readPropertyNoCache("conf/db.properties", "jdbc_url");
    // System.out.println(result3);
    // }
}