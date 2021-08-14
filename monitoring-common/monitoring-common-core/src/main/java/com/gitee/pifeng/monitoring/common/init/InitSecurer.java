package com.gitee.pifeng.monitoring.common.init;

import cn.hutool.crypto.KeyUtil;
import cn.hutool.setting.dialect.Props;
import com.gitee.pifeng.monitoring.common.constant.SecurerEnums;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * <p>
 * 初始化加解密工具
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/8/13 11:08
 */
public class InitSecurer {

    /**
     * 读取“monitoring-bootstrap.properties”文件
     */
    private static final Props PROPS = new Props("monitoring-bootstrap.properties", StandardCharsets.UTF_8);

    /**
     * 加解密类型
     */
    public static final String SECRET_TYPE = PROPS.getStr("secret.type");

    /**
     * DES密钥
     */
    public static final String SECRET_KEY_DES = PROPS.getStr("secret.key.des");

    /**
     * AES密钥
     */
    public static final String SECRET_KEY_AES = PROPS.getStr("secret.key.aes");


    /**
     * 国密SM4密钥
     */
    public static final String SECRET_KEY_SM4 = PROPS.getStr("secret.key.sm4");

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
