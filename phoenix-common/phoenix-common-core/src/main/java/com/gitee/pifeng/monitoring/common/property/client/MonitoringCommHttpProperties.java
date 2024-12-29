package com.gitee.pifeng.monitoring.common.property.client;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 与HTTP通信相关的监控属性
 * </p>
 *
 * @author 皮锋
 * @custom.date 2024/3/14 15:00
 */
@Data
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class MonitoringCommHttpProperties {

    /**
     * 监控服务端url
     */
    private String url;

    /**
     * 连接超时时间（毫秒）
     */
    private Integer connectTimeout;

    /**
     * 等待数据超时时间（毫秒）
     */
    private Integer socketTimeout;

    /**
     * 从连接池获取连接的等待超时时间（毫秒）
     */
    private Integer connectionRequestTimeout;

}
