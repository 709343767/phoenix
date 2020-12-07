package com.gitee.pifeng.common.domain;

import com.gitee.pifeng.common.abs.AbstractSuperBean;
import com.gitee.pifeng.common.domain.server.*;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 服务器信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/3 11:26
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public final class Server extends AbstractSuperBean {

    /**
     * 操作系统信息
     */
    private OsDomain osDomain;

    /**
     * 内存信息
     */
    private MemoryDomain memoryDomain;

    /**
     * Cpu信息
     */
    private CpuDomain cpuDomain;

    /**
     * 网卡信息
     */
    private NetDomain netDomain;

    /**
     * 磁盘信息
     */
    private DiskDomain diskDomain;

}
