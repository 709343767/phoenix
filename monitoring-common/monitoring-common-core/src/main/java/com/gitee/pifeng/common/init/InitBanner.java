package com.gitee.pifeng.common.init;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 * 初始化项目banner
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/16 13:34
 */
@Slf4j
public class InitBanner {

    static {
        InitBanner.printBanner();
    }

    /**
     * <p>
     * 私有化构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/10/27 13:26
     */
    private InitBanner() {
    }

    /**
     * <p>
     * 加载“banner.txt”文件
     * </p>
     *
     * @return “banner.txt”文件字符串
     * @throws IOException IO异常
     * @author 皮锋
     * @custom.date 2020/9/16 12:43
     */
    private static String loadBanner() throws IOException {
        @Cleanup
        InputStream path = Thread.currentThread().getContextClassLoader().getResourceAsStream("banner-monitoring.txt");
        assert path != null;
        return IOUtils.toString(path, StandardCharsets.UTF_8);
    }

    /**
     * <p>
     * 打印banner
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/9/16 12:46
     */
    public static void printBanner() {
        try {
            // 加载
            String banner = loadBanner();
            // 打印banner就要用syso，不要用logger
            System.out.println(banner);
        } catch (IOException e) {
            log.error("控制台打印banner异常！", e);
        }
    }

    /**
     * <p>
     * 打印banner成功
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/10/26 21:55
     */
    public static void declare() {
        log.trace("打印banner成功！");
    }

}
