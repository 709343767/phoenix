package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorJvmClassLoading;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * <p>
 * java虚拟机类加载信息表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/10/15 13:45
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "java虚拟机类加载信息表现层对象")
public class MonitorJvmClassLoadingVo implements ISuperBean {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "应用实例ID")
    private String instanceId;

    @Schema(description = "加载的类的总数")
    private Integer totalLoadedClassCount;

    @Schema(description = "当前加载的类的总数")
    private Integer loadedClassCount;

    @Schema(description = "卸载的类总数")
    private Integer unloadedClassCount;

    @Schema(description = "是否启用了类加载系统的详细输出")
    private String isVerbose;

    @Schema(description = "新增时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date insertTime;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * <p>
     * MonitorJvmClassLoadingVo转MonitorJvmClassLoading
     * </p>
     *
     * @return {@link MonitorJvmClassLoading}
     * @author 皮锋
     * @custom.date 2020/9/3 9:20
     */
    public MonitorJvmClassLoading convertTo() {
        MonitorJvmClassLoading monitorJvmClassLoading = MonitorJvmClassLoading.builder().build();
        BeanUtils.copyProperties(this, monitorJvmClassLoading);
        return monitorJvmClassLoading;
    }

    /**
     * <p>
     * MonitorJvmClassLoading转MonitorJvmClassLoadingVo
     * </p>
     *
     * @param monitorJvmClassLoading {@link MonitorJvmClassLoading}
     * @return {@link MonitorJvmClassLoadingVo}
     * @author 皮锋
     * @custom.date 2020/9/3 9:22
     */
    public MonitorJvmClassLoadingVo convertFor(MonitorJvmClassLoading monitorJvmClassLoading) {
        if (null != monitorJvmClassLoading) {
            BeanUtils.copyProperties(monitorJvmClassLoading, this);
        }
        return this;
    }

}
