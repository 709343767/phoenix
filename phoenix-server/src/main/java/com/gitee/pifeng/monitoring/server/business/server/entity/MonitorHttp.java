package com.gitee.pifeng.monitoring.server.business.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * HTTP信息表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-04-11
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_HTTP")
public class MonitorHttp {

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;
    /**
     * 主机名（来源）
     */
    @TableField("HOSTNAME_SOURCE")
    private String hostnameSource;
    /**
     * URL地址（目的地）
     */
    @TableField("URL_TARGET")
    private String urlTarget;
    /**
     * 请求方法
     */
    @TableField("METHOD")
    private String method;
    /**
     * 请求参数
     */
    @TableField("PARAMETER")
    private String parameter;
    /**
     * 描述
     */
    @TableField("DESCR")
    private String descr;
    /**
     * 平均响应时间（毫秒）
     */
    @TableField("AVG_TIME")
    private Long avgTime;
    /**
     * 状态
     */
    @TableField("STATUS")
    private Integer status;
    /**
     * 异常信息
     */
    @TableField("EXC_MESSAGE")
    private String excMessage;
    /**
     * 离线次数
     */
    @TableField("OFFLINE_COUNT")
    private Integer offlineCount;
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

    /**
     * 监控环境
     */
    @TableField("MONITOR_ENV")
    private String monitorEnv;

    /**
     * 监控分组
     */
    @TableField("MONITOR_GROUP")
    private String monitorGroup;

}
