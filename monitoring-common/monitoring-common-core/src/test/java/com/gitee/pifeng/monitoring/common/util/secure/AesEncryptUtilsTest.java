package com.gitee.pifeng.monitoring.common.util.secure;

import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;

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
        String encrypt = AesEncryptUtils.encrypt("测试AES加解密工具类");
        log.info("加密结果：" + encrypt);
        assertEquals(encrypt, "IfXkyDHOB9FGhgj9OLizOVBRwO9D7lir86BtkUNaxr8=");
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
        String decrypt = AesEncryptUtils.decrypt("IfXkyDHOB9FGhgj9OLizOVBRwO9D7lir86BtkUNaxr8=");
        log.info("解密结果：" + decrypt);
        assertEquals(decrypt, "测试AES加解密工具类");
    }

}