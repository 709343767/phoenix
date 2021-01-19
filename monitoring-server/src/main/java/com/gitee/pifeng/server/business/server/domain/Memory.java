package com.gitee.pifeng.server.business.server.domain;

import com.gitee.pifeng.common.abs.AbstractSuperBean;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 服务器内存
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/26 14:53
 */
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class Memory extends AbstractSuperBean {

    /**
     * 内存过载次数
     */
    private int num;

    /**
     * 是否已经发送过内存过载告警消息
     */
    private boolean isAlarm;

    /**
     * 是否确认内存过载
     */
    private boolean isOverLoad;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 计算机名
     */
    private String computerName;

    /**
     * 内存使用率
     */
    private double usedPercent;

}
