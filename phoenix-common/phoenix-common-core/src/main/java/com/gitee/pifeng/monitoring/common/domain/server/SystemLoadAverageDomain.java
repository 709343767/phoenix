package com.gitee.pifeng.monitoring.common.domain.server;

import com.gitee.pifeng.monitoring.common.abs.AbstractSuperBean;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统平均负载信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/6/17 11:20
 */
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class SystemLoadAverageDomain extends AbstractSuperBean {

    /**
     * CPU逻辑核数量
     */
    private Integer logicalProcessorCount;

    /**
     * 1分钟负载平均值
     */
    private Double oneLoadAverage;

    /**
     * 5分钟负载平均值
     */
    private Double fiveLoadAverage;

    /**
     * 15分钟负载平均值
     */
    private Double fifteenLoadAverage;

}
