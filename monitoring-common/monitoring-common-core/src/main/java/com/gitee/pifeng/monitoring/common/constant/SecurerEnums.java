package com.gitee.pifeng.monitoring.common.constant;

import com.gitee.pifeng.monitoring.common.inf.ISecurer;
import com.gitee.pifeng.monitoring.common.util.secure.AesEncryptUtils;
import com.gitee.pifeng.monitoring.common.util.secure.DesEncryptUtils;

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
    DES() {
        @Override
        public String encrypt(String str) {
            return DesEncryptUtils.encrypt(str);
        }

        @Override
        public String decrypt(String str) {
            return DesEncryptUtils.decrypt(str);
        }
    },

    /**
     * AES加解密
     */
    AES() {
        @Override
        public String encrypt(String str) {
            return AesEncryptUtils.encrypt(str);
        }

        @Override
        public String decrypt(String str) {
            return AesEncryptUtils.decrypt(str);
        }
    }
}
