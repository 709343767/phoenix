package com.imby.common.domain.server;

import com.imby.common.abs.AbstractSuperBean;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * CPU信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月3日 下午2:28:02
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class CpuDomain extends AbstractSuperBean {

    /**
     * cpu总数
     */
    private int cpuNum;
    /**
     * cpu信息
     */
    private List<CpuInfoDomain> cpuList;

    @Data
    @ToString
    @NoArgsConstructor
    @Accessors(chain = true)
    @EqualsAndHashCode(callSuper = true)
    public static class CpuInfoDomain extends AbstractSuperBean {

        /**
         * CPU频率（MHz）
         */
        String cpuMhz;
        /**
         * CPU剩余率
         */
        String cpuIdle;
        /**
         * CPU使用率
         */
        String cpuCombined;

    }

}
