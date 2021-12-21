package com.gitee.pifeng.monitoring.common.util.secure;

import cn.hutool.crypto.SmUtil;
import com.gitee.pifeng.monitoring.common.init.InitSecure;

import java.nio.charset.Charset;
import java.util.Base64;

/**
 * <p>
 * 国密SM4加解密工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/8/14 9:00
 */
public class Sm4EncryptUtils extends InitSecure {

    /**
     * <p>
     * 字符串SM4加密
     * </p>
     *
     * @param str     需要加密的字符串
     * @param charset 字符集
     * @return 加密后的字符串
     * @author 皮锋
     * @custom.date 2021/8/14 9:01
     */
    public static String encrypt(String str, Charset charset) {
        byte[] key = Base64.getDecoder().decode(SECRET_KEY_SM4);
        return SmUtil.sm4(key).encryptBase64(str, charset);
    }

    /**
     * <p>
     * 字节数组SM4加密
     * </p>
     *
     * @param arry 需要加密的字节数组
     * @return 加密后的字符串
     * @author 皮锋
     * @custom.date 2021/8/14 9:01
     */
    public static String encrypt(byte[] arry) {
        byte[] key = Base64.getDecoder().decode(SECRET_KEY_SM4);
        return SmUtil.sm4(key).encryptBase64(arry);
    }

    /**
     * <p>
     * 字符串SM4解密
     * </p>
     *
     * @param str     需要解密的字符串
     * @param charset 字符集
     * @return 解密后的字符串
     * @author 皮锋
     * @custom.date 2021/8/14 9:01
     */
    public static String decrypt(String str, Charset charset) {
        byte[] key = Base64.getDecoder().decode(SECRET_KEY_SM4);
        return SmUtil.sm4(key).decryptStr(str, charset);
    }

    /**
     * <p>
     * 字符串SM4解密
     * </p>
     *
     * @param str 需要解密的字符串
     * @return 解密后的字节数组
     * @author 皮锋
     * @custom.date 2021/12/21 13:00
     */
    public static byte[] decrypt(String str) {
        byte[] key = Base64.getDecoder().decode(SECRET_KEY_SM4);
        return SmUtil.sm4(key).decrypt(str);
    }

}
