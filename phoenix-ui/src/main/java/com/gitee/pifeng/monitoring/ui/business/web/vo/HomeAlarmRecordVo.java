package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * home页的告警记录表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/4 16:56
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "home页的告警记录表现层对象")
public class HomeAlarmRecordVo implements ISuperBean {

    @Schema(description = "告警总次数")
    private Integer alarmRecordSum;

    @Schema(description = "告警成功次数")
    private Integer alarmRecordSuccessSum;

    @Schema(description = "告警失败次数")
    private Integer alarmRecordFailSum;

    @Schema(description = "未发送告警次数")
    private Integer alarmRecordUnsentSum;

    @Schema(description = "告警成功率")
    private String alarmSucRate;

}
