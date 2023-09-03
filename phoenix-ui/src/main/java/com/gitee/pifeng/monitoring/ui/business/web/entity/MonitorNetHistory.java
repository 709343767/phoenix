package com.gitee.pifeng.monitoring.ui.business.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 网络信息历史记录表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-03-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_NET_HISTORY")
@Schema(description = "MonitorNetHistory对象")
public class MonitorNetHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @Schema(description = "网络主表ID")
    @TableField("NET_ID")
    private Long netId;

    @Schema(description = "IP地址（来源）")
    @TableField("IP_SOURCE")
    private String ipSource;

    @Schema(description = "IP地址（目的地）")
    @TableField("IP_TARGET")
    private String ipTarget;

    @Schema(description = "IP地址描述")
    @TableField("IP_DESC")
    private String ipDesc;

    @Schema(description = "状态（0：网络不通，1：网络正常）")
    @TableField("STATUS")
    private String status;

    @Schema(description = "平均响应时间（毫秒）")
    @TableField("AVG_TIME")
    private Double avgTime;

    @Schema(description = "ping详情")
    @TableField("PING_DETAIL")
    private String pingDetail;

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
