package com.imby.server.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.imby.common.inf.ISuperBean;
import com.imby.server.business.web.entity.MonitorAlarmRecord;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * <p>
 * 监控告警表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/5 14:49
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "监控告警表现层对象")
public class MonitorAlarmRecordVo implements ISuperBean {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "UUID，唯一不重复，可用作主键")
    private String code;

    @ApiModelProperty(value = "告警类型（SERVER、NET、INSTANCE、CUSTOM）")
    private String type;

    @ApiModelProperty(value = "告警方式（SMS、MAIL）")
    private String way;

    @ApiModelProperty(value = "告警级别（INFO、WARM、ERROR、FATAL）")
    private String level;

    @ApiModelProperty(value = "告警时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date insertTime;

    @ApiModelProperty(value = "告警结果获取时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "告警标题")
    private String title;

    @ApiModelProperty(value = "告警内容")
    private String content;

    @ApiModelProperty(value = "短信告警发送状态（0：失败；1：成功）")
    private String smsStatus;

    @ApiModelProperty(value = "邮件告警发送状态（0：失败；1：成功）")
    private String mailStatus;

    @ApiModelProperty(value = "被告警人手机号码")
    private String phone;

    @ApiModelProperty(value = "被告警人电子邮箱")
    private String mail;

    /**
     * <p>
     * MonitorAlarmRecordVo转MonitorAlarmRecord
     * </p>
     *
     * @return {@link MonitorAlarmRecord}
     * @author 皮锋
     * @custom.date 2020/7/14 10:31
     */
    public MonitorAlarmRecord convertTo() {
        MonitorAlarmRecord monitorAlarmRecord = MonitorAlarmRecord.builder().build();
        BeanUtils.copyProperties(this, monitorAlarmRecord);
        return monitorAlarmRecord;
    }

    /**
     * <p>
     * MonitorAlarmRecord转MonitorAlarmRecordVo
     * </p>
     *
     * @param monitorAlarmRecord {@link MonitorAlarmRecord}
     * @return {@link MonitorAlarmRecordVo}
     * @author 皮锋
     * @custom.date 2020/7/14 10:34
     */
    public MonitorAlarmRecordVo convertFor(MonitorAlarmRecord monitorAlarmRecord) {
        BeanUtils.copyProperties(monitorAlarmRecord, this);
        return this;
    }

}
