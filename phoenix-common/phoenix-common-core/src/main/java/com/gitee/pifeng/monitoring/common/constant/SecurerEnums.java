package com.gitee.pifeng.monitoring.common.constant;

import com.gitee.pifeng.monitoring.common.inf.ISecurer;
import com.gitee.pifeng.monitoring.common.util.secure.AesEncryptUtils;
import com.gitee.pifeng.monitoring.common.util.secure.DesEncryptUtils;
import com.gitee.pifeng.monitoring.common.util.secure.Sm4EncryptUtils;

import java.nio.charset.Charset;

/**
 * <p>
 * 加解密类型枚举
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/8/13 12:00
 */
public enum SecurerEnums implements ISecurer {

    /**
     * DES加解密
     */
    DES {
        @Override
        public String encrypt(String str, Charset charset) {
            return DesEncryptUtils.encrypt(str, charset);
        }

        @Override
        public String encrypt(byte[] arry) {
            return DesEncryptUtils.encrypt(arry);
        }

        @Override
        public String decrypt(String str, Charset charset) {
            return DesEncryptUtils.decrypt(str, charset);
        }

        @Override
        public byte[] decrypt(String str) {
            return DesEncryptUtils.decrypt(str);
        }
    },

    /**
     * AES加解密
     */
    AES {
        @Override
        public String encrypt(String str, Charset charset) {
            return AesEncryptUtils.encrypt(str, charset);
        }

        @Override
        public String encrypt(byte[] arry) {
            return AesEncryptUtils.encrypt(arry);
        }

        @Override
        public String decrypt(String str, Charset charset) {
            return AesEncryptUtils.decrypt(str, charset);
        }

        @Override
        public byte[] decrypt(String str) {
            return AesEncryptUtils.decrypt(str);
        }
    },

    /**
     * 国密SM4加解密
     */
    SM4 {
        @Override
        public String encrypt(String str, Charset charset) {
            return Sm4EncryptUtils.encrypt(str, charset);
        }

        @Override
        public String encrypt(byte[] arry) {
            return Sm4EncryptUtils.encrypt(arry);
        }

        @Override
        public String decrypt(String str, Charset charset) {
            return Sm4EncryptUtils.decrypt(str, charset);
        }

        @Override
        public byte[] decrypt(String str) {
            return Sm4EncryptUtils.decrypt(str);
        }
    }
}
