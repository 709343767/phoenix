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
@Schema(description = "MonitorUser对象")
public class MonitorUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @Schema(description = "账号")
    @TableField("ACCOUNT")
    private String account;

    @Schema(description = "用户名")
    @TableField("USERNAME")
    private String username;

    @Schema(description = "密码")
    @TableField("PASSWORD")
    private String password;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "角色ID")
    @TableField("ROLE_ID")
    private Long roleId;

    @Schema(description = "注册时间")
    @TableField("REGISTER_TIME")
    private Date registerTime;

    @Schema(description = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

    @Schema(description = "电子邮箱")
    @TableField("EMAIL")
    private String email;

    @Schema(description = "备注")
    @TableField("REMARKS")
    private String remarks;
}
