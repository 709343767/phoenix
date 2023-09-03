package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 服务器详情页面服务器网速图表信息表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/1/10 20:39
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "服务器详情页面服务器网速图表信息表现层对象")
public class ServerDetailPageServerNetworkSpeedChartVo implements ISuperBean {

    @Schema(description = "网卡名字")
    private String name;

    @Schema(description = "下载速度")
    private Double downloadSpeed;

    @Schema(description = "上传速度")
    private Double uploadSpeed;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "新增时间")
    private Date insertTime;

}
