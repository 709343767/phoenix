package com.imby.server.business.server.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 服务器JVM信息表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/5/12 9:45
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("MONITOR_SERVER_JVM")
public class MonitorServerJvm {

    /**
     * 主键ID
     */
    @TableId("ID")
    private int id;

    /**
     * IP地址
     */
    @TableField("IP")
    private String ip;

    /**
     * java安路径
     */
    @TableField("JAVA_PATH")
    private String javaPath;

    /**
     * java运行时供应商
     */
    @TableField("JAVA_VENDOR")
    private String javaVendor;

    /**
     * java版本
     */
    @TableField("JAVA_VERSION")
    private String javaVersion;

    /**
     * java运行时名称
     */
    @TableField("JAVA_NAME")
    private String javaName;

    /**
     * java虚拟机版本
     */
    @TableField("JVM_VERSION")
    private String jvmVersion;

    /**
     * java虚拟机总内存
     */
    @TableField("JVM_TOTAL_MEMORY")
    private String jvmTotalMemory;

    /**
     * java虚拟机剩余内存
     */
    @TableField("JVM_FREE_MEMORY")
    private String jvmFreeMemory;

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

}
