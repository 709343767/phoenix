package com.imby.server.business.web.vo;

import com.imby.common.inf.ISuperBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 最近几天的告警统计信息表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/18 12:34
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "最近几天的告警统计信息表现层对象")
public class LastFewDaysAlarmRecordStatisticsVo implements ISuperBean {

    @ApiModelProperty(value = "日期")
    private String date;

    @ApiModelProperty(value = "告警总数")
    private Integer total;

    @ApiModelProperty(value = "成功总数")
    private Integer success;

    @ApiModelProperty(value = "失败总数")
    private Integer fail;

}
