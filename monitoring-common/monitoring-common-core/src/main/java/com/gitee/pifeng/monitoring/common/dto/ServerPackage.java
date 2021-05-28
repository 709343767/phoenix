package com.gitee.pifeng.monitoring.common.dto;

import com.gitee.pifeng.monitoring.common.domain.Server;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 服务器信息包
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月6日 上午9:54:41
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ServerPackage extends BaseRequestPackage {

    /**
     * 服务器信息
     */
    private Server server;

    /**
     * 传输频率
     */
    private long rate;

}
