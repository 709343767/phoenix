package com.gitee.pifeng.server.business.server.domain;

import com.gitee.pifeng.common.abs.AbstractSuperBean;
import lombok.*;
import lombok.experimental.Accessors;

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

    /**
     * 是否已经发送告警信息
     */
    private boolean isAlarm;

}
