package com.imby.server.business.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 监控配置表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/11/2 16:54
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("MONITOR_CONFIG")
public class MonitorConfig {

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 监控阈值
     */
    @TableField("THRESHOLD")
    private Integer threshold;

}
