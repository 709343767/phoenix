package com.gitee.pifeng.monitoring.common.property.server;

import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import lombok.*;

/**
 * <p>
 * 服务器状态配置属性
 * </p>
 *
 * @author 皮锋
 * @custom.date 2024/1/23 11:33
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MonitoringServerStatusProperties implements ISuperBean {

    /**
     * 是否监控服务器状态
     */
    private boolean enable;

    /**
     * 告警是否打开
     */
    private boolean alarmEnable;

}
