package com.imby.common.domain.jvm;

import com.imby.common.abs.AbstractSuperBean;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * GC信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/14 16:11
 */
@Data
@Builder
@ToString
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class GarbageCollectorDomain extends AbstractSuperBean {

    /**
     * 详细GC信息列表
     */
    private List<GarbageCollectorInfoDomain> garbageCollectorInfoDomains;


    /**
     * <p>
     * 详细GC信息
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/8/14 16:24
     */
    @Data
    @Builder
    @ToString
    @Accessors(chain = true)
    @EqualsAndHashCode(callSuper = true)
    public static class GarbageCollectorInfoDomain extends AbstractSuperBean {

        /**
         * 内存管理器名称
         */
        private String name;

        /**
         * GC总次数
         */
        private String collectionCount;

        /**
         * GC总时间（毫秒）
         */
        private String collectionTime;

    }

}
