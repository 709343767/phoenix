package com.gitee.pifeng.monitoring.common.property.server;

import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import lombok.*;

/**
 * <p>
 * 应用实例配置属性
 * </p>
 *
 * @author 皮锋
 * @custom.date 2024/1/22 14:25
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MonitoringInstanceProperties implements ISuperBean {

    /**
     * 是否监控应用实例
     */
    private boolean enable;

    /**
     * 应用实例状态配置属性
     */
    private MonitoringInstanceStatusProperties instanceStatusProperties;

}