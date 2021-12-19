package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorJvmThread;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;

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
@ApiModel(value = "java虚拟机线程信息表现层对象")
public class MonitorJvmThreadVo implements ISuperBean {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "应用实例ID")
    private String instanceId;

    @ApiModelProperty(value = "当前活动线程数")
    private Integer threadCount;

    @ApiModelProperty(value = "线程峰值")
    private Integer peakThreadCount;

    @ApiModelProperty(value = "已创建并已启动的线程总数")
    private Integer totalStartedThreadCount;

    @ApiModelProperty(value = "当前活动守护线程数")
    private Integer daemonThreadCount;

    @ApiModelProperty(value = "新增时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date insertTime;

    @ApiModelProperty(value = "更新时间")
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
