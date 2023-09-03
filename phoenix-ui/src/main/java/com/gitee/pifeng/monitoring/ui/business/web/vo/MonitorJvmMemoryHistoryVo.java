package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorJvmMemoryHistory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * <p>
 * java虚拟机内存历史记录表现层对象
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
@Schema(description = "java虚拟机内存历史记录表现层对象")
public class MonitorJvmMemoryHistoryVo implements ISuperBean {

    @Schema(description = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @Schema(description = "应用实例ID")
    @TableField("INSTANCE_ID")
    private String instanceId;

    @Schema(description = "内存类型")
    @TableField("MEMORY_TYPE")
    private String memoryType;

    @Schema(description = "初始内存量（单位：byte）")
    @TableField("INIT")
    private Long init;

    @Schema(description = "已用内存量（单位：byte）")
    @TableField("USED")
    private Long used;

    @Schema(description = "提交内存量（单位：byte）")
    @TableField("COMMITTED")
    private Long committed;

    @Schema(description = "最大内存量（单位：byte，可能存在未定义）")
    @TableField("MAX")
    private String max;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "新增时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * <p>
     * MonitorJvmMemoryHistoryVo转MonitorJvmMemoryHistory
     * </p>
     *
     * @return {@link MonitorJvmMemoryHistory}
     * @author 皮锋
     * @custom.date 2020/9/3 9:20
     */
    public MonitorJvmMemoryHistory convertTo() {
        MonitorJvmMemoryHistory monitorJvmMemoryHistory = MonitorJvmMemoryHistory.builder().build();
        BeanUtils.copyProperties(this, monitorJvmMemoryHistory);
        return monitorJvmMemoryHistory;
    }

    /**
     * <p>
     * MonitorJvmMemoryHistory转MonitorJvmMemoryHistoryVo
     * </p>
     *
     * @param monitorJvmMemoryHistory {@link MonitorJvmMemoryHistory}
     * @return {@link MonitorJvmMemoryHistoryVo}
     * @author 皮锋
     * @custom.date 2020/9/3 9:22
     */
    public MonitorJvmMemoryHistoryVo convertFor(MonitorJvmMemoryHistory monitorJvmMemoryHistory) {
        if (null != monitorJvmMemoryHistory) {
            BeanUtils.copyProperties(monitorJvmMemoryHistory, this);
        }
        return this;
    }

}
