package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorTcpIp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * <p>
 * TCP/IP信息表现层对象
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
@ApiModel(value = "TCP/IP信息表现层对象")
public class MonitorTcpIpVo implements ISuperBean {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "IP地址（来源）")
    private String ipSource;

    @ApiModelProperty(value = "IP地址（目的地）")
    private String ipTarget;

    @ApiModelProperty(value = "端口号")
    private Integer portTarget;

    @ApiModelProperty(value = "描述")
    private String descr;

    @ApiModelProperty(value = "协议")
    private String protocol;

    @ApiModelProperty(value = "状态（0：不通，1：正常）")
    private String status;

    @ApiModelProperty(value = "平均响应时间（毫秒）")
    private String avgTime;

    @ApiModelProperty(value = "离线次数")
    private Integer offlineCount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "新增时间")
    private Date insertTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * <p>
     * MonitorTcpIpVo转MonitorTcpIp
     * </p>
     *
     * @return {@link MonitorTcpIp}
     * @author 皮锋
     * @custom.date 2022/1/10 17:21
     */
    public MonitorTcpIp convertTo() {
        MonitorTcpIp monitorTcpIp = MonitorTcpIp.builder().build();
        BeanUtils.copyProperties(this, monitorTcpIp);
        return monitorTcpIp;
    }

    /**
     * <p>
     * MonitorTcpIp转MonitorTcpIpVo
     * </p>
     *
     * @param monitorTcpIp {@link MonitorTcpIp}
     * @return {@link MonitorTcpIpVo}
     * @author 皮锋
     * @custom.date 2022/1/10 17:21
     */
    public MonitorTcpIpVo convertFor(MonitorTcpIp monitorTcpIp) {
        if (null != monitorTcpIp) {
            BeanUtils.copyProperties(monitorTcpIp, this);
        }
        return this;
    }

}
