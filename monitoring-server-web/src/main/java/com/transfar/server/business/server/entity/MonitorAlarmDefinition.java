package com.transfar.server.business.server.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@Builder
@TableName("MONITOR_ALARM_DEFINITION")
public class MonitorAlarmDefinition {

    /**
     * 主键ID
     */
    @TableId("ID")
    private int id;

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
    @TableField("LEVEL")
    private String level;

    /**
     * 告警编码
     */
    @TableField("CODE")
    private String code;

    /**
     * 告警定义
     */
    @TableField("DEFINITION")
    private String definition;

}
