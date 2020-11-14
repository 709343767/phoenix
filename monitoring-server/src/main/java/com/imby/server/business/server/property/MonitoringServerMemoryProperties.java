package com.imby.server.business.server.property;

import lombok.*;

/**
 * <p>
 * 服务器内存配置属性
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/11/14 21:56
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MonitoringServerMemoryProperties {

    /**
     * 过载阈值
     */
    private double overloadThreshold;

}
