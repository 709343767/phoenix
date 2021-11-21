package com.gitee.pifeng.monitoring.common.property.bootstrap;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 监控引导配置属性
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/11/21 19:50
 */
@Data
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class MonitoringBootstrapProperties {

    /**
     * 加解密属性
     */
    private MonitoringSecretProperties secretProperties;

}
