package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorServerMemory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * <p>
 * 服务器内存信息表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/1/26 9:42
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "服务器内存信息表现层对象")
public class MonitorServerMemoryVo implements ISuperBean {

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
     * MonitorServerMemoryVo转MonitorServerMemory
     * </p>
     *
     * @return {@link MonitorServerMemory}
     * @author 皮锋
     * @custom.date 2020/9/3 9:20
     */
    public MonitorServerMemory convertTo() {
        MonitorServerMemory monitorServerMemory = MonitorServerMemory.builder().build();
        BeanUtils.copyProperties(this, monitorServerMemory);
        return monitorServerMemory;
    }

    /**
     * <p>
     * MonitorServerMemory转MonitorServerMemoryVo
     * </p>
     *
     * @param monitorServerMemory {@link MonitorServerMemory}
     * @return {@link MonitorServerMemoryVo}
     * @author 皮锋
     * @custom.date 2020/9/3 9:22
     */
    public MonitorServerMemoryVo convertFor(MonitorServerMemory monitorServerMemory) {
        BeanUtils.copyProperties(monitorServerMemory, this);
        return this;
    }

}