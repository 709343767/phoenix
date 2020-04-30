package com.imby.common.property;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 与自己相关的监控属性
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月4日 下午3:07:32
 */
@Data
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class MonitoringOwnProperties {

    /**
     * 实例ID
     */
    private String instanceId;

    /**
     * 实例名称
     */
    private String instanceName;

}
