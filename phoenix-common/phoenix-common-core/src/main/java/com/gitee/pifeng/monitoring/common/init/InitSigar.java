package com.gitee.pifeng.monitoring.common.init;

import cn.hutool.core.io.FileUtil;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarLoader;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * <p>
 * 初始化Sigar
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/13 11:21
 */
@Slf4j
public class InitSigar {

    /**
     * 初始化Sigar，并创建Sigar对象
     */
    public static final Sigar SIGAR = initSigar();

    /**
     * <p>
     * 初始化Sigar
     * </p>
     *
     * @return Sigar对象
     * @author 皮锋
     * @custom.date 2020年3月3日 下午12:56:14
     */
    @SneakyThrows
    public static Sigar initSigar() {
        try {
            SigarLoader loader = new SigarLoader(Sigar.class);
            String lib = loader.getLibraryName();
            log.info("初始化Sigar库文件：{}", lib);
            // 系统属性
            Properties props = System.getProperties();
            // 当前文件夹路径
            String currentDir = props.getProperty("user.dir");
            File tempDir = new File(currentDir + File.separator + "liblog4phoenix" + File.separator + "sigar");
            // 判断文件夹是否存在
            if (!tempDir.exists()) {
                boolean isMkdirs = tempDir.mkdirs();
                log.info("创建Sigar库文件夹：{}", isMkdirs);
            }
            File file = new File(tempDir, lib);
            // 判断文件是否存在
            if (!file.exists()) {
                @Cleanup
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("sigar/" + lib);
                @Cleanup
                FileOutputStream fileOutputStream = new FileOutputStream(file, false);
                @Cleanup
                BufferedOutputStream os = new BufferedOutputStream(fileOutputStream);
                int lentgh;
                assert is != null;
                while ((lentgh = is.read()) != -1) {
                    os.write(lentgh);
                }
            }
            // 文件为空
            if (FileUtil.isEmpty(file)) {
                throw new SigarException("Sigar库文件大小为零！");
            }
            // 系统属性路径
            String propertyPath = "org.hyperic.sigar.path";
            // 系统属性
            String property = System.getProperty(propertyPath);
            if (StringUtils.isBlank(property)) {
                // 设置系统属性
                System.setProperty(propertyPath, tempDir.getCanonicalPath());
            }
            log.info("Sigar库文件路径：{}", System.getProperty(propertyPath));
            // 返回Sigar对象
            return new Sigar();
        } catch (Exception e) {
            String msg = "初始化Sigar异常！";
            log.error(msg, e);
            throw new Exception(msg);
        }
    }
}
