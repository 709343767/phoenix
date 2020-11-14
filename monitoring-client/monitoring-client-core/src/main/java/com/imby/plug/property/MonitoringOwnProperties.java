package com.imby.plug.property;

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
     * 实例次序（不能重复）
     */
    private int instanceOrder;

    /**
     * 实例端点类型（服务端、代理端、客户端）
     */
    private String instanceEndpoint;

    /**
     * 实例名称
     */
    private String instanceName;

    /**
     * 实例描述
     */
    private String instanceDesc;

    /**
     * 程序语言
     */
    private String instanceLanguage;

}
