package com.gitee.pifeng.server.business.server.entity;

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
 * @custom.date 2020-11-17
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

    /**
     * 主键ID
     */
    @TableId("ID")
    private Long id;

    /**
     * 过载阈值
     */
    @TableField("OVERLOAD_THRESHOLD")
    private Double overloadThreshold;

    /**
     * 监控级别，四级：INFO &#60; WARN &#60; ERROR &#60; FATAL
     */
    @TableField("LEVEL")
    private String level;

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
