package com.gitee.pifeng.monitoring.ui.business.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 服务器CPU表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/4 20:50
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_SERVER_CPU")
@Schema(description = "MonitorServerCpu对象")
public class MonitorServerCpu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @Schema(description = "IP地址")
    @TableField("IP")
    private String ip;

    @Schema(description = "CPU序号")
    @TableField("CPU_NO")
    private Integer cpuNo;

    @Schema(description = "CPU频率（MHz）")
    @TableField("CPU_MHZ")
    private Integer cpuMhz;

    @Schema(description = "CPU卖主")
    @TableField("CPU_VENDOR")
    private String cpuVendor;

    @Schema(description = "CPU的类别，如：Celeron")
    @TableField("CPU_MODEL")
    private String cpuModel;

    @Schema(description = "CPU用户使用率")
    @TableField("CPU_USER")
    private Double cpuUser;

    @Schema(description = "CPU系统使用率")
    @TableField("CPU_SYS")
    private Double cpuSys;

    @Schema(description = "CPU等待率")
    @TableField("CPU_WAIT")
    private Double cpuWait;

    @Schema(description = "CPU错误率")
    @TableField("CPU_NICE")
    private Double cpuNice;

    @Schema(description = "CPU使用率")
    @TableField("CPU_COMBINED")
    private Double cpuCombined;

    @Schema(description = "CPU剩余率")
    @TableField("CPU_IDLE")
    private Double cpuIdle;

    @Schema(description = "新增时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @Schema(description = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
