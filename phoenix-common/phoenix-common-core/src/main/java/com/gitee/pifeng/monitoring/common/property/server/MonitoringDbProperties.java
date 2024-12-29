package com.gitee.pifeng.monitoring.common.property.server;

import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import lombok.*;

/**
 * <p>
 * 数据库配置属性
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/12/15 12:27
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MonitoringDbProperties implements ISuperBean {

    /**
     * 是否监控数据库
     */
    private boolean enable;

    /**
     * 数据库状态配置属性
     */
    private MonitoringDbStatusProperties dbStatusProperties;

    /**
     * 数据库表空间配置属性
     */
    private MonitoringDbTableSpaceProperties dbTableSpaceProperties;

}
