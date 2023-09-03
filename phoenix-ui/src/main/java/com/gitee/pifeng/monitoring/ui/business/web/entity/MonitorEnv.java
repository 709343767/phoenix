package com.gitee.pifeng.monitoring.ui.business.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 监控环境表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-12-23
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_ENV")
@Schema(description = "MonitorEnv对象")
public class MonitorEnv implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @Schema(description = "环境名")
    @TableField("ENV_NAME")
    private String envName;

    @Schema(description = "环境描述")
    @TableField("ENV_DESC")
    private String envDesc;

    @Schema(description = "创建人账号")
    @TableField("CREATE_ACCOUNT")
    private String createAccount;

    @Schema(description = "更新人账号")
    @TableField("UPDATE_ACCOUNT")
    private String updateAccount;

    @Schema(description = "插入时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @Schema(description = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
