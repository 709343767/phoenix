package com.transfar.server.business.server.domain;

import com.transfar.common.abs.SuperBean;
import com.transfar.common.domain.server.MemoryDomain;
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
     * 内存过载次数
     */
    private int num;

    /**
     * 内存信息获取频率
     */
    private long rate;

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
     * 内存信息
     */
    private MemoryDomain memoryDomain;

}
