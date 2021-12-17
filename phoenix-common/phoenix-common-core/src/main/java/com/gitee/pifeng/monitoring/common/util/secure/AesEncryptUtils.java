package com.gitee.pifeng.monitoring.common.util.secure;

import cn.hutool.crypto.SecureUtil;
import com.gitee.pifeng.monitoring.common.init.InitSecure;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * <p>
 * AES加解密工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/8/13 15:57
 */
public class AesEncryptUtils extends InitSecure {

    /**
     * <p>
     * 字符串AES加密
     * </p>
     *
     * @param str 需要加密的字符串
     * @return 加密后的字符串
     * @author 皮锋
     * @custom.date 2021/8/13 16:19
     */
    public static String encrypt(String str) {
        byte[] key = Base64.getDecoder().decode(SECRET_KEY_AES);
        return SecureUtil.aes(key).encryptBase64(str, StandardCharsets.UTF_8);
    }

    /**
     * <p>
     * 字符串AES解密
     * </p>
     *
     * @param str 需要解密的字符串
     * @return 解密后的字符串
     * @author 皮锋
     * @custom.date 2021/8/13 16:19
     */
    public static String decrypt(String str) {
        byte[] key = Base64.getDecoder().decode(SECRET_KEY_AES);
        return SecureUtil.aes(key).decryptStr(str, StandardCharsets.UTF_8);
    }

}
