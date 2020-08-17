package com.imby.common.domain.jvm;

import com.imby.common.abs.AbstractSuperBean;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 线程信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/14 10:24
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ThreadDomain extends AbstractSuperBean {

    /**
     * 当前活动线程数
     */
    private int threadCount;

    /**
     * 线程峰值
     */
    private int peakThreadCount;

    /**
     * 已创建并已启动的线程总数
     */
    private long totalStartedThreadCount;

    /**
     * 当前活动守护线程数
     */
    private int daemonThreadCount;

}
