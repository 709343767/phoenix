package com.gitee.pifeng.monitoring.common.property.server;

import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import lombok.*;

/**
 * <p>
 * 应用实例状态配置属性
 * </p>
 *
 * @author 皮锋
 * @custom.date 2024/1/22 14:33
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MonitoringInstanceStatusProperties implements ISuperBean {

    /**
     * 是否监控应用实例状态
     */
    private boolean enable;

    /**
     * 告警是否打开
     */
    private boolean alarmEnable;

}
