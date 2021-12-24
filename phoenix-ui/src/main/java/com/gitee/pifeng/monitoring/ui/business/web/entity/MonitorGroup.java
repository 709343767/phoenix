package com.gitee.pifeng.monitoring.ui.business.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 监控分组表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-12-23
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_GROUP")
@ApiModel(value = "MonitorGroup对象", description = "监控分组表")
public class MonitorGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "分组名")
    @TableField("GROUP_NAME")
    private String groupName;

    @ApiModelProperty(value = "分组描述")
    @TableField("GROUP_DESC")
    private String groupDesc;

    @ApiModelProperty(value = "创建人账号")
    @TableField("CREATE_ACCOUNT")
    private String createAccount;

    @ApiModelProperty(value = "更新人账号")
    @TableField("UPDATE_ACCOUNT")
    private String updateAccount;

    @ApiModelProperty(value = "插入时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
