package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorServerProcessHistory;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "服务器进程历史记录表现层对象")
public class MonitorServerProcessHistoryVo implements ISuperBean {

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "IP地址")
    private String ip;

    @Schema(description = "正在运行的进程数")
    private Integer processNum;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "新增时间")
    private Date insertTime;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "更新时间")
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
        if (null != monitorServerProcessHistory) {
            BeanUtils.copyProperties(monitorServerProcessHistory, this);
        }
        return this;
    }
}
