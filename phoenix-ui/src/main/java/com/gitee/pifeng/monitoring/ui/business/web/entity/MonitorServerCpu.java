package com.gitee.pifeng.monitoring.ui.business.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(value = "CPU频率（MHz）")
    @TableField("CPU_MHZ")
    private Integer cpuMhz;

    @ApiModelProperty(value = "CPU卖主")
    @TableField("CPU_VENDOR")
    private String cpuVendor;

    @ApiModelProperty(value = "CPU的类别，如：Celeron")
    @TableField("CPU_MODEL")
    private String cpuModel;

    @ApiModelProperty(value = "CPU用户使用率")
    @TableField("CPU_USER")
    private Double cpuUser;

    @ApiModelProperty(value = "CPU系统使用率")
    @TableField("CPU_SYS")
    private Double cpuSys;

    @ApiModelProperty(value = "CPU等待率")
    @TableField("CPU_WAIT")
    private Double cpuWait;

    @ApiModelProperty(value = "CPU错误率")
    @TableField("CPU_NICE")
    private Double cpuNice;

    @ApiModelProperty(value = "CPU使用率")
    @TableField("CPU_COMBINED")
    private Double cpuCombined;

    @ApiModelProperty(value = "CPU剩余率")
    @TableField("CPU_IDLE")
    private Double cpuIdle;

    @ApiModelProperty(value = "新增时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
