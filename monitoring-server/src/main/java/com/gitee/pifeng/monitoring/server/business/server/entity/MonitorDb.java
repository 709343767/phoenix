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
 * 数据库表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020-12-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("MONITOR_DB")
public class MonitorDb {

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 数据库连接名
     */
    @TableField("CONN_NAME")
    private String connName;

    /**
     * 数据库URL
     */
    @TableField("URL")
    private String url;

    /**
     * 用户名
     */
    @TableField("USERNAME")
    private String username;

    /**
     * 密码
     */
    @TableField("PASSWORD")
    private String password;

    /**
     * 数据库类型
     */
    @TableField("DB_TYPE")
    private String dbType;

    /**
     * 驱动类
     */
    @TableField("DRIVER_CLASS")
    private String driverClass;

    /**
     * 描述
     */
    @TableField("DB_DESC")
    private String dbDesc;

    /**
     * 数据库状态（0：离线，1：在线）
     */
    @TableField("IS_ONLINE")
    private String isOnline;

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
