package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorLogException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * <p>
 * 异常日志表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-06-09
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "MonitorLogException对象")
public class MonitorLogExceptionVo implements ISuperBean {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "请求参数")
    private String reqParam;

    @ApiModelProperty(value = "异常名称")
    private String excName;

    @ApiModelProperty(value = "异常信息")
    private String excMessage;

    @ApiModelProperty(value = "操作用户ID")
    private Long userId;

    @ApiModelProperty(value = "操作用户名")
    private String username;

    @ApiModelProperty(value = "操作方法")
    private String operMethod;

    @ApiModelProperty(value = "请求URI")
    private String uri;

    @ApiModelProperty(value = "请求IP")
    private String ip;

    @ApiModelProperty(value = "插入时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date insertTime;

    /**
     * <p>
     * MonitorLogExceptionVo转MonitorLogException
     * </p>
     *
     * @return {@link MonitorLogException}
     * @author 皮锋
     * @custom.date 2021/6/9 22:22
     */
    public MonitorLogException convertTo() {
        MonitorLogException monitorLogException = MonitorLogException.builder().build();
        BeanUtils.copyProperties(this, monitorLogException);
        return monitorLogException;
    }

    /**
     * <p>
     * MonitorLogException转MonitorLogExceptionVo
     * </p>
     *
     * @param monitorLogException {@link MonitorLogException}
     * @return {@link MonitorLogExceptionVo}
     * @author 皮锋
     * @custom.date 2021/6/9 22:22
     */
    public MonitorLogExceptionVo convertFor(MonitorLogException monitorLogException) {
        if (null != monitorLogException) {
            BeanUtils.copyProperties(monitorLogException, this);
        }
        return this;
    }

}
