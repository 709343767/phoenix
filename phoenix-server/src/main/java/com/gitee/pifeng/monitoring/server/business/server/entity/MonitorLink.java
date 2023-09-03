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
 * 链路表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/12/18 17:41
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("MONITOR_LINK")
public class MonitorLink {

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 根节点
     */
    @TableField("ROOT_NODE")
    private String rootNode;

    /**
     * 根节点时间
     */
    @TableField("ROOT_NODE_TIME")
    private String rootNodeTime;

    /**
     * 链路
     */
    @TableField("LINK")
    private String link;

    /**
     * 链路时间
     */
    @TableField("LINK_TIME")
    private String linkTime;

    /**
     * 链路类型（SERVER、INSTANCE）
     */
    @TableField("TYPE")
    private String type;

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
