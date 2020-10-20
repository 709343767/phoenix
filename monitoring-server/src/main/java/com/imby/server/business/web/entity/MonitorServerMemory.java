package com.imby.server.business.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 服务器内存表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/4 20:50
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_SERVER_MEMORY")
@ApiModel(value = "MonitorServerMemory对象", description = "服务器内存表")
public class MonitorServerMemory implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "IP地址")
    @TableField("IP")
    private String ip;

    @ApiModelProperty(value = "物理内存总量（单位：byte）")
    @TableField("MEM_TOTAL")
    private Long memTotal;

    @ApiModelProperty(value = "物理内存使用量（单位：byte）")
    @TableField("MEM_USED")
    private Long memUsed;

    @ApiModelProperty(value = "物理内存剩余量（单位：byte）")
    @TableField("MEM_FREE")
    private Long memFree;

    @ApiModelProperty(value = "物理内存使用率")
    @TableField("MEN_USED_PERCENT")
    private Double menUsedPercent;

    @ApiModelProperty(value = "新增时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
