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
 * 异常日志表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-06-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("MONITOR_LOG_EXCEPTION")
public class MonitorLogException {

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 请求参数
     */
    @TableField("REQ_PARAM")
    private String reqParam;

    /**
     * 异常名称
     */
    @TableField("EXC_NAME")
    private String excName;

    /**
     * 异常信息
     */
    @TableField("EXC_MESSAGE")
    private String excMessage;

    /**
     * 操作用户ID
     */
    @TableField("USER_ID")
    private Long userId;

    /**
     * 操作用户名
     */
    @TableField("USERNAME")
    private String username;

    /**
     * 操作方法
     */
    @TableField("OPER_METHOD")
    private String operMethod;

    /**
     * 请求URI
     */
    @TableField("URI")
    private String uri;

    /**
     * 请求IP
     */
    @TableField("IP")
    private String ip;

    /**
     * 插入时间
     */
    @TableField("INSERT_TIME")
    private Date insertTime;

}
