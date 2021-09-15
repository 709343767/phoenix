package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorServerProcessHistory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * <p>
 * 服务器进程历史记录表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/9/15 14:22
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "服务器进程历史记录表现层对象")
public class MonitorServerProcessHistoryVo implements ISuperBean {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "IP地址")
    private String ip;

    @ApiModelProperty(value = "正在运行的进程数")
    private Integer processNum;

    @ApiModelProperty(value = "新增时间")
    private Date insertTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * <p>
     * MonitorServerProcessHistoryVo转MonitorServerProcessHistory
     * </p>
     *
     * @return {@link MonitorServerProcessHistory}
     * @author 皮锋
     * @custom.date 2021/9/15 14:22
     */
    public MonitorServerProcessHistory convertTo() {
        MonitorServerProcessHistory monitorServerProcessHistory = MonitorServerProcessHistory.builder().build();
        BeanUtils.copyProperties(this, monitorServerProcessHistory);
        return monitorServerProcessHistory;
    }

    /**
     * <p>
     * MonitorServerProcessHistory转MonitorServerProcessHistoryVo
     * </p>
     *
     * @param monitorServerProcessHistory {@link MonitorServerProcessHistory}
     * @return {@link MonitorServerProcessHistoryVo}
     * @author 皮锋
     * @custom.date 2021/9/15 14:22
     */
    public MonitorServerProcessHistoryVo convertFor(MonitorServerProcessHistory monitorServerProcessHistory) {
        BeanUtils.copyProperties(monitorServerProcessHistory, this);
        return this;
    }
}
