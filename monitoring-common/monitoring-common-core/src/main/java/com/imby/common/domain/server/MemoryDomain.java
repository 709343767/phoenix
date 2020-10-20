package com.imby.common.domain.server;

import com.imby.common.abs.AbstractSuperBean;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 内存信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月3日 下午2:20:14
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class MemoryDomain extends AbstractSuperBean {

    /**
     * 物理内存总量（单位：byte）
     */

    private long memTotal;

    /**
     * 物理内存使用量（单位：byte）
     */
    private long memUsed;

    /**
     * 物理内存剩余量（单位：byte）
     */
    private long memFree;

    /**
     * 物理内存使用率
     */
    private double menUsedPercent;

}
