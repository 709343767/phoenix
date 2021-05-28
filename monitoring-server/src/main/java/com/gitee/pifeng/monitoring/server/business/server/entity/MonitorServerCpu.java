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
 * 服务器CPU表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/5/11 16:54
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("MONITOR_SERVER_CPU")
public class MonitorServerCpu {

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * IP地址
     */
    @TableField("IP")
    private String ip;

    /**
     * CPU序号
     */
    @TableField("CPU_NO")
    private Integer cpuNo;

    /**
     * CPU频率（MHz）
     */
    @TableField("CPU_MHZ")
    private Integer cpuMhz;

    /**
     * CPU卖主
     */
    @TableField("CPU_VENDOR")
    private String cpuVendor;

    /**
     * CPU的类别，如：Celeron
     */
    @TableField("CPU_MODEL")
    private String cpuModel;

    /**
     * CPU用户使用率
     */
    @TableField("CPU_USER")
    private Double cpuUser;

    /**
     * CPU系统使用率
     */
    @TableField("CPU_SYS")
    private Double cpuSys;

    /**
     * CPU等待率
     */
    @TableField("CPU_WAIT")
    private Double cpuWait;

    /**
     * CPU错误率
     */
    @TableField("CPU_NICE")
    private Double cpuNice;

    /**
     * CPU使用率
     */
    @TableField("CPU_COMBINED")
    private Double cpuCombined;

    /**
     * CPU剩余率
     */
    @TableField("CPU_IDLE")
    private Double cpuIdle;

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
