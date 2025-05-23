package com.gitee.pifeng.monitoring.server.business.server.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * HTTP信息历史记录表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-04-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_HTTP_HISTORY")
public class MonitorHttpHistory {

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;
    /**
     * HTTP主表ID
     */
    @TableField("HTTP_ID")
    private Long httpId;
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
     * 媒体类型
     */
    @TableField("CONTENT_TYPE")
    private String contentType;
    /**
     * 请求头参数
     */
    @TableField("HEADER_PARAMETER")
    private String headerParameter;
    /**
     * 请求体参数
     */
    @TableField("BODY_PARAMETER")
    private String bodyParameter;
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
     * 结果内容
     */
    @TableField(value = "RESULT_BODY", updateStrategy = FieldStrategy.IGNORED)
    private String resultBody;

    /**
     * 结果内容大小（byte）
     */
    @TableField(value = "RESULT_BODY_SIZE", updateStrategy = FieldStrategy.IGNORED)
    private Integer resultBodySize;

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

}
