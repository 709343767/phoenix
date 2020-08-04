package com.imby.server.business.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 告警记录表
 * </p>
 *
 * @author 皮锋
 * @since 2020-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "MonitorAlarmRecord对象", description = "告警记录表")
public class MonitorAlarmRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "UUID，唯一不重复，可用作主键")
    @TableField("CODE")
    private String code;

    @ApiModelProperty(value = "告警类型（SERVER、NET、INSTANCE、CUSTOM）")
    @TableField("TYPE")
    private String type;

    @ApiModelProperty(value = "告警方式（SMS、MAIL）")
    @TableField("WAY")
    private String way;

    @ApiModelProperty(value = "告警级别（INFO、WARM、ERROR、FATAL）")
    @TableField("LEVEL")
    private String level;

    @ApiModelProperty(value = "告警时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @ApiModelProperty(value = "告警结果获取时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

    @ApiModelProperty(value = "告警标题")
    @TableField("TITLE")
    private String title;

    @ApiModelProperty(value = "告警内容")
    @TableField("CONTENT")
    private String content;

    @ApiModelProperty(value = "短信告警发送状态（0：失败；1：成功）")
    @TableField("SMS_STATUS")
    private String smsStatus;

    @ApiModelProperty(value = "邮件告警发送状态（0：失败；1：成功）")
    @TableField("MAIL_STATUS")
    private String mailStatus;

    @ApiModelProperty(value = "被告警人手机号码")
    @TableField("PHONE")
    private String phone;

    @ApiModelProperty(value = "被告警人电子邮箱")
    @TableField("MAIL")
    private String mail;


}
