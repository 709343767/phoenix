package com.gitee.pifeng.monitoring.common.domain.jvm;

import com.gitee.pifeng.monitoring.common.abs.AbstractSuperBean;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 类加载信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/14 11:03
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ClassLoadingDomain extends AbstractSuperBean {

    /**
     * 加载的类的总数
     */
    private long totalLoadedClassCount;

    /**
     * 当前加载的类的总数
     */
    private int loadedClassCount;

    /**
     * 卸载的类总数
     */
    private long unloadedClassCount;

    /**
     * 是否启用了类加载系统的详细输出
     */
    private boolean isVerbose;
}
