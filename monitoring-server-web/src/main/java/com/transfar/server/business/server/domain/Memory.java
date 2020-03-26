package com.transfar.server.business.server.domain;

import com.transfar.common.abs.SuperBean;
import com.transfar.common.domain.server.MemoryDomain;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 服务器内存
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/26 14:53
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class Memory extends SuperBean {

    /**
     * 允许多少秒内内存过载是正常情况
     */
    private int thresholdSecond;

    /**
     * 是否已经发送过内存过载告警消息
     */
    private boolean isAlarm;

    /**
     * 最后一次通过服务器信息包更新的时间
     */
    private Date dateTime;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 内存信息
     */
    private MemoryDomain memoryDomain;

}
