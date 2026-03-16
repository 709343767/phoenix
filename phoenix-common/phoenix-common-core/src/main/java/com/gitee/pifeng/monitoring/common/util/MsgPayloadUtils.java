package com.gitee.pifeng.monitoring.common.util;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ZipUtil;
import com.alibaba.fastjson.JSON;
import com.gitee.pifeng.monitoring.common.dto.CiphertextPackage;
import com.gitee.pifeng.monitoring.common.util.secure.SecureUtils;

import java.nio.charset.StandardCharsets;

/**
 * <p>
 * 消息负载加解密与压缩工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2026/2/26 09:16
 */
public class MsgPayloadUtils {

    /**
     * <p>
     * 私有化构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2026/2/26 09:28
     */
    private MsgPayloadUtils() {
    }

    /**
     * <p>
     * 将 明文JSON字符串 转换成 密文数据包（自动判断是否压缩）
     * </p>
     *
     * @param plaintextJsonStr 明文JSON字符串
     * @return {@link CiphertextPackage} 密文数据包
     * @author 皮锋
     * @custom.date 2026/2/26 10:02
     */
    public static CiphertextPackage encryptPayloadTo(String plaintextJsonStr) {
        CiphertextPackage requestCiphertextPackage;
        // 字符串是否需要进行gzip压缩
        boolean isNeedGzip = ZipUtils.isNeedGzip(plaintextJsonStr);
        // 字符串是否需要进行gzip压缩
        if (isNeedGzip) {
            // 压缩字符串
            byte[] gzip = ZipUtil.gzip(plaintextJsonStr, CharsetUtil.UTF_8);
            // 加密请求数据
            String encrypt = SecureUtils.encrypt(gzip);
            requestCiphertextPackage = new CiphertextPackage(encrypt, true);
        } else {
            // 加密请求数据
            String encrypt = SecureUtils.encrypt(plaintextJsonStr, StandardCharsets.UTF_8);
            requestCiphertextPackage = new CiphertextPackage(encrypt, false);
        }
        return requestCiphertextPackage;
    }

    /**
     * <p>
     * 将 明文JSON字符串 转换成 密文JSON字符串（自动判断是否压缩）
     * </p>
     *
     * @param plaintextJsonStr 明文JSON字符串
     * @return 密文JSON字符串
     * @author 皮锋
     * @custom.date 2026/2/26 09:19
     */
    public static String encryptPayload(String plaintextJsonStr) {
        return encryptPayloadTo(plaintextJsonStr).toJsonString();
    }

    /**
     * <p>
     * 将 密文数据包 转换成 明文JSON字符串
     * </p>
     *
     * @param ciphertextPackage {@link CiphertextPackage} 密文数据包
     * @return 明文JSON字符串
     * @author 皮锋
     * @custom.date 2026/2/26 10:07
     */
    public static String decryptPayloadFrom(CiphertextPackage ciphertextPackage) {
        // 解密响应数据
        String decryptStr;
        // 是否需要进行UnGzip
        boolean isUnGzipEnabled = ciphertextPackage.isUnGzipEnabled();
        // 加密后的数据
        String ciphertext = ciphertextPackage.getCiphertext();
        if (isUnGzipEnabled) {
            // 解密
            byte[] decrypt = SecureUtils.decrypt(ciphertext);
            // 解压
            decryptStr = ZipUtil.unGzip(decrypt, CharsetUtil.UTF_8);
        } else {
            // 解密
            decryptStr = SecureUtils.decrypt(ciphertext, StandardCharsets.UTF_8);
        }
        return decryptStr;
    }

    /**
     * <p>
     * 将 密文JSON字符串 转换成 明文JSON字符串
     * </p>
     *
     * @param ciphertextJsonStr 密文JSON字符串
     * @return 明文JSON字符串
     * @author 皮锋
     * @custom.date 2026/2/26 09:24
     */
    public static String decryptPayload(String ciphertextJsonStr) {
        // 转成密文数据包
        CiphertextPackage responseCiphertextPackage = JSON.parseObject(ciphertextJsonStr, CiphertextPackage.class);
        return decryptPayloadFrom(responseCiphertextPackage);
    }

}