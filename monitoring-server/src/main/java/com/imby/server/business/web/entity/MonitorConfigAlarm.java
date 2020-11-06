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
 * 监控告警配置表
 * </p>
 *
 * @author 皮锋
 * @since 2020-11-04
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_CONFIG_ALARM")
@ApiModel(value = "MonitorConfigAlarm对象", description = "监控告警配置表")
public class MonitorConfigAlarm implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "监控告警是否打开")
    @TableField("ENABLE")
    private Integer enable;

    @ApiModelProperty(value = "监控告警级别，四级：INFO < WARN < ERROR < FATAL")
    @TableField("LEVEL")
    private String level;

    @ApiModelProperty(value = "监控告警方式(MAIL、SMS、...)，多种告警方式用英文分号隔开")
    @TableField("WAY")
    private String way;

    @ApiModelProperty(value = "插入时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
