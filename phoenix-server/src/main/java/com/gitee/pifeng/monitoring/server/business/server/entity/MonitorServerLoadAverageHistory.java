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
 * 服务器平均负载历史记录表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-06-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("MONITOR_SERVER_LOAD_AVERAGE_HISTORY")
public class MonitorServerLoadAverageHistory {

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * IP地址
     */
    @TableField("IP")
    private String ip;

    /**
     * 1分钟负载平均值
     */
    @TableField("ONE")
    private Double one;

    /**
     * 5分钟负载平均值
     */
    @TableField("FIVE")
    private Double five;

    /**
     * 15分钟负载平均值
     */
    @TableField("FIFTEEN")
    private Double fifteen;

    /**
     * CPU逻辑核数量
     */
    @TableField("LOGICAL_PROCESSOR_COUNT")
    private Integer logicalProcessorCount;

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
