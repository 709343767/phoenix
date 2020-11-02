package com.imby.server.business.server.property;

import lombok.*;

/**
 * <p>
 * 邮箱配置属性
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/4/13 11:13
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MonitoringMailProperties {

    /**
     * 收件人邮箱地址
     */
    private String[] to;
}
