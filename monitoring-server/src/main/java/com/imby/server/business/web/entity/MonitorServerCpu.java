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
 * 服务器CPU表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/4 20:50
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "MonitorServerCpu对象", description = "服务器CPU表")
public class MonitorServerCpu implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "IP地址")
    @TableField("IP")
    private String ip;

    @ApiModelProperty(value = "CPU序号")
    @TableField("CPU_NO")
    private Integer cpuNo;

    @ApiModelProperty(value = "CPU频率")
    @TableField("CPU_MHZ")
    private String cpuMhz;

    @ApiModelProperty(value = "CPU使用率")
    @TableField("CPU_COMBINED")
    private String cpuCombined;

    @ApiModelProperty(value = "CPU剩余率")
    @TableField("CPU_IDLE")
    private String cpuIdle;

    @ApiModelProperty(value = "新增时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
