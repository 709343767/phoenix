package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorNet;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "网络信息表现层对象")
public class MonitorNetVo implements ISuperBean {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "IP地址（来源）")
    private String ipSource;

    @ApiModelProperty(value = "IP地址（目的地）")
    private String ipTarget;

    @ApiModelProperty(value = "IP地址描述")
    private String ipDesc;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "新增时间")
    private Date insertTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "状态（0：网络不通，1：网络正常）")
    private String status;

    @ApiModelProperty(value = "平均响应时间（毫秒）")
    private Double avgTime;

    @ApiModelProperty(value = "离线次数")
    private Integer offlineCount;

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
