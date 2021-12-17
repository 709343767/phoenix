package com.gitee.pifeng.monitoring.common.util.secure;

import cn.hutool.crypto.SecureUtil;
import com.gitee.pifeng.monitoring.common.init.InitSecure;

import java.util.Base64;

/**
 * <p>
 * DES加解密工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/8/1613:06
 */
public class DesEncryptUtils extends InitSecure {

    /**
     * <p>
     * 字符串DES加密
     * </p>
     *
     * @param str 需要加密的字符串
     * @return 加密后的字符串
     * @author 皮锋
     * @custom.date 2021/8/14 9:01
     */
    public static String encrypt(String str) {
        byte[] key = Base64.getDecoder().decode(SECRET_KEY_DES);
        return SecureUtil.des(key).encryptBase64(str);
    }

    /**
     * <p>
     * 字符串DES解密
     * </p>
     *
     * @param str 需要解密的字符串
     * @return 解密后的字符串
     * @author 皮锋
     * @custom.date 2021/8/14 9:01
     */
    public static String decrypt(String str) {
        byte[] key = Base64.getDecoder().decode(SECRET_KEY_DES);
        return SecureUtil.des(key).decryptStr(str);
    }

}
