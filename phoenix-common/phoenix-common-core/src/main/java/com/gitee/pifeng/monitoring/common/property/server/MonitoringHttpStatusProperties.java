package com.gitee.pifeng.monitoring.common.property.server;

import lombok.*;

/**
 * <p>
 * HTTP状态配置属性
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
public class MonitoringHttpStatusProperties {

    /**
     * 是否监控HTTP状态
     */
    private boolean enable;

    /**
     * 告警是否打开
     */
    private boolean alarmEnable;

}
