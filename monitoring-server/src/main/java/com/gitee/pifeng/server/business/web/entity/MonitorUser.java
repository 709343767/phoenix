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
 * 监控用户表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/7/1 17:38
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_USER")
@ApiModel(value = "MonitorUser对象", description = "监控用户")
public class MonitorUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "账号")
    @TableField("ACCOUNT")
    private String account;

    @ApiModelProperty(value = "用户名")
    @TableField("USERNAME")
    private String username;

    @ApiModelProperty(value = "密码")
    @TableField("PASSWORD")
    private String password;

    @ApiModelProperty(value = "角色ID")
    @TableField("ROLE_ID")
    private Long roleId;

    @ApiModelProperty(value = "注册时间")
    @TableField("REGISTER_TIME")
    private Date registerTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

    @ApiModelProperty(value = "电子邮箱")
    @TableField("EMAIL")
    private String email;

    @ApiModelProperty(value = "备注")
    @TableField("REMARKS")
    private String remarks;
}
