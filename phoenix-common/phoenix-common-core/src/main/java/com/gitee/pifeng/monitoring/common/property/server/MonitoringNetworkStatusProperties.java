package com.gitee.pifeng.monitoring.common.property.server;

import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import lombok.*;

/**
 * <p>
 * 网络状态配置属性
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/23 10:46
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MonitoringNetworkStatusProperties implements ISuperBean {

    /**
     * 是否监控网络状态
     */
    private boolean enable;

    /**
     * 告警是否打开
     */
    private boolean alarmEnable;

}
