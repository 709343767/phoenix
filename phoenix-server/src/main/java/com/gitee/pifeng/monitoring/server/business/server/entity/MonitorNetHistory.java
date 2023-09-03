package com.gitee.pifeng.monitoring.server.business.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("MONITOR_NET_HISTORY")
public class MonitorNetHistory {

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 网络主表ID
     */
    @TableField("NET_ID")
    private Long netId;

    /**
     * IP地址（来源）
     */
    @TableField("IP_SOURCE")
    private String ipSource;

    /**
     * IP地址（目的地）
     */
    @TableField("IP_TARGET")
    private String ipTarget;

    /**
     * IP地址描述
     */
    @TableField("IP_DESC")
    private String ipDesc;

    /**
     * 状态（0：网络不通，1：网络正常）
     */
    @TableField("STATUS")
    private String status;

    /**
     * 平均响应时间（毫秒）
     */
    @TableField("AVG_TIME")
    private Double avgTime;

    /**
     * 离线次数
     */
    @TableField("OFFLINE_COUNT")
    private Integer offlineCount;

    /**
     * ping详情
     */
    @TableField("PING_DETAIL")
    private String pingDetail;

    /**
     * 新增时间
     */
    @TableField("INSERT_TIME")
    private Date insertTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
