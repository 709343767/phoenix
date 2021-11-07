package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorInstance;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * <p>
 * 应用程序信息表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/26 11:05
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "应用程序信息表现层对象")
public class MonitorInstanceVo implements ISuperBean {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "应用实例ID")
    private String instanceId;

    @ApiModelProperty(value = "端点（客户端<client>、代理端<agent>、服务端<server>、UI端<ui>）")
    private String endpoint;

    @ApiModelProperty(value = "应用实例名")
    private String instanceName;

    @ApiModelProperty(value = "应用实例描述")
    private String instanceDesc;

    @ApiModelProperty(value = "应用实例摘要")
    private String instanceSummary;

    @ApiModelProperty(value = "IP地址")
    private String ip;

    @ApiModelProperty(value = "新增时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date insertTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "应用状态（0：离线，1：在线）")
    private String isOnline;

    @ApiModelProperty(value = "离线次数")
    private Integer offlineCount;

    @ApiModelProperty(value = "连接频率")
    private Integer connFrequency;

    @ApiModelProperty(value = "程序语言")
    private String language;

    @ApiModelProperty(value = "应用服务器类型")
    private String appServerType;

    /**
     * <p>
     * MonitorInstanceVo转MonitorInstance
     * </p>
     *
     * @return {@link MonitorInstance}
     * @author 皮锋
     * @custom.date 2020/9/3 9:20
     */
    public MonitorInstance convertTo() {
        MonitorInstance monitorInstance = MonitorInstance.builder().build();
        BeanUtils.copyProperties(this, monitorInstance);
        return monitorInstance;
    }

    /**
     * <p>
     * MonitorInstance转MonitorInstanceVo
     * </p>
     *
     * @param monitorInstance {@link MonitorInstance}
     * @return {@link MonitorInstanceVo}
     * @author 皮锋
     * @custom.date 2020/9/3 9:22
     */
    public MonitorInstanceVo convertFor(MonitorInstance monitorInstance) {
        BeanUtils.copyProperties(monitorInstance, this);
        return this;
    }

}
