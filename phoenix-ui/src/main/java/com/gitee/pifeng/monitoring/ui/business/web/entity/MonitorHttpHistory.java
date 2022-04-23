package com.gitee.pifeng.monitoring.ui.business.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * HTTP信息历史记录表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-04-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_HTTP_HISTORY")
@ApiModel(value = "MonitorHttpHistory对象", description = "HTTP信息历史记录表")
public class MonitorHttpHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "HTTP主表ID")
    @TableField("HTTP_ID")
    private Long httpId;

    @ApiModelProperty(value = "主机名（来源）")
    @TableField("HOSTNAME_SOURCE")
    private String hostnameSource;

    @ApiModelProperty(value = "URL地址（目的地）")
    @TableField("URL_TARGET")
    private String urlTarget;

    @ApiModelProperty(value = "请求方法")
    @TableField("METHOD")
    private String method;

    @ApiModelProperty(value = "请求参数")
    @TableField("PARAMETER")
    private String parameter;

    @ApiModelProperty(value = "描述")
    @TableField("DESCR")
    private String descr;

    @ApiModelProperty(value = "平均响应时间（毫秒）")
    @TableField("AVG_TIME")
    private Long avgTime;

    @ApiModelProperty(value = "状态")
    @TableField("STATUS")
    private Integer status;

    @ApiModelProperty(value = "异常信息")
    @TableField("EXC_MESSAGE")
    private String excMessage;

    @ApiModelProperty(value = "离线次数")
    @TableField("OFFLINE_COUNT")
    private Integer offlineCount;

    @ApiModelProperty(value = "新增时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
