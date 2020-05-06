package com.imby.server.business.server.domain;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

import com.imby.common.abs.InstanceBean;

/**
 * <p>
 * 应用实例
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月9日 上午10:49:29
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class Instance extends InstanceBean {

    /**
     * 允许多少秒内没收到心跳包是正常情况
     */
    private int thresholdSecond;

    /**
     * 是否在线
     */
    private boolean isOnline;

    /**
     * 是否网络可连接
     */
    private boolean isOnConnect;

    /**
     * 最后一次通过心跳包更新的时间
     */
    private Date dateTime;

    /**
     * 是否已经发送过离线告警消息
     */
    private boolean isLineAlarm;

    /**
     * 是否已经发送过断网告警消息
     */
    private boolean isConnectAlarm;

}
