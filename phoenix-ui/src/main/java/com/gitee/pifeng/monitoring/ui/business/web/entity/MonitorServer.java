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
 * 服务器表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月7日 下午5:03:49
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_SERVER")
@ApiModel(value = "MonitorServer对象", description = "服务器表")
public class MonitorServer implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "IP地址")
    @TableField("IP")
    private String ip;

    @ApiModelProperty(value = "服务器名")
    @TableField("SERVER_NAME")
    private String serverName;

    @ApiModelProperty(value = "服务器摘要")
    @TableField("SERVER_SUMMARY")
    private String serverSummary;

    @ApiModelProperty(value = "服务器状态（0：离线，1：在线）")
    @TableField("IS_ONLINE")
    private String isOnline;

    @ApiModelProperty(value = "离线次数")
    @TableField("OFFLINE_COUNT")
    private Integer offlineCount;

    @ApiModelProperty(value = "连接频率")
    @TableField("CONN_FREQUENCY")
    private Integer connFrequency;

    @ApiModelProperty(value = "新增时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
