package com.gitee.pifeng.monitoring.ui.business.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "MonitorLogOperation对象", description = "操作日志表")
public class MonitorLogOperation implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "功能模块")
    @TableField("OPER_MODULE")
    private String operModule;

    @ApiModelProperty(value = "操作类型")
    @TableField("OPER_TYPE")
    private String operType;

    @ApiModelProperty(value = "操作描述")
    @TableField("OPER_DESC")
    private String operDesc;

    @ApiModelProperty(value = "请求参数")
    @TableField("REQ_PARAM")
    private String reqParam;

    @ApiModelProperty(value = "返回参数")
    @TableField("RESP_PARAM")
    private String respParam;

    @ApiModelProperty(value = "操作用户ID")
    @TableField("USER_ID")
    private Long userId;

    @ApiModelProperty(value = "操作用户名")
    @TableField("USERNAME")
    private String username;

    @ApiModelProperty(value = "操作方法")
    @TableField("OPER_METHOD")
    private String operMethod;

    @ApiModelProperty(value = "请求URI")
    @TableField("URI")
    private String uri;

    @ApiModelProperty(value = "请求IP")
    @TableField("IP")
    private String ip;

    @ApiModelProperty(value = "插入时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

}
