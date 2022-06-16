package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorTcp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * <p>
 * TCP信息表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/1/10 17:21
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "TCP信息表现层对象")
public class MonitorTcpVo implements ISuperBean {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "主机名（来源）")
    private String hostnameSource;

    @ApiModelProperty(value = "主机名（目的地）")
    private String hostnameTarget;

    @ApiModelProperty(value = "端口号")
    private Integer portTarget;

    @ApiModelProperty(value = "描述")
    private String descr;

    @ApiModelProperty(value = "状态（0：不通，1：正常）")
    private String status;

    @ApiModelProperty(value = "平均响应时间（毫秒）")
    private Long avgTime;

    @ApiModelProperty(value = "离线次数")
    private Integer offlineCount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "新增时间")
    private Date insertTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "监控环境")
    private String monitorEnv;

    @ApiModelProperty(value = "监控分组")
    private String monitorGroup;

    /**
     * <p>
     * MonitorTcpVo转MonitorTcp
     * </p>
     *
     * @return {@link MonitorTcp}
     * @author 皮锋
     * @custom.date 2022/1/10 17:21
     */
    public MonitorTcp convertTo() {
        MonitorTcp monitorTcp = MonitorTcp.builder().build();
        BeanUtils.copyProperties(this, monitorTcp);
        return monitorTcp;
    }

    /**
     * <p>
     * MonitorTcp转MonitorTcpVo
     * </p>
     *
     * @param monitorTcp {@link MonitorTcp}
     * @return {@link MonitorTcpVo}
     * @author 皮锋
     * @custom.date 2022/1/10 17:21
     */
    public MonitorTcpVo convertFor(MonitorTcp monitorTcp) {
        if (null != monitorTcp) {
            BeanUtils.copyProperties(monitorTcp, this);
        }
        return this;
    }

}
