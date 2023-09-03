package com.gitee.pifeng.monitoring.ui.business.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 告警定义表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月7日 下午5:03:49
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_ALARM_DEFINITION")
@Schema(description = "MonitorAlarmDefinition对象")
public class MonitorAlarmDefinition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @Schema(description = "告警类型（SERVER、NET、TCP4SERVICE、HTTP4SERVICE、DOCKER、INSTANCE、DATABASE、CUSTOM）")
    @TableField("TYPE")
    private String type;

    @Schema(description = "一级分类")
    @TableField("FIRST_CLASS")
    private String firstClass;

    @Schema(description = "二级分类")
    @TableField("SECOND_CLASS")
    private String secondClass;

    @Schema(description = "三级分类")
    @TableField("THIRD_CLASS")
    private String thirdClass;

    @Schema(description = "告警级别（INFO、WARN、ERROR、FATAL）")
    @TableField("GRADE")
    private String grade;

    @Schema(description = "告警编码")
    @TableField("CODE")
    private String code;

    @Schema(description = "告警标题")
    @TableField("TITLE")
    private String title;

    @Schema(description = "告警内容")
    @TableField("CONTENT")
    private String content;

    @Schema(description = "插入时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @Schema(description = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
