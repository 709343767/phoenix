package com.gitee.pifeng.monitoring.common.util.secure;

import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * <p>
 * 测试国密SM4加解密工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/8/14 12:33
 */
@Slf4j
public class Sm4EncryptUtilsTest extends TestCase {

    /**
     * <p>
     * 测试国密SM4加密
     * </p>
     *
     * @author 皮锋
     * @custom.date 2021/8/14 12:33
     */
    public void testEncrypt() {
        String encrypt = Sm4EncryptUtils.encrypt("测试AES加解密工具类", StandardCharsets.UTF_8);
        log.info("加密结果：{}", encrypt);
        assertEquals("WWv1OzuLn4Uep/oz9LFIrqMdXU45y+I7wrFuZW0nGuM=", encrypt);
    }

    /**
     * <p>
     * 测试国密SM4解密
     * </p>
     *
     * @author 皮锋
     * @custom.date 2021/8/14 12:33
     */
    public void testDecrypt() {
        String decrypt = Sm4EncryptUtils.decrypt("WWv1OzuLn4Uep/oz9LFIrqMdXU45y+I7wrFuZW0nGuM=", StandardCharsets.UTF_8);
        log.info("解密结果：{}", decrypt);
        assertEquals("测试AES加解密工具类", decrypt);
    }

}