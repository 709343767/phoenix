package com.gitee.pifeng.monitoring.common.property.server;

import lombok.*;

/**
 * <p>
 * HTTP配置属性
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/1/11 16:33
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MonitoringHttpProperties {

    /**
     * 是否监控HTTP服务
     */
    private boolean enable;

    /**
     * HTTP状态配置属性
     */
    private MonitoringHttpStatusProperties httpStatusProperties;

}
