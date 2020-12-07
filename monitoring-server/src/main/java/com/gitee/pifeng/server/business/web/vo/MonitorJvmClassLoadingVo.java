package com.gitee.pifeng.server.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.pifeng.common.inf.ISuperBean;
import com.gitee.pifeng.server.business.web.entity.MonitorJvmClassLoading;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "java虚拟机类加载信息表现层对象")
public class MonitorJvmClassLoadingVo implements ISuperBean {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "应用实例ID")
    private String instanceId;

    @ApiModelProperty(value = "加载的类的总数")
    private Integer totalLoadedClassCount;

    @ApiModelProperty(value = "当前加载的类的总数")
    private Integer loadedClassCount;

    @ApiModelProperty(value = "卸载的类总数")
    private Integer unloadedClassCount;

    @ApiModelProperty(value = "是否启用了类加载系统的详细输出")
    private String isVerbose;

    @ApiModelProperty(value = "新增时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date insertTime;

    @ApiModelProperty(value = "更新时间")
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
        BeanUtils.copyProperties(monitorJvmClassLoading, this);
        return this;
    }

}
