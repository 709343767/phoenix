package com.gitee.pifeng.common.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * <p>
 * DES加解密工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/4/28 15:47
 */
@Slf4j
public class DesEncryptUtils {

    /**
     * 密钥是16位长度的byte[]进行Base64转换后得到的字符串
     */
    public static final String KEY = "cc839cf9feca4ed7oa65064177a0b505";

    /**
     * <p>
     * 字符串DES加密
     * </p>
     *
     * @param str 需要加密的字符串
     * @return 加密后的字符串
     * @author 皮锋
     * @custom.date 2020/4/28 15:55
     */
    public static String encrypt(String str) {
        // 使用DES算法使用加密消息体
        String result = null;
        try {
            // 取需要加密内容的utf-8编码
            byte[] encrypt = str.getBytes(StandardCharsets.UTF_8);
            // 取MD5Hash码，并组合加密数组
            byte[] md5Hasn = DesEncryptUtils.md5Hash(encrypt, 0, encrypt.length);
            // 组合消息体
            byte[] totalByte = DesEncryptUtils.addMd5(md5Hasn, encrypt);
            // 取密钥和偏转向量
            byte[] key = new byte[8];
            byte[] iv = new byte[8];
            getKeyIV(key, iv);
            SecretKeySpec deskey = new SecretKeySpec(key, "DES");
            IvParameterSpec ivParam = new IvParameterSpec(iv);
            byte[] temp = DesEncryptUtils.desCbcEncrypt(totalByte, deskey, ivParam);
            result = Base64.getEncoder().encodeToString(temp);
        } catch (Exception e) {
            log.error("字符串DES加密失败！", e);
        }
        // 使用Base64加密后返回
        return result;
    }

    /**
     * <p>
     * 字符串DES解密
     * </p>
     *
     * @param str 需要解密的字符串
     * @return 解密后的字符串
     * @author 皮锋
     * @custom.date 2020/4/28 15:57
     */
    public static String decrypt(String str) {
        // 使用DES算法解密
        String result = null;
        try {
            // base64解码
            byte[] encBuf = Base64.getDecoder().decode(str);
            // 取密钥和偏转向量
            byte[] key = new byte[8];
            byte[] iv = new byte[8];
            getKeyIV(key, iv);
            SecretKeySpec deskey = new SecretKeySpec(key, "DES");
            IvParameterSpec ivParam = new IvParameterSpec(iv);
            byte[] temp = DesEncryptUtils.desCbcDecrypt(encBuf, deskey, ivParam);
            // 进行解密后的md5Hash校验
            byte[] md5Hash = DesEncryptUtils.md5Hash(temp, 16, temp.length - 16);
            // 进行解密校检
            for (int i = 0; i < md5Hash.length; i++) {
                if (md5Hash[i] != temp[i]) {
                    throw new Exception();
                }
            }
            result = new String(temp, 16, temp.length - 16, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("字符串DES解密失败！", e);
        }
        // 返回解密后的数组，其中前16位MD5Hash码要除去
        return result;
    }

    /**
     * <p>
     * 经过封装的DES/CBC加密算法，如果包含中文，请注意编码
     * </p>
     *
     * @param sourceBuf 需要加密内容的字节数组
     * @param deskey    KEY由8位字节数组通过SecretKeySpec类转换而成
     * @param ivParam   IV偏转向量，由8位字节数组通过IvParameterSpec类转换而成
     * @return 加密后的字节数组
     * @throws Exception 异常
     * @author 皮锋
     * @custom.date 2020/4/28 16:06
     */
    private static byte[] desCbcEncrypt(byte[] sourceBuf, SecretKeySpec deskey, IvParameterSpec ivParam)
            throws Exception {
        byte[] cipherByte;
        // 使用DES对称加密算法的CBC模式加密
        Cipher encrypt = Cipher.getInstance("DES/CBC/PKCS5Padding");
        encrypt.init(Cipher.ENCRYPT_MODE, deskey, ivParam);
        cipherByte = encrypt.doFinal(sourceBuf, 0, sourceBuf.length);
        // 返回加密后的字节数组
        return cipherByte;
    }

    /**
     * <p>
     * 经过封装的DES/CBC解密算法
     * </p>
     *
     * @param sourceBuf 需要解密内容的字节数组
     * @param deskey    KEY由8位字节数组通过SecretKeySpec类转换而成
     * @param ivParam   IV偏转向量，由6位字节数组通过IvParameterSpec类转换而成
     * @return 解密后的字节数组
     * @throws Exception 异常
     * @author 皮锋
     * @custom.date 2020/4/28 16:07
     */
    private static byte[] desCbcDecrypt(byte[] sourceBuf, SecretKeySpec deskey, IvParameterSpec ivParam)
            throws Exception {
        byte[] cipherByte;
        // 获得Cipher实例，使用CBC模式
        Cipher decrypt = Cipher.getInstance("DES/CBC/PKCS5Padding");
        // 初始化加密实例，定义为解密功能，并传入密钥，偏转向量
        decrypt.init(Cipher.DECRYPT_MODE, deskey, ivParam);
        cipherByte = decrypt.doFinal(sourceBuf, 0, sourceBuf.length);
        // 返回解密后的字节数组
        return cipherByte;
    }

    /**
     * <p>
     * 把MD5进行了简单的封装，以适用于加、解密字符串的校验
     * </p>
     *
     * @param buf    需要MD5加密字节数组
     * @param offset 加密数据起始位置
     * @param length 需要加密的数组长度
     * @return byte数组
     * @throws Exception 异常
     * @author 皮锋
     * @custom.date 2020/4/28 16:08
     */
    private static byte[] md5Hash(byte[] buf, int offset, int length) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(buf, offset, length);
        return md.digest();
    }

    /**
     * <p>
     * MD5校验码组合方法，前16位放MD5Hash码，把MD5验证码byte[]、加密内容byte[]组合的方法
     * </p>
     *
     * @param md5Byte  加密内容的MD5Hash字节数组
     * @param bodyByte 加密内容字节数组
     * @return 组合后的字节数组，比加密内容长16个字节
     * @author 皮锋
     * @custom.date 2020/4/28 16:11
     */
    private static byte[] addMd5(byte[] md5Byte, byte[] bodyByte) {
        int length = bodyByte.length + md5Byte.length;
        byte[] resultByte = new byte[length];
        // 前16位放MD5Hash码
        for (int i = 0; i < length; i++) {
            if (i < md5Byte.length) {
                resultByte[i] = md5Byte[i];
            } else {
                resultByte[i] = bodyByte[i - md5Byte.length];
            }
        }
        return resultByte;
    }

    /**
     * <p>
     * 获取key和iv
     * </p>
     *
     * @param key key数组
     * @param iv  iv向量数组
     * @author 皮锋
     * @custom.date 2020/4/28 16:13
     */
    private static void getKeyIV(byte[] key, byte[] iv) {
        // 密钥Base64解密
        byte[] buf = Base64.getDecoder().decode(DesEncryptUtils.KEY);
        // 前8位为key
        int i;
        for (i = 0; i < key.length; i++) {
            assert buf != null;
            key[i] = buf[i];
        }
        // 后8位为iv向量
        for (i = 0; i < iv.length; i++) {
            assert buf != null;
            iv[i] = buf[i + 8];
        }
    }

}
