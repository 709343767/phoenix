package com.gitee.pifeng.monitoring.ui.business.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
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
 * 异常日志表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-06-09
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_LOG_EXCEPTION")
@ApiModel(value = "MonitorLogException对象", description = "异常日志表")
public class MonitorLogException implements Serializable, ISuperBean {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "请求参数")
    @TableField("REQ_PARAM")
    private String reqParam;

    @ApiModelProperty(value = "异常名称")
    @TableField("EXC_NAME")
    private String excName;

    @ApiModelProperty(value = "异常信息")
    @TableField("EXC_MESSAGE")
    private String excMessage;

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
