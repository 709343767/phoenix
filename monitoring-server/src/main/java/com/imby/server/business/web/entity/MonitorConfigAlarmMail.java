package com.imby.server.business.web.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 监控邮件告警配置表
 * </p>
 *
 * @author 皮锋
 * @since 2020-11-04
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_CONFIG_ALARM_MAIL")
@ApiModel(value="MonitorConfigAlarmMail对象", description="监控邮件告警配置表")
public class MonitorConfigAlarmMail implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "收件人邮箱地址，多个地址用英文分号隔开")
    @TableField("EMILL")
    private String emill;

    @ApiModelProperty(value = "插入时间")
    @TableField("INSERT_TIME")
    private LocalDateTime insertTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;


}
