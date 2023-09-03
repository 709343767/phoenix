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
 * 异常日志表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-06-09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_LOG_EXCEPTION")
@Schema(description = "MonitorLogException对象")
public class MonitorLogException implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @Schema(description = "请求参数")
    @TableField("REQ_PARAM")
    private String reqParam;

    @Schema(description = "异常名称")
    @TableField("EXC_NAME")
    private String excName;

    @Schema(description = "异常信息")
    @TableField("EXC_MESSAGE")
    private String excMessage;

    @Schema(description = "操作用户ID")
    @TableField("USER_ID")
    private Long userId;

    @Schema(description = "操作用户名")
    @TableField("USERNAME")
    private String username;

    @Schema(description = "操作方法")
    @TableField("OPER_METHOD")
    private String operMethod;

    @Schema(description = "请求URI")
    @TableField("URI")
    private String uri;

    @Schema(description = "请求IP")
    @TableField("IP")
    private String ip;

    @Schema(description = "插入时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

}
