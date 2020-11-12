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
 * 应用实例表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/5/10 22:24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("MONITOR_INSTANCE")
public class MonitorInstance {

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 应用实例ID
     */
    @TableField("INSTANCE_ID")
    private String instanceId;

    /**
     * 端点（client、agent、server）
     */
    @TableField("ENDPOINT")
    private String endpoint;

    /**
     * 应用实例名
     */
    @TableField("INSTANCE_NAME")
    private String instanceName;

    /**
     * 应用实例描述
     */
    @TableField("INSTANCE_DESC")
    private String instanceDesc;

    /**
     * IP地址
     */
    @TableField("IP")
    private String ip;

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

    /**
     * 应用状态（0：离线，1：在线）
     */
    @TableField("IS_ONLINE")
    private String isOnLine;

    /**
     * 编程语言
     */
    @TableField("LANGUAGE")
    private String language;

}
