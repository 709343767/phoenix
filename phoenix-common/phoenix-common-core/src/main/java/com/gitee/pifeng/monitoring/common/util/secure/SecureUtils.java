package com.gitee.pifeng.monitoring.common.util.secure;

import cn.hutool.crypto.KeyUtil;
import com.gitee.pifeng.monitoring.common.constant.SecurerEnums;
import com.gitee.pifeng.monitoring.common.init.InitSecure;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;
import java.util.Base64;

/**
 * <p>
 * 加解密工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/8/13 11:08
 */
@Slf4j
public class SecureUtils extends InitSecure {

    /**
     * <p>
     * 字符串加密
     * </p>
     *
     * @param str     需要加密的字符串
     * @param charset 字符集
     * @return 加密后的字符串
     * @author 皮锋
     * @custom.date 2021/8/13 11:11
     */
    public static String encrypt(String str, Charset charset) {
        // 没选择加解密类型，则不加密
        if (StringUtils.isBlank(SECRET_TYPE)) {
            return str;
        }
        return SecurerEnums.valueOf(StringUtils.upperCase(SECRET_TYPE)).encrypt(str, charset);
    }

    /**
     * <p>
     * 字节数组加密
     * </p>
     *
     * @param arry    需要加密的字节数组
     * @param charset 字符集
     * @return 加密后的字符串
     * @author 皮锋
     * @custom.date 2021/12/19 22:51
     */
    public static String encrypt(byte[] arry, Charset charset) {
        // 没选择加解密类型，则不加密
        if (StringUtils.isBlank(SECRET_TYPE)) {
            return new String(arry, charset);
        }
        return SecurerEnums.valueOf(StringUtils.upperCase(SECRET_TYPE)).encrypt(arry);
    }

    /**
     * <p>
     * 字符串解密
     * </p>
     *
     * @param str     需要解密的字符串
     * @param charset 字符集
     * @return 解密后的字符串
     * @author 皮锋
     * @custom.date 2021/8/13 11:11
     */
    public static String decrypt(String str, Charset charset) {
        // 没选择加解密类型，则不解密
        if (StringUtils.isBlank(SECRET_TYPE)) {
            return str;
        }
        return SecurerEnums.valueOf(StringUtils.upperCase(SECRET_TYPE)).decrypt(str, charset);
    }

    /**
     * <p>
     * 字符串解密
     * </p>
     *
     * @param str 需要解密的字符串
     * @return 解密后的字节数组
     * @author 皮锋
     * @custom.date 2021/12/21 13:08
     */
    @SneakyThrows
    public static byte[] decrypt(String str, String charset) {
        // 没选择加解密类型，则不解密
        if (StringUtils.isBlank(SECRET_TYPE)) {
            return str.getBytes(charset);
        }
        return SecurerEnums.valueOf(StringUtils.upperCase(SECRET_TYPE)).decrypt(str);
    }

    /**
     * <p>
     * 仅用于对称加密和摘要算法密钥生成
     * </p>
     *
     * @param algorithm 算法，支持PBE算法
     * @param keySize   密钥长度，&lt;0表示不设定密钥长度，即使用默认长度
     * @return 明文秘钥字符串
     * @author 皮锋
     * @custom.date 2021/8/14 14:41
     */
    public static String generateKey(String algorithm, int keySize) {
        return Base64.getEncoder().encodeToString(KeyUtil.generateKey(algorithm, keySize).getEncoded());
    }

}
