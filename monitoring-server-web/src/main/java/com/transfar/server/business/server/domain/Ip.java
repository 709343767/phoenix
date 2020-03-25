package com.transfar.server.business.server.domain;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

import com.transfar.common.abs.SuperBean;

/**
 * <p>
 * IP地址
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/25 10:26
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class Ip extends SuperBean {

    /**
     * 允许多少秒内没更新是正常情况
     */
    private int thresholdSecond;

    /**
     * 是否网络可连接
     */
    private boolean isOnConnect;

    /**
     * 最后一次更新的时间
     */
    private Date dateTime;

    /**
     * 是否已经发送过断网告警消息
     */
    private boolean isConnectAlarm;

}
