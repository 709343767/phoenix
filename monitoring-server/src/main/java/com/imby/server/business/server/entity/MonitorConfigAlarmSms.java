package com.imby.server.business.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 监控短信告警配置表
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
@TableName("MONITOR_CONFIG_ALARM_SMS")
public class MonitorConfigAlarmSms {

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 手机号码，多个号码用英文分号隔开
     */
    @TableField("PHONE_NUMBERS")
    private String phoneNumbers;

    /**
     * 短信接口地址
     */
    @TableField("ADDRESS")
    private String address;

    /**
     * 短信接口协议
     */
    @TableField("PROTOCOL")
    private String protocol;

    /**
     * 短信接口企业
     */
    @TableField("ENTERPRISE")
    private String enterprise;

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
