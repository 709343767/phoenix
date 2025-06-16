package com.gitee.pifeng.monitoring.common.domain.server;

import com.gitee.pifeng.monitoring.common.abs.AbstractSuperBean;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * GPU信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2025年6月6日 上午11:40:30
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class GpuDomain extends AbstractSuperBean {

    /**
     * gpu总数
     */
    private Integer gpuNum;
    /**
     * gpu信息
     */
    private List<GpuInfoDomain> gpuList;

    @Data
    @ToString
    @NoArgsConstructor
    @Accessors(chain = true)
    @EqualsAndHashCode(callSuper = true)
    public static class GpuInfoDomain extends AbstractSuperBean {

        /**
         * 显卡名称
         */
        private String gpuName;

        /**
         * 显卡设备ID
         */
        private String gpuDeviceId;

        /**
         * 显卡供应商
         */
        private String gpuVendor;

        /**
         * 显卡版本信息
         */
        private String gpuVersionInfo;

        /**
         * 显存总量（单位：byte）
         */
        private Long gpuVRamTotal;

    }

}