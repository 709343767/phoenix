package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorServerOs;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * <p>
 * 服务器操作系统信息表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/1/21 13:01
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "服务器操作系统信息表现层对象")
public class MonitorServerOsVo implements ISuperBean {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "IP地址")
    private String ip;

    @Schema(description = "服务器名")
    private String serverName;

    @Schema(description = "操作系统名称")
    private String osName;

    @Schema(description = "操作系统架构")
    private String osArch;

    @Schema(description = "操作系统版本")
    private String osVersion;

    @Schema(description = "用户名称")
    private String userName;

    @Schema(description = "用户主目录")
    private String userHome;

    @Schema(description = "操作系统时区")
    private String osTimeZone;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "新增时间")
    private Date insertTime;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "更新时间")
    private Date updateTime;

    /**
     * <p>
     * MonitorServerOsVo转MonitorServerOs
     * </p>
     *
     * @return {@link MonitorServerOs}
     * @author 皮锋
     * @custom.date 2020/9/3 9:20
     */
    public MonitorServerOs convertTo() {
        MonitorServerOs monitorServerOs = MonitorServerOs.builder().build();
        BeanUtils.copyProperties(this, monitorServerOs);
        return monitorServerOs;
    }

    /**
     * <p>
     * MonitorServerOs转MonitorServerOsVo
     * </p>
     *
     * @param monitorServerOs {@link MonitorServerOs}
     * @return {@link MonitorServerOsVo}
     * @author 皮锋
     * @custom.date 2020/9/3 9:22
     */
    public MonitorServerOsVo convertFor(MonitorServerOs monitorServerOs) {
        if (null != monitorServerOs) {
            BeanUtils.copyProperties(monitorServerOs, this);
        }
        return this;
    }

}
