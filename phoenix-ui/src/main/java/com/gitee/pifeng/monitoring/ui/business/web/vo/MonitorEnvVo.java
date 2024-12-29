package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorEnv;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * <p>
 * 监控环境信息表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-12-23
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "监控环境信息表现层对象")
public class MonitorEnvVo implements ISuperBean {

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "环境名")
    private String envName;

    @Schema(description = "环境描述")
    private String envDesc;

    @Schema(description = "创建人账号")
    private String createAccount;

    @Schema(description = "更新人账号")
    private String updateAccount;

    @Schema(description = "插入时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date insertTime;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * <p>
     * MonitorEnvVo转MonitorEnv
     * </p>
     *
     * @return {@link MonitorEnv}
     * @author 皮锋
     * @custom.date 2020/12/20 9:20
     */
    public MonitorEnv convertTo() {
        MonitorEnv monitorEnv = MonitorEnv.builder().build();
        BeanUtils.copyProperties(this, monitorEnv);
        return monitorEnv;
    }

    /**
     * <p>
     * MonitorEnv转MonitorEnvVo
     * </p>
     *
     * @param monitorEnv {@link MonitorEnv}
     * @return {@link MonitorEnvVo}
     * @author 皮锋
     * @custom.date 2020/12/20 9:22
     */
    public MonitorEnvVo convertFor(MonitorEnv monitorEnv) {
        if (null != monitorEnv) {
            BeanUtils.copyProperties(monitorEnv, this);
        }
        return this;
    }

}
