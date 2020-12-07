package com.gitee.pifeng.common.domain.jvm;

import com.gitee.pifeng.common.abs.AbstractSuperBean;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * <p>
 * 内存信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/14 12:16
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class MemoryDomain extends AbstractSuperBean {

    /**
     * 堆内存信息
     */
    private HeapMemoryUsageDomain heapMemoryUsageDomain;

    /**
     * 堆外内存信息
     */
    private NonHeapMemoryUsageDomain nonHeapMemoryUsageDomain;

    /**
     * 内存池信息
     */
    private Map<String, MemoryPoolDomain> memoryPoolDomainMap;

    /**
     * <p>
     * 堆内存信息
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/8/14 12:32
     */
    @Data
    @ToString
    @Accessors(chain = true)
    @EqualsAndHashCode(callSuper = true)
    public static class HeapMemoryUsageDomain extends MemoryUsage {
    }

    /**
     * <p>
     * 堆外内存信息
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/8/14 12:32
     */
    @Data
    @ToString
    @Accessors(chain = true)
    @EqualsAndHashCode(callSuper = true)
    public static class NonHeapMemoryUsageDomain extends MemoryUsage {
    }

    /**
     * 内存池信息
     */
    @Data
    @ToString
    @Accessors(chain = true)
    @EqualsAndHashCode(callSuper = true)
    public static class MemoryPoolDomain extends MemoryUsage {
    }

}

/**
 * <p>
 * 内存使用情况
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/14 12:28
 */
@Data
@EqualsAndHashCode(callSuper = true)
class MemoryUsage extends AbstractSuperBean {

    /**
     * 初始内存量（单位：byte）
     */
    private long init;

    /**
     * 已用内存量（单位：byte）
     */
    private long used;

    /**
     * 提交内存量（单位：byte）
     */
    private long committed;

    /**
     * 最大内存量（单位：byte，可能存在未定义）
     */
    private String max;

}


