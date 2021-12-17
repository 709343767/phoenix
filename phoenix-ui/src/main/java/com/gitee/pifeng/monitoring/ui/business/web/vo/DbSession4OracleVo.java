package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * Oracle数据库会话表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/12/30 13:01
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Oracle数据库会话表现层对象")
public class DbSession4OracleVo implements ISuperBean {

    @ApiModelProperty(value = "会话ID")
    private Long sid;

    @ApiModelProperty(value = "serial#")
    private Long serial;

    @ApiModelProperty(value = "用户")
    private String username;

    @ApiModelProperty(value = "模式")
    private String schemaName;

    @ApiModelProperty(value = "会话类型")
    private String type;

    @ApiModelProperty(value = "状态")
    private String state;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "登录时间")
    private Date logonTime;

    @ApiModelProperty(value = "远程主机")
    private String machine;

    @ApiModelProperty(value = "远程用户")
    private String osUser;

    @ApiModelProperty(value = "远程程序")
    private String program;

    @ApiModelProperty(value = "事件")
    private String event;

    @ApiModelProperty(value = "等待时间")
    private String waitTime;

    @ApiModelProperty(value = "SQL语句")
    private String sql;

}
