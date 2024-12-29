package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "应用实例ID")
    private String instanceId;

    @Schema(description = "内存类型")
    private String memoryType;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "初始内存量（单位：byte）")
    private Long init;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "已用内存量（单位：byte）")
    private Long used;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "提交内存量（单位：byte）")
    private Long committed;

    @Schema(description = "最大内存量（单位：byte，可能存在未定义）")
    private String max;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "新增时间")
    private Date insertTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "更新时间")
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
