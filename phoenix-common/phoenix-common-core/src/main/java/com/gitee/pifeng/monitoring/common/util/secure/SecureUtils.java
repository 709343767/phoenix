package com.gitee.pifeng.monitoring.common.util.secure;

import cn.hutool.crypto.KeyUtil;
import com.gitee.pifeng.monitoring.common.constant.SecurerEnums;
import com.gitee.pifeng.monitoring.common.init.InitSecure;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

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
     * @param str 需要加密的字符串
     * @return 加密后的字符串
     * @author 皮锋
     * @custom.date 2021/8/13 11:11
     */
    public static String encrypt(String str) {
        // 没选择加解密类型，则不加密
        if (StringUtils.isBlank(SECRET_TYPE)) {
            return str;
        }
        return SecurerEnums.valueOf(StringUtils.upperCase(SECRET_TYPE)).encrypt(str);
    }

    /**
     * <p>
     * 字符串解密
     * </p>
     *
     * @param str 需要解密的字符串
     * @return 解密后的字符串
     * @author 皮锋
     * @custom.date 2021/8/13 11:11
     */
    public static String decrypt(String str) {
        // 没选择加解密类型，则不解密
        if (StringUtils.isBlank(SECRET_TYPE)) {
            return str;
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
