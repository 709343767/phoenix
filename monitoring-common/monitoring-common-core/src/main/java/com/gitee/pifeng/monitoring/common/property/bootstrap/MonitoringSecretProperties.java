package com.gitee.pifeng.monitoring.common.property.bootstrap;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 加解密属性
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/11/21 19:57
 */
@Data
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class MonitoringSecretProperties {

    /**
     * 加解密类型
     */
    public String secretType;

    /**
     * DES密钥
     */
    public String secretKeyDes;

    /**
     * AES密钥
     */
    public String secretKeyAes;

    /**
     * 国密SM4密钥
     */
    public String secretKeySm4;

}
