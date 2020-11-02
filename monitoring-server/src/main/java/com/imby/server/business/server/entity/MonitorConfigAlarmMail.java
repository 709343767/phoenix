package com.imby.server.business.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 监控邮件告警配置表
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
@TableName("MONITOR_CONFIG_ALARM_MAIL")
public class MonitorConfigAlarmMail {

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 收件人邮箱地址，多个地址用英文分号隔开
     */
    @TableField("TO")
    private String to;

}
