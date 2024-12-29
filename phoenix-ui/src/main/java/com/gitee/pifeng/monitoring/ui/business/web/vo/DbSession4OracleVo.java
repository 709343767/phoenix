package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Oracle数据库会话表现层对象")
public class DbSession4OracleVo implements ISuperBean {

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "会话ID")
    private Long sid;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "serial#")
    private Long serial;

    @Schema(description = "用户")
    private String username;

    @Schema(description = "模式")
    private String schemaName;

    @Schema(description = "会话类型")
    private String type;

    @Schema(description = "状态")
    private String state;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "登录时间")
    private Date logonTime;

    @Schema(description = "远程主机")
    private String machine;

    @Schema(description = "远程用户")
    private String osUser;

    @Schema(description = "远程程序")
    private String program;

    @Schema(description = "事件")
    private String event;

    @Schema(description = "等待时间")
    private String waitTime;

    @Schema(description = "SQL语句")
    private String sql;

}
