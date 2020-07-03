package com.imby.common.domain.server;

import com.imby.common.abs.AbstractSuperBean;
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
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public final class ServerDomain extends AbstractSuperBean {

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
     * JVM信息
     */
    private JvmDomain jvmDomain;

    /**
     * 磁盘信息
     */
    private DiskDomain diskDomain;

}
