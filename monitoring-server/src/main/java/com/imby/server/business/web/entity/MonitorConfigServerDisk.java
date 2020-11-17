package com.imby.server.business.web.entity;

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
 * 监控服务器磁盘信息配置表
 * </p>
 *
 * @author 皮锋
 * @since 2020-11-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_CONFIG_SERVER_DISK")
@ApiModel(value = "MonitorConfigServerDisk对象", description = "监控服务器磁盘信息配置表")
public class MonitorConfigServerDisk implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId("ID")
    private Long id;

    @ApiModelProperty(value = "过载阈值")
    @TableField("OVERLOAD_THRESHOLD")
    private Double overloadThreshold;

    @ApiModelProperty(value = "插入时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
