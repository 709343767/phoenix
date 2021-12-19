package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorLogOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * <p>
 * 操作日志表现层对象
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
@ApiModel(value = "MonitorLogOperation对象")
public class MonitorLogOperationVo implements ISuperBean {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "功能模块")
    private String operModule;

    @ApiModelProperty(value = "操作类型")
    private String operType;

    @ApiModelProperty(value = "操作描述")
    private String operDesc;

    @ApiModelProperty(value = "请求参数")
    private String reqParam;

    @ApiModelProperty(value = "返回参数")
    private String respParam;

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
     * MonitorLogOperationVo转MonitorLogOperation
     * </p>
     *
     * @return {@link MonitorLogOperation}
     * @author 皮锋
     * @custom.date 2021/6/9 22:25
     */
    public MonitorLogOperation convertTo() {
        MonitorLogOperation monitorLogOperation = MonitorLogOperation.builder().build();
        BeanUtils.copyProperties(this, monitorLogOperation);
        return monitorLogOperation;
    }

    /**
     * <p>
     * MonitorLogOperation转MonitorLogOperationVo
     * </p>
     *
     * @param monitorLogOperation {@link MonitorLogOperation}
     * @return {@link MonitorLogOperationVo}
     * @author 皮锋
     * @custom.date 2021/6/9 22:25
     */
    public MonitorLogOperationVo convertFor(MonitorLogOperation monitorLogOperation) {
        if (null != monitorLogOperation) {
            BeanUtils.copyProperties(monitorLogOperation, this);
        }
        return this;
    }

}
