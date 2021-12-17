package com.gitee.pifeng.monitoring.ui.business.web.entity;

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
 * 告警记录表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月7日 下午5:03:49
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_ALARM_RECORD")
@ApiModel(value = "MonitorAlarmRecord对象", description = "告警记录表")
public class MonitorAlarmRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "告警代码，使用UUID")
    @TableField("CODE")
    private String code;

    @ApiModelProperty(value = "告警定义编码")
    @TableField("ALARM_DEF_CODE")
    private String alarmDefCode;

    @ApiModelProperty(value = "告警类型（SERVER、NET、INSTANCE、DATABASE、CUSTOM）")
    @TableField("TYPE")
    private String type;

    @ApiModelProperty(value = "告警方式（SMS、MAIL、...）")
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

    @ApiModelProperty(value = "告警发送状态（0：失败；1：成功）")
    @TableField("STATUS")
    private String status;

    @ApiModelProperty(value = "被告警人号码（手机号码、电子邮箱、...）")
    @TableField("NUMBER")
    private String number;

}
