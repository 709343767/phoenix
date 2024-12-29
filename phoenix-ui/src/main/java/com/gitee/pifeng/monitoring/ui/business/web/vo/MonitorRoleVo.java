package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * <p>
 * 监控用户角色表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/7/14 9:12
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "监控用户角色表现层对象")
public class MonitorRoleVo implements ISuperBean {

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "角色ID")
    private Long id;

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * <p>
     * MonitorRoleVo转MonitorRole
     * </p>
     *
     * @return {@link MonitorRole}
     * @author 皮锋
     * @custom.date 2020/7/14 10:31
     */
    public MonitorRole convertTo() {
        MonitorRole monitorRole = MonitorRole.builder().build();
        BeanUtils.copyProperties(this, monitorRole);
        return monitorRole;
    }

    /**
     * <p>
     * MonitorRole转MonitorRoleVo
     * </p>
     *
     * @param monitorRole {@link MonitorRole}
     * @return {@link MonitorRoleVo}
     * @author 皮锋
     * @custom.date 2020/7/14 10:34
     */
    public MonitorRoleVo convertFor(MonitorRole monitorRole) {
        if (null != monitorRole) {
            BeanUtils.copyProperties(monitorRole, this);
        }
        return this;
    }

}
