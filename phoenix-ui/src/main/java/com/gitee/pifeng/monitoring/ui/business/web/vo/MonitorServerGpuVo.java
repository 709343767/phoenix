package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorServerGpu;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * <p>
 * 服务器GPU信息表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2025/6/16 11:10
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "服务器GPU信息表现层对象")
public class MonitorServerGpuVo implements ISuperBean {

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键ID")
    private Long id;

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

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "GPU显存总量（单位：byte）")
    private Long gpuVramTotal;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "新增时间")
    private Date insertTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "更新时间")
    private Date updateTime;

    /**
     * <p>
     * MonitorServerGpuVo转MonitorServerGpu
     * </p>
     *
     * @return {@link MonitorServerGpu}
     * @author 皮锋
     * @custom.date 2025/6/16 11:20
     */
    public MonitorServerGpu convertTo() {
        MonitorServerGpu monitorServerGpu = MonitorServerGpu.builder().build();
        BeanUtils.copyProperties(this, monitorServerGpu);
        return monitorServerGpu;
    }

    /**
     * <p>
     * MonitorServerGpu转MonitorServerGpuVo
     * </p>
     *
     * @param monitorServerGpu {@link MonitorServerGpu}
     * @return {@link MonitorServerGpuVo}
     * @author 皮锋
     * @custom.date 2025/6/16 11:22
     */
    public MonitorServerGpuVo convertFor(MonitorServerGpu monitorServerGpu) {
        if (null != monitorServerGpu) {
            BeanUtils.copyProperties(monitorServerGpu, this);
        }
        return this;
    }

}