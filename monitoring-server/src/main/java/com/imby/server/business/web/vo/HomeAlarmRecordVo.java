package com.imby.server.business.web.vo;

import com.imby.common.inf.ISuperBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "home页的告警记录表现层对象")
public class HomeAlarmRecordVo implements ISuperBean {

    @ApiModelProperty(value = "告警总次数")
    private Integer alarmRecordSum;

    @ApiModelProperty(value = "告警成功次数")
    private Integer alarmRecordSuccessSum;

    @ApiModelProperty(value = "告警失败次数")
    private Integer alarmRecordFailSum;

    @ApiModelProperty(value = "告警成功率")
    private String alarmSucRate;

}
