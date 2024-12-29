package com.gitee.pifeng.monitoring.common.property.server;

import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import lombok.*;

/**
 * <p>
 * 数据库状态配置属性
 * </p>
 *
 * @author 皮锋
 * @custom.date 2024/1/23 11:36
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MonitoringDbStatusProperties implements ISuperBean {

    /**
     * 是否监控数据库状态
     */
    private boolean enable;

    /**
     * 告警是否打开
     */
    private boolean alarmEnable;

}
