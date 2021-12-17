package com.gitee.pifeng.monitoring.common.domain.jvm;

import com.gitee.pifeng.monitoring.common.abs.AbstractSuperBean;
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
     * 不同内存类型的内存使用量
     */
    private Map<String, MemoryUsageDomain> memoryUsageDomainMap;

    /**
     * 内存使用量
     */
    @Data
    @ToString
    @Accessors(chain = true)
    @EqualsAndHashCode(callSuper = true)
    public static class MemoryUsageDomain extends AbstractSuperBean {

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

}



