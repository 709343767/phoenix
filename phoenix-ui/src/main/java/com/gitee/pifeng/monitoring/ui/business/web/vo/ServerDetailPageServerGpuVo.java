package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 服务器详情页面服务器GPU信息表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2025/6/16 11:46
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "服务器详情页面服务器GPU信息表现层对象")
public class ServerDetailPageServerGpuVo implements ISuperBean {

    @Schema(description = "IP地址")
    private String ip;

    @Schema(description = "GPU序号")
    private Integer gpuNo;

    @Schema(description = "GPU名称")
    private String gpuName;

    @Schema(description = "GPU设备ID")
    private String gpuDeviceId;

    @Schema(description = "GPU供应商")
    private String gpuVendor;

    @Schema(description = "GPU版本信息")
    private String gpuVersionInfo;

    @Schema(description = "GPU显存总量（单位：byte）")
    private String gpuVramTotal;

}
