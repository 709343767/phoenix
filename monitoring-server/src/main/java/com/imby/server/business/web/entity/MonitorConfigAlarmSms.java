package com.imby.server.business.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 监控短信告警配置表
 * </p>
 *
 * @author 皮锋
 * @since 2020-11-04
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_CONFIG_ALARM_SMS")
@ApiModel(value = "MonitorConfigAlarmSms对象", description = "监控短信告警配置表")
public class MonitorConfigAlarmSms implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "手机号码，多个号码用英文分号隔开")
    @TableField("PHONE_NUMBERS")
    private String phoneNumbers;

    @ApiModelProperty(value = "短信接口地址")
    @TableField("ADDRESS")
    private String address;

    @ApiModelProperty(value = "短信接口协议")
    @TableField("PROTOCOL")
    private String protocol;

    @ApiModelProperty(value = "短信接口企业")
    @TableField("ENTERPRISE")
    private String enterprise;

    @ApiModelProperty(value = "插入时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
