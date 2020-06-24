package com.imby.common.domain.server;

import com.imby.common.abs.SuperBean;
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
public class MemoryDomain extends SuperBean {

    /**
     * 物理内存总量
     */
    private String memTotal;
    /**
     * 物理内存使用量
     */
    private String memUsed;
    /**
     * 物理内存剩余量
     */
    private String memFree;

    /**
     * 物理内存使用率
     */
    private String menUsedPercent;

}
