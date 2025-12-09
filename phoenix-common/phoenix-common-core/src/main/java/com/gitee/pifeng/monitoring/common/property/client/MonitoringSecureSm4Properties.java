package com.gitee.pifeng.monitoring.common.property.client;

import com.gitee.pifeng.monitoring.common.init.InitSecure;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * SM4加密算法属性
 * </p>
 *
 * @author 皮锋
 * @custom.date 2025年12月7日 上午10:24:32
 */
@Data
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class MonitoringSecureSm4Properties {

    /**
     * 秘钥
     * 注意：这个属性名称不能随意改动，因为在 {@link InitSecure} 用了反射来获取这个属性
     */
    private String key;
    
}
