package com.imby.server.business.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * java虚拟机内存信息表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/6 19:59
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "MonitorJvmMemory对象", description = "java虚拟机内存信息表")
public class MonitorJvmMemory implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "应用实例ID")
    @TableField("INSTANCE_ID")
    private String instanceId;

    @ApiModelProperty(value = "内存类型")
    @TableField("MEMORY_TYPE")
    private String memoryType;

    @ApiModelProperty(value = "初始内存量")
    @TableField("INIT")
    private String init;

    @ApiModelProperty(value = "已用内存量")
    @TableField("USED")
    private String used;

    @ApiModelProperty(value = "提交内存量")
    @TableField("COMMITTED")
    private String committed;

    @ApiModelProperty(value = "最大内存量")
    @TableField("MAX")
    private String max;

    @ApiModelProperty(value = "新增时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
