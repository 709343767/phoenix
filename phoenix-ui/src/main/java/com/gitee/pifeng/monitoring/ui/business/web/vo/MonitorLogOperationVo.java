package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorLogOperation;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "MonitorLogOperation对象")
public class MonitorLogOperationVo implements ISuperBean {

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "功能模块")
    private String operModule;

    @Schema(description = "操作类型")
    private String operType;

    @Schema(description = "操作描述")
    private String operDesc;

    @Schema(description = "请求参数")
    private String reqParam;

    @Schema(description = "返回参数")
    private String respParam;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "操作用户ID")
    private Long userId;

    @Schema(description = "操作用户名")
    private String username;

    @Schema(description = "操作方法")
    private String operMethod;

    @Schema(description = "请求URI")
    private String uri;

    @Schema(description = "请求IP")
    private String ip;

    @Schema(description = "插入时间")
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
