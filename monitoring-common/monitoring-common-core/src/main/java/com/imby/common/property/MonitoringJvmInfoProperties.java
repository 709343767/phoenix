package com.imby.common.property;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * Java虚拟机信息属性
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/14 21:00
 */
@Data
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class MonitoringJvmInfoProperties {

    /**
     * 是否采集Java虚拟机信息
     */
    private boolean enable;

    /**
     * 发送Java虚拟机信息的频率
     */
    private long rate;

}
