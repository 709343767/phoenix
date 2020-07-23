package com.imby.server.business.web.vo;

import com.imby.server.business.web.entity.MonitorRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

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
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "监控用户角色表现层对象")
public class MonitorRoleVo {

    @ApiModelProperty(value = "角色ID")
    private Long id;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    /**
     * <p>
     * MonitorRoleVo转MonitorRole
     * </p>
     *
     * @return {@link MonitorRole}
     * @author 皮锋
     * @custom.date 2020/7/14 10:31
     */
    public MonitorRole convertToMonitorRole() {
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
        BeanUtils.copyProperties(monitorRole, this);
        return this;
    }

}
