package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorHttp;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * <p>
 * HTTP信息表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/4/11 10:39
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "HTTP信息表现层对象")
public class MonitorHttpVo implements ISuperBean {

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "主机名（来源）")
    private String hostnameSource;

    @Schema(description = "URL地址（目的地）")
    private String urlTarget;

    @Schema(description = "请求方法")
    private String method;

    @Schema(description = "媒体类型")
    private String contentType;

    @Schema(description = "请求头参数")
    private String headerParameter;

    @Schema(description = "请求体参数")
    private String bodyParameter;

    @Schema(description = "描述")
    private String descr;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "平均响应时间（毫秒）")
    private Long avgTime;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "是否开启监控（0：不开启监控；1：开启监控）")
    private String isEnableMonitor;

    @Schema(description = "是否开启告警（0：不开启告警；1：开启告警）")
    private String isEnableAlarm;

    @Schema(description = "异常信息")
    private String excMessage;

    @Schema(description = "结果内容")
    private String resultBody;

    @Schema(description = "结果内容大小（byte）")
    private String resultBodySize;

    @Schema(description = "离线次数")
    private Integer offlineCount;

    @Schema(description = "新增时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date insertTime;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @Schema(description = "监控环境")
    private String monitorEnv;

    @Schema(description = "监控分组")
    private String monitorGroup;

    /**
     * <p>
     * MonitorHttpVo转MonitorHttp
     * </p>
     *
     * @return {@link MonitorHttp}
     * @author 皮锋
     * @custom.date 2020/12/20 9:20
     */
    public MonitorHttp convertTo() {
        MonitorHttp monitorHttp = MonitorHttp.builder().build();
        BeanUtils.copyProperties(this, monitorHttp);
        return monitorHttp;
    }

    /**
     * <p>
     * MonitorHttp转MonitorHttpVo
     * </p>
     *
     * @param monitorHttp {@link MonitorHttp}
     * @return {@link MonitorHttpVo}
     * @author 皮锋
     * @custom.date 2020/12/20 9:22
     */
    public MonitorHttpVo convertFor(MonitorHttp monitorHttp) {
        if (null != monitorHttp) {
            BeanUtils.copyProperties(monitorHttp, this);
        }
        return this;
    }

}
