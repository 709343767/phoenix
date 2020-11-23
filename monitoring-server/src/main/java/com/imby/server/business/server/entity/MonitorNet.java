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
 * 网络信息表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/31 16:32
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("MONITOR_NET")
public class MonitorNet {

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * IP地址（来源）
     */
    @TableField("IP_SOURCE")
    private String ipSource;

    /**
     * IP地址（目的地）
     */
    @TableField("IP_TARGET")
    private String ipTarget;

    /**
     * IP地址（目的地）描述
     */
    @TableField("IP_TARGET_DESC")
    private String ipTargetDesc;

    /**
     * 状态（0：网络不通，1：网络正常）
     */
    @TableField("STATUS")
    private String status;

    /**
     * 是否已经断网告警（0：否，1：是）
     */
    @TableField("IS_ALARM")
    private String isAlarm;

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
