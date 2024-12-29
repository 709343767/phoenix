package com.gitee.pifeng.monitoring.common.property.client;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 服务器信息属性
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月8日 下午2:27:42
 */
@Data
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class MonitoringServerInfoProperties {

    /**
     * 是否采集服务器信息
     */
    private Boolean enable;

    /**
     * 是否使用 Sigar 采集服务器信息
     */
    private Boolean userSigarEnable;

    /**
     * 发送服务器信息的频率
     */
    private Long rate;

    /**
     * 服务器本机IP地址
     */
    private String ip;

}
