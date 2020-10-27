package com.imby.plug.property;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 与服务端相关的监控属性
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月4日 下午2:45:10
 */
@Data
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class MonitoringServerProperties {

    /**
     * 监控服务端url
     */
    private String url;

}
