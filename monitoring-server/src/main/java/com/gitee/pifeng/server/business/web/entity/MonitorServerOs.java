package com.gitee.pifeng.server.business.web.entity;

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
 * 服务器操作系统表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-01-21
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_SERVER_OS")
@ApiModel(value = "MonitorServerOs对象", description = "服务器操作系统表")
public class MonitorServerOs implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "IP地址")
    @TableField("IP")
    private String ip;

    @ApiModelProperty(value = "服务器名")
    @TableField("SERVER_NAME")
    private String serverName;

    @ApiModelProperty(value = "操作系统名称")
    @TableField("OS_NAME")
    private String osName;

    @ApiModelProperty(value = "操作系统版本")
    @TableField("OS_VERSION")
    private String osVersion;

    @ApiModelProperty(value = "用户名称")
    @TableField("USER_NAME")
    private String userName;

    @ApiModelProperty(value = "用户主目录")
    @TableField("USER_HOME")
    private String userHome;

    @ApiModelProperty(value = "操作系统时区")
    @TableField("OS_TIME_ZONE")
    private String osTimeZone;

    @ApiModelProperty(value = "新增时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
