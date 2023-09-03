package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 服务器详情页面服务器磁盘图表信息表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/10/22 18:30
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "服务器详情页面服务器磁盘图表信息表现层对象")
public class ServerDetailPageServerDiskChartVo implements ISuperBean {

    @Schema(description = "分区的盘符名称")
    private String devName;

    @Schema(description = "分区的盘符路径")
    private String dirName;

    @Schema(description = "磁盘类型，比如 FAT32、NTFS")
    private String sysTypeName;

    @Schema(description = "磁盘总大小")
    private String totalStr;

    @Schema(description = "磁盘剩余大小")
    private String freeStr;

    @Schema(description = "磁盘已用大小")
    private String usedStr;

    @Schema(description = "磁盘可用大小")
    private String availStr;

    @Schema(description = "磁盘资源的利用率")
    private Double usePercent;

}
