package com.gitee.pifeng.monitoring.ui.business.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 操作日志表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-06-09
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("MONITOR_LOG_OPERATION")
@Schema(description = "MonitorLogOperation对象")
public class MonitorLogOperation implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @Schema(description = "功能模块")
    @TableField("OPER_MODULE")
    private String operModule;

    @Schema(description = "操作类型")
    @TableField("OPER_TYPE")
    private String operType;

    @Schema(description = "操作描述")
    @TableField("OPER_DESC")
    private String operDesc;

    @Schema(description = "请求参数")
    @TableField("REQ_PARAM")
    private String reqParam;

    @Schema(description = "返回参数")
    @TableField("RESP_PARAM")
    private String respParam;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "操作用户ID")
    @TableField("USER_ID")
    private Long userId;

    @Schema(description = "操作用户名")
    @TableField("USERNAME")
    private String username;

    @Schema(description = "操作方法")
    @TableField("OPER_METHOD")
    private String operMethod;

    @Schema(description = "请求URI")
    @TableField("URI")
    private String uri;

    @Schema(description = "请求IP")
    @TableField("IP")
    private String ip;

    @Schema(description = "耗时")
    @TableField("DURATION")
    private String duration;

    @Schema(description = "插入时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

}
