package com.gitee.pifeng.monitoring.common.util;

import com.gitee.pifeng.monitoring.common.exception.MonitoringUniversalException;
import com.gitee.pifeng.monitoring.common.util.server.OsUtils;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * <p>
 * 文件夹工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2024/10/23 15:33
 */
public class DirUtils {

    /**
     * <p>
     * 私有化构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2024/10/23 16:02
     */
    private DirUtils() {
    }

    /**
     * <p>
     * 获取Jar同级目录
     * </p>
     *
     * @return Jar同级目录
     * @throws MonitoringUniversalException 监控平台通用异常
     * @author 皮锋
     * @custom.date 2024/10/23 15:40
     */
    public static String getJarDirectory() {
        // 获取当前类所在JAR文件的URL
        URL resourceUrl = getClassUrlInJar(DirUtils.class);
        try {
            // 提取JAR文件的路径
            String jarFilePath = resourceUrl.getPath().substring(5, resourceUrl.getPath().indexOf('!'));
            if (OsUtils.isWindowsOs()) {
                // 移除前导斜杠
                if (jarFilePath.startsWith("/")) {
                    jarFilePath = jarFilePath.substring(1);
                }
            }
            // 使用Paths.get处理路径
            Path jarPath = Paths.get(jarFilePath);
            File jarFile = jarPath.toFile();
            // 获取JAR文件所在的目录
            File jarDirectory = jarFile.getParentFile();
            String absolutePath = jarDirectory.getAbsolutePath();
            // 解码路径
            return URLDecoder.decode(absolutePath, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            throw new MonitoringUniversalException("获取Jar同级目录失败：" + e.getMessage(), e);
        }
    }

    /**
     * <p>
     * 获取给定类所在JAR文件的URL
     * </p>
     *
     * @param clazz {@link Class}
     * @return 给定类所在JAR文件的URL
     * @throws MonitoringUniversalException 监控平台通用异常
     * @author 皮锋
     * @custom.date 2024/10/23 15:40
     */
    public static URL getClassUrlInJar(Class<?> clazz) {
        // 获取当前类的类加载器
        ClassLoader classLoader = clazz.getClassLoader();
        // 获取当前类的资源URL
        URL resourceUrl = classLoader.getResource(clazz.getName().replace('.', '/') + ".class");
        if (resourceUrl == null) {
            throw new MonitoringUniversalException("无法找到资源URL！");
        }
        // 检查资源URL是否为JAR文件
        if (!resourceUrl.toString().startsWith("jar:file:")) {
            throw new MonitoringUniversalException("资源URL不是JAR文件：" + resourceUrl);
        }
        return resourceUrl;
    }

}
