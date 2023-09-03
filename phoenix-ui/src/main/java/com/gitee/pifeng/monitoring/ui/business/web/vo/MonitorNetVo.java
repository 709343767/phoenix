package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorNet;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * <p>
 * 网络信息表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/26 13:31
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "网络信息表现层对象")
public class MonitorNetVo implements ISuperBean {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "IP地址（来源）")
    private String ipSource;

    @Schema(description = "IP地址（目的地）")
    private String ipTarget;

    @Schema(description = "IP地址描述")
    private String ipDesc;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "新增时间")
    private Date insertTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "状态（0：网络不通，1：网络正常）")
    private String status;

    @Schema(description = "平均响应时间（毫秒）")
    private Double avgTime;

    @Schema(description = "ping详情")
    private String pingDetail;

    @Schema(description = "离线次数")
    private Integer offlineCount;

    @Schema(description = "监控环境")
    private String monitorEnv;

    @Schema(description = "监控分组")
    private String monitorGroup;

    /**
     * <p>
     * MonitorNetVo转MonitorNet
     * </p>
     *
     * @return {@link MonitorNet}
     * @author 皮锋
     * @custom.date 2020/9/3 9:20
     */
    public MonitorNet convertTo() {
        MonitorNet monitorNet = MonitorNet.builder().build();
        BeanUtils.copyProperties(this, monitorNet);
        return monitorNet;
    }

    /**
     * <p>
     * MonitorNet转MonitorNetVo
     * </p>
     *
     * @param monitorNet {@link MonitorNet}
     * @return {@link MonitorNetVo}
     * @author 皮锋
     * @custom.date 2020/9/3 9:22
     */
    public MonitorNetVo convertFor(MonitorNet monitorNet) {
        if (null != monitorNet) {
            BeanUtils.copyProperties(monitorNet, this);
        }
        return this;
    }

}
