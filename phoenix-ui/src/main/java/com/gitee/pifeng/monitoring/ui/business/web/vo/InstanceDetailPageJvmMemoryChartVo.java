package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 应用实例详情页面java虚拟机内存图表信息表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/10/16 13:28
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "应用实例详情页面java虚拟机内存图表信息表现层对象")
public class InstanceDetailPageJvmMemoryChartVo implements ISuperBean {

    @Schema(description = "初始内存量（单位：Mb）")
    private String init;

    @Schema(description = "已用内存量（单位：Mb）")
    private String used;

    @Schema(description = "提交内存量（单位：Mb）")
    private String committed;

    @Schema(description = "最大内存量（单位：Mb，可能存在未定义）")
    private String max;

    @Schema(description = "新增时间")
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    private Date insertTime;

}
