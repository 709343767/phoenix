package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 服务器详情页面服务器CPU图表信息表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/10/19 14:21
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "服务器详情页面服务器CPU图表信息表现层对象")
public class ServerDetailPageServerCpuChartVo implements ISuperBean {

    @Schema(description = "CPU用户使用率")
    private Double cpuUser;

    @Schema(description = "CPU系统使用率")
    private Double cpuSys;

    @Schema(description = "CPU等待率")
    private Double cpuWait;

    @Schema(description = "CPU错误率")
    private Double cpuNice;

    @Schema(description = "CPU使用率")
    private Double cpuCombined;

    @Schema(description = "CPU剩余率")
    private Double cpuIdle;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "新增时间")
    private Date insertTime;

}
