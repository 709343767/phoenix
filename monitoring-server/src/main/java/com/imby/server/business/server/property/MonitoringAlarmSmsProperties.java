package com.imby.server.business.server.property;

import lombok.*;

/**
 * <p>
 * 告警短信配置属性
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月10日 下午2:22:46
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MonitoringAlarmSmsProperties {

    /**
     * 手机号码
     */
    private String[] phoneNumbers;

    /**
     * 接口地址
     */
    private String address;

    /**
     * 接口协议
     */
    private String protocol;

    /**
     * 接口所属企业
     */
    private String enterprise;

}
