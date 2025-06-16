package com.gitee.pifeng.monitoring.ui.business.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 服务器GPU表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2025-06-09
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_SERVER_GPU")
@Schema(description = "服务器GPU对象")
public class MonitorServerGpu implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @Schema(description = "IP地址")
    @TableField("IP")
    private String ip;

    @Schema(description = "GPU序号")
    @TableField("GPU_NO")
    private Integer gpuNo;

    @Schema(description = "GPU名称")
    @TableField("GPU_NAME")
    private String gpuName;

    @Schema(description = "GPU设备ID")
    @TableField("GPU_DEVICE_ID")
    private String gpuDeviceId;

    @Schema(description = "GPU供应商")
    @TableField("GPU_VENDOR")
    private String gpuVendor;

    @Schema(description = "GPU版本信息")
    @TableField("GPU_VERSION_INFO")
    private String gpuVersionInfo;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "GPU显存总量（单位：byte）")
    @TableField("GPU_VRAM_TOTAL")
    private Long gpuVramTotal;

    @Schema(description = "新增时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @Schema(description = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
