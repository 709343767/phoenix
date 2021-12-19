package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorServerCpuHistory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * <p>
 * 服务器CPU历史记录表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-01-24
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "服务器CPU历史记录表现层对象")
public class MonitorServerCpuHistoryVo implements ISuperBean {

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "IP地址")
    @TableField("IP")
    private String ip;

    @ApiModelProperty(value = "CPU序号")
    @TableField("CPU_NO")
    private Integer cpuNo;

    @ApiModelProperty(value = "CPU频率（MHz）")
    @TableField("CPU_MHZ")
    private Integer cpuMhz;

    @ApiModelProperty(value = "CPU卖主")
    @TableField("CPU_VENDOR")
    private String cpuVendor;

    @ApiModelProperty(value = "CPU的类别，如：Celeron")
    @TableField("CPU_MODEL")
    private String cpuModel;

    @ApiModelProperty(value = "CPU用户使用率")
    @TableField("CPU_USER")
    private Double cpuUser;

    @ApiModelProperty(value = "CPU系统使用率")
    @TableField("CPU_SYS")
    private Double cpuSys;

    @ApiModelProperty(value = "CPU等待率")
    @TableField("CPU_WAIT")
    private Double cpuWait;

    @ApiModelProperty(value = "CPU错误率")
    @TableField("CPU_NICE")
    private Double cpuNice;

    @ApiModelProperty(value = "CPU使用率")
    @TableField("CPU_COMBINED")
    private Double cpuCombined;

    @ApiModelProperty(value = "CPU剩余率")
    @TableField("CPU_IDLE")
    private Double cpuIdle;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "新增时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * <p>
     * MonitorServerCpuHistoryVo转MonitorServerCpuHistory
     * </p>
     *
     * @return {@link MonitorServerCpuHistory}
     * @author 皮锋
     * @custom.date 2020/9/3 9:20
     */
    public MonitorServerCpuHistory convertTo() {
        MonitorServerCpuHistory monitorServerCpuHistory = MonitorServerCpuHistory.builder().build();
        BeanUtils.copyProperties(this, monitorServerCpuHistory);
        return monitorServerCpuHistory;
    }

    /**
     * <p>
     * MonitorServerCpuHistory转MonitorServerCpuHistoryVo
     * </p>
     *
     * @param monitorServerCpuHistory {@link MonitorServerCpuHistory}
     * @return {@link MonitorServerCpuHistoryVo}
     * @author 皮锋
     * @custom.date 2020/9/3 9:22
     */
    public MonitorServerCpuHistoryVo convertFor(MonitorServerCpuHistory monitorServerCpuHistory) {
        if (null != monitorServerCpuHistory) {
            BeanUtils.copyProperties(monitorServerCpuHistory, this);
        }
        return this;
    }

}
