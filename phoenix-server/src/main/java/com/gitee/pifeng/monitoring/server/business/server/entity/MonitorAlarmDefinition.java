package com.gitee.pifeng.monitoring.server.business.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 监控告警定义表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/4/2 10:10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("MONITOR_ALARM_DEFINITION")
public class MonitorAlarmDefinition {

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 告警类型
     */
    @TableField("TYPE")
    private String type;

    /**
     * 一级分类
     */
    @TableField("FIRST_CLASS")
    private String firstClass;

    /**
     * 二级分类
     */
    @TableField("SECOND_CLASS")
    private String secondClass;

    /**
     * 三级分类
     */
    @TableField("THIRD_CLASS")
    private String thirdClass;

    /**
     * 告警级别
     */
    @TableField("GRADE")
    private String grade;

    /**
     * 告警编码
     */
    @TableField("CODE")
    private String code;

    /**
     * 告警标题
     */
    @TableField("TITLE")
    private String title;

    /**
     * 告警内容
     */
    @TableField("CONTENT")
    private String content;

    /**
     * 插入时间
     */
    @TableField("INSERT_TIME")
    private Date insertTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;
}
