package com.gitee.pifeng.monitoring.common.util.secure;

import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 测试DES加解密工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/8/12 17:07
 */
@Slf4j
public class DesEncryptUtilsTest extends TestCase {

    /**
     * <p>
     * 测试字符串DES加密
     * </p>
     *
     * @author 皮锋
     * @custom.date 2021/8/12 17:14
     */
    public void testEncrypt() {
        String encrypt = DesEncryptUtils.encrypt("测试DES加解密工具类");
        log.info("加密结果：" + encrypt);
        assertEquals(encrypt, "9H9PtukuYEeFIMdraLd+3qWX1DLwDwl7ga7sVr5l6DU=");
    }

    /**
     * <p>
     * 字符串DES解密
     * </p>
     *
     * @author 皮锋
     * @custom.date 2021/8/12 17:15
     */
    public void testDecrypt() {
        String decrypt = DesEncryptUtils.decrypt("9H9PtukuYEeFIMdraLd+3qWX1DLwDwl7ga7sVr5l6DU=");
        log.info("解密结果：" + decrypt);
        assertEquals(decrypt, "测试DES加解密工具类");
    }

}