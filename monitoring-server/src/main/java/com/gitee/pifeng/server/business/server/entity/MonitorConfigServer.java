package com.gitee.pifeng.server.business.server.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 监控服务器信息配置表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020-11-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("MONITOR_CONFIG_SERVER")
public class MonitorConfigServer implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId("ID")
    private Long id;

    /**
     * 是否监控服务器
     */
    @TableField("ENABLE")
    private Integer enable;

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
