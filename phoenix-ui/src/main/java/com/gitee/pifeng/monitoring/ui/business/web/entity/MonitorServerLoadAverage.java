package com.gitee.pifeng.monitoring.ui.business.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 服务器平均负载表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-06-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_SERVER_LOAD_AVERAGE")
@Schema(description = "MonitorServerLoadAverage对象")
public class MonitorServerLoadAverage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @Schema(description = "IP地址")
    @TableField("IP")
    private String ip;

    @Schema(description = "1分钟负载平均值")
    @TableField("ONE")
    private Double one;

    @Schema(description = "5分钟负载平均值")
    @TableField("FIVE")
    private Double five;

    @Schema(description = "15分钟负载平均值")
    @TableField("FIFTEEN")
    private Double fifteen;

    @Schema(description = "CPU逻辑核数量")
    @TableField("LOGICAL_PROCESSOR_COUNT")
    private Integer logicalProcessorCount;

    @Schema(description = "新增时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @Schema(description = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
