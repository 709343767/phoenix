package com.gitee.pifeng.monitoring.common.property.client;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 应用程序监控属性
 * </p>
 *
 * @author 皮锋
 * @custom.date 2024年3月23日 下午21:20:00
 */
@Data
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class MonitoringInstanceProperties {

    /**
     * 实例次序（不能重复）
     */
    private Integer order;

    /**
     * 实例端点类型（服务端、代理端、客户端、UI端）
     */
    private String endpoint;

    /**
     * 实例名称
     */
    private String name;

    /**
     * 实例描述
     */
    private String desc;

    /**
     * 程序语言
     */
    private String language;

}
