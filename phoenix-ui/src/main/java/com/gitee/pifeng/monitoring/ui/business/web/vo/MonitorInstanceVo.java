package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorInstance;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "应用程序信息表现层对象")
public class MonitorInstanceVo implements ISuperBean {

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "应用实例ID")
    private String instanceId;

    @Schema(description = "端点（客户端<client>、代理端<agent>、服务端<server>、UI端<ui>）")
    private String endpoint;

    @Schema(description = "应用实例名")
    private String instanceName;

    @Schema(description = "应用实例描述")
    private String instanceDesc;

    @Schema(description = "应用实例摘要")
    private String instanceSummary;

    @Schema(description = "IP地址")
    private String ip;

    @Schema(description = "新增时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date insertTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "最后心跳时间")
    private String finalHeartbeat;

    @Schema(description = "应用状态（0：离线，1：在线）")
    private String isOnline;

    @Schema(description = "是否开启监控（0：不开启监控；1：开启监控）")
    private String isEnableMonitor;

    @Schema(description = "是否开启告警（0：不开启告警；1：开启告警）")
    private String isEnableAlarm;

    @Schema(description = "是否收到离线通知（0：否，1：是）")
    private String isOfflineNotice;

    @Schema(description = "离线次数")
    private Integer offlineCount;

    @Schema(description = "连接频率")
    private Integer connFrequency;

    @Schema(description = "程序语言")
    private String language;

    @Schema(description = "应用服务器类型")
    private String appServerType;

    @Schema(description = "监控环境")
    private String monitorEnv;

    @Schema(description = "监控分组")
    private String monitorGroup;

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
        if (null != monitorInstance) {
            BeanUtils.copyProperties(monitorInstance, this);
        }
        return this;
    }

}
