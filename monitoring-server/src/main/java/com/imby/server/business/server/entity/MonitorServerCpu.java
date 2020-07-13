package com.imby.server.business.server.entity;

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
    @TableId("ID")
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
     * CPU频率
     */
    @TableField("CPU_MHZ")
    private String cpuMhz;

    /**
     * CPU使用率
     */
    @TableField("CPU_COMBINED")
    private String cpuCombined;

    /**
     * CPU剩余率
     */
    @TableField("CPU_IDLE")
    private String cpuIdle;

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
