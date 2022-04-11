package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorHttp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "HTTP信息表现层对象")
public class MonitorHttpVo implements ISuperBean {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "主机名（来源）")
    private String hostnameSource;

    @ApiModelProperty(value = "URL地址（目的地）")
    private String urlTarget;

    @ApiModelProperty(value = "请求方法")
    private String method;

    @ApiModelProperty(value = "描述")
    private String descr;

    @ApiModelProperty(value = "平均响应时间（毫秒）")
    private Long avgTime;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "离线次数")
    private Integer offlineCount;

    @ApiModelProperty(value = "新增时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date insertTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

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
