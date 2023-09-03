package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * MySQL数据库会话表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/12/24 16:56
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "MySQL数据库会话表现层对象")
public class DbSession4MysqlVo implements ISuperBean {

    @Schema(description = "进程ID")
    private Long id;

    @Schema(description = "用户")
    private String user;

    @Schema(description = "主机")
    private String host;

    @Schema(description = "数据库")
    private String db;

    @Schema(description = "命令")
    private String command;

    @Schema(description = "时间")
    private String time;

    @Schema(description = "状态")
    private String state;

    @Schema(description = "活动查询")
    private String info;

}
