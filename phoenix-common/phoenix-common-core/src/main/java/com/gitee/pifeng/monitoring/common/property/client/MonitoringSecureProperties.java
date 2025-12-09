package com.gitee.pifeng.monitoring.common.property.client;

import com.gitee.pifeng.monitoring.common.constant.SecurerEnums;
import com.gitee.pifeng.monitoring.common.init.InitSecure;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 安全相关监控属性
 * </p>
 * 注意：这些属性名称不能随意改动，因为在 {@link InitSecure} 用了反射来获取这些属性
 *
 * @author 皮锋
 * @custom.date 2025年12月7日 上午10:11:46
 */
@Data
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class MonitoringSecureProperties {

    /**
     * 加密算法类型
     */
    private SecurerEnums encryptionAlgorithmType;

    /**
     * DES加密算法属性
     */
    private MonitoringSecureDesProperties des;

    /**
     * AES加密算法属性
     */
    private MonitoringSecureAesProperties aes;

    /**
     * SM4加密算法属性
     */
    private MonitoringSecureSm4Properties sm4;

}
