package com.gitee.pifeng.server.business.server.domain;

import com.gitee.pifeng.common.abs.AbstractSuperBean;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 服务器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/12/17 19:53
 */
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class Server extends AbstractSuperBean {

    /**
     * 允许多少秒内没收到服务器信息包是正常情况
     */
    private int thresholdSecond;

    /**
     * 是否在线
     */
    private boolean isOnline;

    /**
     * 最后一次通过服务器信息包更新的时间
     */
    private Date dateTime;

    /**
     * 是否已经发送过离线告警消息
     */
    private boolean isLineAlarm;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 计算机名
     */
    private String computerName;

    /**
     * 是否首次发现新服务器
     */
    private boolean isFirstDiscovery;

}
