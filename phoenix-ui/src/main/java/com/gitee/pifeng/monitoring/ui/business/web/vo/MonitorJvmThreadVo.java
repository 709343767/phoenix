package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorJvmThread;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * java虚拟机线程信息表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/10/15 12:46
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "java虚拟机线程信息表现层对象")
public class MonitorJvmThreadVo implements ISuperBean {

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "应用实例ID")
    private String instanceId;

    @Schema(description = "当前活动线程数")
    private Integer threadCount;

    @Schema(description = "线程峰值")
    private Integer peakThreadCount;

    @Schema(description = "已创建并已启动的线程总数")
    private Integer totalStartedThreadCount;

    @Schema(description = "当前活动守护线程数")
    private Integer daemonThreadCount;

    @Schema(description = "所有线程信息")
    private List<String> threadInfoList;

    @Schema(description = "新增时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date insertTime;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * <p>
     * MonitorJvmThreadVo转MonitorJvmThread
     * </p>
     *
     * @return {@link MonitorJvmThread}
     * @author 皮锋
     * @custom.date 2020/9/3 9:20
     */
    public MonitorJvmThread convertTo() {
        MonitorJvmThread monitorJvmThread = MonitorJvmThread.builder().build();
        BeanUtils.copyProperties(this, monitorJvmThread);
        return monitorJvmThread;
    }

    /**
     * <p>
     * MonitorJvmThread转MonitorJvmThreadVo
     * </p>
     *
     * @param monitorJvmThread {@link MonitorJvmThread}
     * @return {@link MonitorJvmThreadVo}
     * @author 皮锋
     * @custom.date 2020/9/3 9:22
     */
    public MonitorJvmThreadVo convertFor(MonitorJvmThread monitorJvmThread) {
        if (null != monitorJvmThread) {
            BeanUtils.copyProperties(monitorJvmThread, this);
        }
        return this;
    }

}
