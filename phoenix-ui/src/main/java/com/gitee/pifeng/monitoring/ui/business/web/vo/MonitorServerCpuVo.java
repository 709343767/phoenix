package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorServerCpu;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * <p>
 * 服务器CPU信息表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/11/1 14:29
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "服务器CPU信息表现层对象")
public class MonitorServerCpuVo implements ISuperBean {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "IP地址")
    private String ip;

    @ApiModelProperty(value = "CPU序号")
    private Integer cpuNo;

    @ApiModelProperty(value = "CPU频率（MHz）")
    private Integer cpuMhz;

    @ApiModelProperty(value = "CPU卖主")
    private String cpuVendor;

    @ApiModelProperty(value = "CPU的类别，如：Celeron")
    private String cpuModel;

    @ApiModelProperty(value = "CPU用户使用率")
    private Double cpuUser;

    @ApiModelProperty(value = "CPU系统使用率")
    private Double cpuSys;

    @ApiModelProperty(value = "CPU等待率")
    private Double cpuWait;

    @ApiModelProperty(value = "CPU错误率")
    private Double cpuNice;

    @ApiModelProperty(value = "CPU使用率")
    private Double cpuCombined;

    @ApiModelProperty(value = "CPU剩余率")
    private Double cpuIdle;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "新增时间")
    private Date insertTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * <p>
     * MonitorServerCpuVo转MonitorServerCpu
     * </p>
     *
     * @return {@link MonitorServerCpu}
     * @author 皮锋
     * @custom.date 2020/9/3 9:20
     */
    public MonitorServerCpu convertTo() {
        MonitorServerCpu monitorServerCpu = MonitorServerCpu.builder().build();
        BeanUtils.copyProperties(this, monitorServerCpu);
        return monitorServerCpu;
    }

    /**
     * <p>
     * MonitorServerCpu转MonitorServerCpuVo
     * </p>
     *
     * @param monitorServerCpu {@link MonitorServerCpu}
     * @return {@link MonitorServerCpuVo}
     * @author 皮锋
     * @custom.date 2020/9/3 9:22
     */
    public MonitorServerCpuVo convertFor(MonitorServerCpu monitorServerCpu) {
        BeanUtils.copyProperties(monitorServerCpu, this);
        return this;
    }

}
