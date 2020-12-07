package com.gitee.pifeng.server.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.pifeng.common.inf.ISuperBean;
import com.gitee.pifeng.server.business.web.entity.MonitorAlarmDefinition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "告警定义表现层对象")
public class MonitorAlarmDefinitionVo implements ISuperBean {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "告警类型（SERVER、NET、INSTANCE、CUSTOM）")
    private String type;

    @ApiModelProperty(value = "一级分类")
    private String firstClass;

    @ApiModelProperty(value = "二级分类")
    private String secondClass;

    @ApiModelProperty(value = "三级分类")
    private String thirdClass;

    @ApiModelProperty(value = "告警级别（INFO、WARN、ERROR、FATAL）")
    private String grade;

    @ApiModelProperty(value = "告警编码")
    private String code;

    @ApiModelProperty(value = "告警标题")
    private String title;

    @ApiModelProperty(value = "告警内容")
    private String content;

    @ApiModelProperty(value = "插入时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date insertTime;

    @ApiModelProperty(value = "更新时间")
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
        BeanUtils.copyProperties(monitorAlarmDefinition, this);
        return this;
    }
}
