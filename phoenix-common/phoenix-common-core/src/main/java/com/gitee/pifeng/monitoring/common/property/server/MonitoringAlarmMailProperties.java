package com.gitee.pifeng.monitoring.common.property.server;

import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import lombok.*;

/**
 * <p>
 * 告警邮箱配置属性
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
public class MonitoringAlarmMailProperties implements ISuperBean {

    /**
     * 收件人邮箱地址
     */
    private String[] emills;

}
