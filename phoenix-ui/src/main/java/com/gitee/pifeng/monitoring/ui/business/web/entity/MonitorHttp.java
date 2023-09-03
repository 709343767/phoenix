package com.gitee.pifeng.monitoring.ui.business.web.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * HTTP信息表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-04-11
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_HTTP")
@Schema(description = "MonitorHttp对象")
public class MonitorHttp implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @Schema(description = "主机名（来源）")
    @TableField("HOSTNAME_SOURCE")
    private String hostnameSource;

    @Schema(description = "URL地址（目的地）")
    @TableField("URL_TARGET")
    private String urlTarget;

    @Schema(description = "请求方法")
    @TableField("METHOD")
    private String method;

    @Schema(description = "请求参数")
    @TableField("PARAMETER")
    private String parameter;

    @Schema(description = "描述")
    @TableField("DESCR")
    private String descr;

    @Schema(description = "平均响应时间（毫秒）")
    @TableField("AVG_TIME")
    private Long avgTime;

    @Schema(description = "状态")
    @TableField("STATUS")
    private Integer status;

    @Schema(description = "异常信息")
    @TableField("EXC_MESSAGE")
    private String excMessage;

    @Schema(description = "离线次数")
    @TableField("OFFLINE_COUNT")
    private Integer offlineCount;

    @Schema(description = "新增时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @Schema(description = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

    @Schema(description = "监控环境")
    @TableField(value = "MONITOR_ENV", updateStrategy = FieldStrategy.IGNORED)
    private String monitorEnv;

    @Schema(description = "监控分组")
    @TableField(value = "MONITOR_GROUP", updateStrategy = FieldStrategy.IGNORED)
    private String monitorGroup;

}
