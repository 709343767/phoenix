package com.gitee.pifeng.monitoring.common.util.secure;

import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * <p>
 * 测试AES加解密工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/8/13 18:15
 */
@Slf4j
public class AesEncryptUtilsTest extends TestCase {

    /**
     * <p>
     * 测试AES加密
     * </p>
     *
     * @author 皮锋
     * @custom.date 2021/8/13 18:12
     */
    public void testEncrypt() {
        String encrypt = AesEncryptUtils.encrypt("测试AES加解密工具类", StandardCharsets.UTF_8);
        log.info("加密结果：" + encrypt);
        assertEquals(encrypt, "9FUrFVEsBR3MjwVFv2gydNAKlnXM2fkBdkn1FhMHFro=");
    }

    /**
     * <p>
     * 测试AES解密
     * </p>
     *
     * @author 皮锋
     * @custom.date 2021/8/13 18:13
     */
    public void testDecrypt() {
        String decrypt = AesEncryptUtils.decrypt("9FUrFVEsBR3MjwVFv2gydNAKlnXM2fkBdkn1FhMHFro=", StandardCharsets.UTF_8);
        log.info("解密结果：" + decrypt);
        assertEquals(decrypt, "测试AES加解密工具类");
    }

}