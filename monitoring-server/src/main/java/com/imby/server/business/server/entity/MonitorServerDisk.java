package com.imby.server.business.server.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 服务器磁盘表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/5/12 10:13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("MONITOR_SERVER_DISK")
public class MonitorServerDisk {

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
     * 磁盘序号
     */
    @TableField("DISK_NO")
    private int diskNo;

    /**
     * 分区的盘符名称
     */
    @TableField("DEV_NAME")
    private String devName;

    /**
     * 分区的盘符路径
     */
    @TableField("DIR_NAME")
    private String dirName;

    /**
     * 磁盘类型名
     */
    @TableField("TYPE_NAME")
    private String typeName;

    /**
     * 磁盘类型
     */
    @TableField("SYS_TYPE_NAME")
    private String sysTypeName;

    /**
     * 磁盘总大小
     */
    @TableField("TOTAL")
    private String total;

    /**
     * 磁盘剩余大小
     */
    @TableField("FREE")
    private String free;

    /**
     * 磁盘已用大小
     */
    @TableField("USED")
    private String used;

    /**
     * 磁盘可用大小
     */
    @TableField("AVAIL")
    private String avail;

    /**
     * 磁盘资源的利用率
     */
    @TableField("USE_PERCENT")
    private String usePercent;

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
