package com.gitee.pifeng.monitoring.common.domain.server;

import com.gitee.pifeng.monitoring.common.abs.AbstractSuperBean;
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
    private Integer cpuNum;
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
        Integer cpuMhz;

        /**
         * CPU卖主
         */
        String cpuVendor;

        /**
         * CPU的类别，如：Celeron
         */
        String cpuModel;

        /**
         * CPU用户使用率
         */
        Double cpuUser;

        /**
         * CPU系统使用率
         */
        Double cpuSys;

        /**
         * CPU等待率
         */
        Double cpuWait;

        /**
         * CPU错误率
         */
        Double cpuNice;

        /**
         * CPU剩余率
         */
        Double cpuIdle;

        /**
         * CPU使用率
         */
        Double cpuCombined;

    }

}
