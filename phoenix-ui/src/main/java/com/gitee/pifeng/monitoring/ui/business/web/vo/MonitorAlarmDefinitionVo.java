package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorAlarmDefinition;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * <p>
 * 告警定义表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/6 20:19
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "告警定义表现层对象")
public class MonitorAlarmDefinitionVo implements ISuperBean {

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "告警类型（SERVER、NET、TCP4SERVICE、HTTP4SERVICE、DOCKER、INSTANCE、DATABASE、CUSTOM）")
    private String type;

    @Schema(description = "一级分类")
    private String firstClass;

    @Schema(description = "二级分类")
    private String secondClass;

    @Schema(description = "三级分类")
    private String thirdClass;

    @Schema(description = "告警级别（INFO、WARN、ERROR、FATAL）")
    private String grade;

    @Schema(description = "告警编码")
    private String code;

    @Schema(description = "告警标题")
    private String title;

    @Schema(description = "告警内容")
    private String content;

    @Schema(description = "插入时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date insertTime;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * <p>
     * MonitorAlarmDefinitionVo转MonitorAlarmDefinition
     * </p>
     *
     * @return {@link MonitorAlarmDefinition}
     * @author 皮锋
     * @custom.date 2020/7/14 10:31
     */
    public MonitorAlarmDefinition convertTo() {
        MonitorAlarmDefinition monitorAlarmDefinition = MonitorAlarmDefinition.builder().build();
        BeanUtils.copyProperties(this, monitorAlarmDefinition);
        return monitorAlarmDefinition;
    }

    /**
     * <p>
     * MonitorAlarmDefinition转MonitorAlarmDefinitionVo
     * </p>
     *
     * @param monitorAlarmDefinition {@link MonitorAlarmDefinition}
     * @return {@link MonitorAlarmDefinitionVo}
     * @author 皮锋
     * @custom.date 2020/7/14 10:34
     */
    public MonitorAlarmDefinitionVo convertFor(MonitorAlarmDefinition monitorAlarmDefinition) {
        if (null != monitorAlarmDefinition) {
            BeanUtils.copyProperties(monitorAlarmDefinition, this);
        }
        return this;
    }
}
