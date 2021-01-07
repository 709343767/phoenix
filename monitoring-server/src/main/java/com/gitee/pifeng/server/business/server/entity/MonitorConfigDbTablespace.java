package com.gitee.pifeng.server.business.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 监控数据库表空间配置表
 * </p>
 *
 * @author 皮锋
 * @since 2021-01-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("MONITOR_CONFIG_DB_TABLESPACE")
public class MonitorConfigDbTablespace {

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private Long id;

    /**
     * 过载阈值
     */
    @ApiModelProperty(value = "过载阈值")
    private Double overloadThreshold;

    /**
     * 监控级别
     */
    @ApiModelProperty(value = "监控级别")
    private String level;

    /**
     * 插入时间
     */
    @ApiModelProperty(value = "插入时间")
    private Date insertTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}
