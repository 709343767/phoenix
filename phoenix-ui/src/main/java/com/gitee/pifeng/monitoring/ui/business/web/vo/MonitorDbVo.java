package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorDb;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * <p>
 * 数据库信息表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/12/19 17:23
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "数据库信息表现层对象")
public class MonitorDbVo implements ISuperBean {

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "数据库连接名")
    private String connName;

    @Schema(description = "数据库URL")
    private String url;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "数据库类型")
    private String dbType;

    @Schema(description = "驱动类")
    private String driverClass;

    @Schema(description = "描述")
    private String dbDesc;

    @Schema(description = "数据库状态（0：离线，1：在线）")
    private String isOnline;

    @Schema(description = "离线次数")
    private Integer offlineCount;

    @Schema(description = "是否开启监控（0：不开启监控；1：开启监控）")
    private String isEnableMonitor;

    @Schema(description = "是否开启告警（0：不开启告警；1：开启告警）")
    private String isEnableAlarm;

    @Schema(description = "插入时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date insertTime;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @Schema(description = "监控环境")
    private String monitorEnv;

    @Schema(description = "监控分组")
    private String monitorGroup;

    /**
     * <p>
     * MonitorDbVo转MonitorDb
     * </p>
     *
     * @return {@link MonitorDb}
     * @author 皮锋
     * @custom.date 2020/12/20 9:20
     */
    public MonitorDb convertTo() {
        MonitorDb monitorDb = MonitorDb.builder().build();
        BeanUtils.copyProperties(this, monitorDb);
        return monitorDb;
    }

    /**
     * <p>
     * MonitorDb转MonitorDbVo
     * </p>
     *
     * @param monitorDb {@link MonitorDb}
     * @return {@link MonitorDbVo}
     * @author 皮锋
     * @custom.date 2020/12/20 9:22
     */
    public MonitorDbVo convertFor(MonitorDb monitorDb) {
        if (null != monitorDb) {
            BeanUtils.copyProperties(monitorDb, this);
        }
        return this;
    }

}
