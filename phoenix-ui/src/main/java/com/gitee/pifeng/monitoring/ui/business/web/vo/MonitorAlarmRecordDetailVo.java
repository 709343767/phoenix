package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorAlarmRecordDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * <p>
 * 监控告警详情表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2025/5/7 20:48
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "监控告警详情表现层对象")
public class MonitorAlarmRecordDetailVo implements ISuperBean {

    @Schema(description = "主键ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @Schema(description = "告警记录ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long alarmRecordId;

    @Schema(description = "告警代码，使用UUID")
    private String code;

    @Schema(description = "告警方式（SMS、MAIL、...）")
    private String way;

    @Schema(description = "被告警人号码（手机号码、电子邮箱、...）")
    private String number;

    @Schema(description = "告警发送状态（0：失败；1：成功）")
    private String status;

    @Schema(description = "异常信息")
    private String excMessage;

    @Schema(description = "告警时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date insertTime;

    @Schema(description = "告警结果获取时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * <p>
     * MonitorAlarmRecordDetailVo转MonitorAlarmRecordDetail
     * </p>
     *
     * @return {@link MonitorAlarmRecordDetail}
     * @author 皮锋
     * @custom.date 2020/7/14 10:31
     */
    public MonitorAlarmRecordDetail convertTo() {
        MonitorAlarmRecordDetail monitorAlarmRecordDetail = MonitorAlarmRecordDetail.builder().build();
        BeanUtils.copyProperties(this, monitorAlarmRecordDetail);
        return monitorAlarmRecordDetail;
    }

    /**
     * <p>
     * MonitorAlarmRecordDetail转MonitorAlarmRecordDetailVo
     * </p>
     *
     * @param monitorAlarmRecordDetail {@link MonitorAlarmRecordDetail}
     * @return {@link MonitorAlarmRecordDetailVo}
     * @author 皮锋
     * @custom.date 2020/7/14 10:34
     */
    public MonitorAlarmRecordDetailVo convertFor(MonitorAlarmRecordDetail monitorAlarmRecordDetail) {
        if (null != monitorAlarmRecordDetail) {
            BeanUtils.copyProperties(monitorAlarmRecordDetail, this);
        }
        return this;
    }

}