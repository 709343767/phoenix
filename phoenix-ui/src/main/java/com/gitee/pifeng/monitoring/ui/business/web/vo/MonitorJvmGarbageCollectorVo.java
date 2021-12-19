package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorJvmGarbageCollector;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * <p>
 * java虚拟机GC信息表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/10/15 13:12
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "java虚拟机GC信息表现层对象")
public class MonitorJvmGarbageCollectorVo implements ISuperBean {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "应用实例ID")
    private String instanceId;

    @ApiModelProperty(value = "内存管理器序号")
    private Integer garbageCollectorNo;

    @ApiModelProperty(value = "内存管理器名称")
    private String garbageCollectorName;

    @ApiModelProperty(value = "GC总次数")
    private String collectionCount;

    @ApiModelProperty(value = "GC总时间（毫秒）")
    private String collectionTime;

    @ApiModelProperty(value = "新增时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date insertTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * <p>
     * MonitorJvmGarbageCollectorVo转MonitorJvmGarbageCollector
     * </p>
     *
     * @return {@link MonitorJvmGarbageCollector}
     * @author 皮锋
     * @custom.date 2020/9/3 9:20
     */
    public MonitorJvmGarbageCollector convertTo() {
        MonitorJvmGarbageCollector monitorJvmGarbageCollector = MonitorJvmGarbageCollector.builder().build();
        BeanUtils.copyProperties(this, monitorJvmGarbageCollector);
        return monitorJvmGarbageCollector;
    }

    /**
     * <p>
     * MonitorJvmGarbageCollector转MonitorJvmGarbageCollectorVo
     * </p>
     *
     * @param monitorJvmGarbageCollector {@link MonitorJvmGarbageCollector}
     * @return {@link MonitorJvmGarbageCollectorVo}
     * @author 皮锋
     * @custom.date 2020/9/3 9:22
     */
    public MonitorJvmGarbageCollectorVo convertFor(MonitorJvmGarbageCollector monitorJvmGarbageCollector) {
        if (null != monitorJvmGarbageCollector) {
            BeanUtils.copyProperties(monitorJvmGarbageCollector, this);
        }
        return this;
    }

}
