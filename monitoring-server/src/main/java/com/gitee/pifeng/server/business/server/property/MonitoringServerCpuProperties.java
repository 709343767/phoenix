package com.gitee.pifeng.server.business.server.property;

import lombok.*;

/**
 * <p>
 * 服务器CPU配置属性
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/11/14 21:54
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MonitoringServerCpuProperties {

    /**
     * 过载阈值
     */
    private double overloadThreshold;

    /**
     * 监控级别，四级：INFO &#60; WARN &#60; ERROR &#60; FATAL
     */
    private String level;

}
