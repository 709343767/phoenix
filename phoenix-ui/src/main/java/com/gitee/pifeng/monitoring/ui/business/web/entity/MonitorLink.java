package com.gitee.pifeng.monitoring.ui.business.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 链路表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-12-19
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_LINK")
@Schema(description = "MonitorLink对象")
public class MonitorLink implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @Schema(description = "根节点")
    @TableField("ROOT_NODE")
    private String rootNode;

    @Schema(description = "根节点时间")
    @TableField("ROOT_NODE_TIME")
    private String rootNodeTime;

    @Schema(description = "链路")
    @TableField("LINK")
    private String link;

    @Schema(description = "链路时间")
    @TableField("LINK_TIME")
    private String linkTime;

    @Schema(description = "链路类型（SERVER、INSTANCE）")
    @TableField("TYPE")
    private String type;

    @Schema(description = "新增时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @Schema(description = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
