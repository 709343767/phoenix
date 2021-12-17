package com.gitee.pifeng.monitoring.common.dto;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 心跳包
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月4日 下午12:20:06
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class HeartbeatPackage extends BaseRequestPackage {

    /**
     * 心跳频率
     */
    private long rate;

}
