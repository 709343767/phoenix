package com.gitee.pifeng.monitoring.server.business.server.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("MONITOR_TCP_HISTORY")
public class MonitorTcpHistory {

    /**
     * 主键ID
     */
    @TableId("ID")
    private Long id;

    /**
     * TCP主表ID
     */
    @TableField("TCP_ID")
    private Long tcpId;

    /**
     * 主机名（来源）
     */
    @TableField("HOSTNAME_SOURCE")
    private String hostnameSource;

    /**
     * 主机名（目的地）
     */
    @TableField("HOSTNAME_TARGET")
    private String hostnameTarget;

    /**
     * 端口号
     */
    @TableField("PORT_TARGET")
    private Integer portTarget;

    /**
     * 描述
     */
    @TableField("DESCR")
    private String descr;

    /**
     * 状态（0：不通，1：正常）
     */
    @TableField("STATUS")
    private String status;

    /**
     * 平均响应时间（毫秒）
     */
    @TableField("AVG_TIME")
    private Long avgTime;

    /**
     * 离线次数
     */
    @TableField("OFFLINE_COUNT")
    private Integer offlineCount;

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
