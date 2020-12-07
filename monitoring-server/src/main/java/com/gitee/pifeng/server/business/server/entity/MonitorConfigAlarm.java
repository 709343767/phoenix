package com.gitee.pifeng.server.business.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 监控告警配置表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/11/2 16:55
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("MONITOR_CONFIG_ALARM")
public class MonitorConfigAlarm {

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 监控告警是否打开
     */
    @TableField("ENABLE")
    private Integer enable;

    /**
     * 监控告警级别，四级：INFO &#60; WARN &#60; ERROR &#60; FATAL
     */
    @TableField("LEVEL")
    private String level;

    /**
     * 监控告警方式(MAIL、SMS、...)，多种告警方式用英文分号隔开
     */
    @TableField("WAY")
    private String way;

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
