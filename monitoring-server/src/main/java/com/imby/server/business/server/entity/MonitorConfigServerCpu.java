package com.imby.server.business.server.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 监控服务器CPU信息配置表
 * </p>
 *
 * @author 皮锋
 * @since 2020-11-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("MONITOR_CONFIG_SERVER_CPU")
public class MonitorConfigServerCpu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("ID")
    private Long id;

    @TableField("OVERLOAD_THRESHOLD")
    private Double overloadThreshold;

    @TableField("INSERT_TIME")
    private Date insertTime;

    @TableField("UPDATE_TIME")
    private Date updateTime;

}
