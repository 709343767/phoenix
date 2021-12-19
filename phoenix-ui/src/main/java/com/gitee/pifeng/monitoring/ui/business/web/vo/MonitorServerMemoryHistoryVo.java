package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorServerMemoryHistory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * <p>
 * 服务器内存历史记录表现层对象
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
@ApiModel(value = "服务器内存历史记录表现层对象")
public class MonitorServerMemoryHistoryVo implements ISuperBean {

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "IP地址")
    @TableField("IP")
    private String ip;

    @ApiModelProperty(value = "物理内存总量（单位：byte）")
    @TableField("MEM_TOTAL")
    private Long memTotal;

    @ApiModelProperty(value = "物理内存使用量（单位：byte）")
    @TableField("MEM_USED")
    private Long memUsed;

    @ApiModelProperty(value = "物理内存剩余量（单位：byte）")
    @TableField("MEM_FREE")
    private Long memFree;

    @ApiModelProperty(value = "物理内存使用率")
    @TableField("MEN_USED_PERCENT")
    private Double menUsedPercent;

    @ApiModelProperty(value = "交换区总量（单位：byte）")
    @TableField("SWAP_TOTAL")
    private Long swapTotal;

    @ApiModelProperty(value = "交换区使用量（单位：byte）")
    @TableField("SWAP_USED")
    private Long swapUsed;

    @ApiModelProperty(value = "交换区剩余量（单位：byte）")
    @TableField("SWAP_FREE")
    private Long swapFree;

    @ApiModelProperty(value = "交换区使用率")
    @TableField("SWAP_USED_PERCENT")
    private Double swapUsedPercent;

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
     * MonitorServerMemoryHistoryVo转MonitorServerMemoryHistory
     * </p>
     *
     * @return {@link MonitorServerMemoryHistory}
     * @author 皮锋
     * @custom.date 2020/9/3 9:20
     */
    public MonitorServerMemoryHistory convertTo() {
        MonitorServerMemoryHistory monitorServerMemoryHistory = MonitorServerMemoryHistory.builder().build();
        BeanUtils.copyProperties(this, monitorServerMemoryHistory);
        return monitorServerMemoryHistory;
    }

    /**
     * <p>
     * MonitorServerMemoryHistory转MonitorServerMemoryHistoryVo
     * </p>
     *
     * @param monitorServerMemoryHistory {@link MonitorServerMemoryHistory}
     * @return {@link MonitorServerMemoryHistoryVo}
     * @author 皮锋
     * @custom.date 2020/9/3 9:22
     */
    public MonitorServerMemoryHistoryVo convertFor(MonitorServerMemoryHistory monitorServerMemoryHistory) {
        if (null != monitorServerMemoryHistory) {
            BeanUtils.copyProperties(monitorServerMemoryHistory, this);
        }
        return this;
    }

}
