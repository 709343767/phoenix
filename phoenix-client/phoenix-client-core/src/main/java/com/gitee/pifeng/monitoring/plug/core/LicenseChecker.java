package com.gitee.pifeng.monitoring.plug.core;

import cn.hutool.core.io.IoUtil;
import cn.hutool.crypto.GlobalBouncyCastleProvider;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import com.alibaba.fastjson.JSON;
import com.gitee.pifeng.monitoring.common.domain.License;
import com.gitee.pifeng.monitoring.common.exception.MonitoringUniversalException;
import com.gitee.pifeng.monitoring.common.util.DirUtils;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.Provider;
import java.security.Security;
import java.time.LocalDateTime;

/**
 * <p>
 * 许可证验证器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2024/5/3 17:30
 */
@Slf4j
public class LicenseChecker {

    /**
     * 解密公钥
     */
    private static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyFQ82udUbJIjENoqvyK9PMXr1JjdCypWL+9S7H7XhWlvjivyK6y4VrkZYj6UOe5uV6f3t4mYtg+1fEEJ4WuO5Y2FYgIL+ZIlLEHiz+gaAPmpoGoFR3dfQcLvaZ6Yv2K9Q9SlSi7cazMRTBB4eq6GgB6x6bfMvMML7TfvBe0qwPSeURfatZbPWYuDNArt3wfnk3yMiJxhZLRLnYOg/C3Qs6DC9jffKejIH95bxERbRoMvwjo6wEUZqOsVZHby6PYhE4uxNm3nMpSTX8j7c2CCIXEkQJoIYfI/XEb5H9Z1+8Qvk/tlEPrmgYvqJw7b9drmfkikIvNJ2P8k0SCaqFJqhQIDAQAB";

    /*
     * 参考资料：
     * https://blog.csdn.net/qq_36963762/article/details/122338213
     * https://blog.csdn.net/u012586326/article/details/143574259
     * https://www.cnblogs.com/hellxz/p/18530533
     * https://blog.csdn.net/weixin_40816738/article/details/108164270
     * https://github.com/chinabugotech/hutool/issues/2631
     */
    static {
        if (Security.getProvider("BC") == null) {
            // 注册 Bouncy Castle 提供程序
            Provider provider = GlobalBouncyCastleProvider.INSTANCE.getProvider();
            // Security.insertProviderAt(provider, 1);
            Security.addProvider(provider);
            // 强制关闭Bouncy Castle而使用JDK
            // GlobalBouncyCastleProvider.setUseBouncyCastle(false);
            log.info("注册加密提供程序：Bouncy Castle({})！", Security.getProvider("BC"));
        }
    }

    /**
     * <p>
     * 解析许可证文件
     * </p>
     *
     * @return {@link License}
     * @throws IOException IO异常
     * @author 皮锋
     * @custom.date 2024/5/3 18:39
     */
    public static License analysis() throws IOException {
        String pathname;
        try {
            // 获取Jar同级目录
            String jarDirectory = DirUtils.getJarDirectory();
            pathname = jarDirectory + File.separator + "license.txt";
        } catch (Exception e) {
            pathname = "license.txt";
        }
        // 读取许可证文件
        @Cleanup InputStream inputStream = Files.newInputStream(new File(pathname).toPath());
        String licenseTxt = IoUtil.read(inputStream, StandardCharsets.UTF_8);
        // 解密
        String decryptStr = SecureUtil.rsa(null, PUBLIC_KEY).decryptStr(licenseTxt, KeyType.PublicKey, StandardCharsets.UTF_8);
        // 转成许可证信息
        return JSON.parseObject(decryptStr, License.class);
    }

    /**
     * <p>
     * 验证许可证信息
     * </p>
     *
     * @return 验证许可证信息是否通过
     * @author 皮锋
     * @custom.date 2024/5/3 18:29
     */
    public static boolean verify() {
        try {
            // 解析许可证文件
            License license = analysis();
            // 截止时间
            LocalDateTime deadline = license.getDeadline();
            // 当前时间
            LocalDateTime date = LocalDateTime.now();
            if (date.isAfter(deadline)) {
                log.error("许可证文件已失效！");
                return false;
            }
            log.info("验证许可证信息成功！");
        } catch (Exception e) {
            log.error("无法解析许可证文件，可能是未找到许可证文件，或者许可证文件内容错误！错误详情：{}", e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * <p>
     * 生成许可证信息
     * </p>
     *
     * @param license {@link License}
     * @return 许可证信息 JSON 字符串
     * @author 皮锋
     * @custom.date 2024/5/6 10:52
     */
    public static String createLicense(License license) {
        if (license == null) {
            throw new MonitoringUniversalException("license 不能为空！");
        }
        return license.toJsonString();
    }

}
