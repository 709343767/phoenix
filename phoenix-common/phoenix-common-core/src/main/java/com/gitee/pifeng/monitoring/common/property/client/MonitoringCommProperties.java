package com.gitee.pifeng.monitoring.common.property.client;

import com.gitee.pifeng.monitoring.common.constant.CommFrameworkTypeEnums;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 与通信相关的监控属性
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月4日 下午2:45:10
 */
@Data
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class MonitoringCommProperties {

    /**
     * 与服务端通信的通信框架类型
     */
    private CommFrameworkTypeEnums commFrameworkType;

    /**
     * 与HTTP通信相关的监控属性
     */
    private MonitoringCommHttpProperties http;

}
