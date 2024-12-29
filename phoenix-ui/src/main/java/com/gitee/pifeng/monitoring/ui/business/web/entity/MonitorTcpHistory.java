package com.gitee.pifeng.monitoring.ui.business.web.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * TCP信息历史记录表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-03-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_TCP_HISTORY")
@Schema(description = "MonitorTcpHistory对象")
public class MonitorTcpHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键ID")
    @TableId("ID")
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "TCP主表ID")
    @TableField("TCP_ID")
    private Long tcpId;

    @Schema(description = "主机名（来源）")
    @TableField("HOSTNAME_SOURCE")
    private String hostnameSource;

    @Schema(description = "主机名（目的地）")
    @TableField("HOSTNAME_TARGET")
    private String hostnameTarget;

    @Schema(description = "端口号")
    @TableField("PORT_TARGET")
    private Integer portTarget;

    @Schema(description = "描述")
    @TableField("DESCR")
    private String descr;

    @Schema(description = "状态（0：不通，1：正常）")
    @TableField("STATUS")
    private String status;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "平均响应时间（毫秒）")
    @TableField("AVG_TIME")
    private Long avgTime;

    @Schema(description = "离线次数")
    @TableField("OFFLINE_COUNT")
    private Integer offlineCount;

    @Schema(description = "新增时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @Schema(description = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
