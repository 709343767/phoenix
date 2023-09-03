package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * <p>
 * 监控用户表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/7/8 8:45
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "监控用户表现层对象")
public class MonitorUserVo implements ISuperBean {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "账号")
    private String account;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "角色ID")
    private Long roleId;

    @Schema(description = "角色名字")
    private String roleName;

    @Schema(description = "电子邮箱")
    private String email;

    @Schema(description = "备注")
    private String remarks;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "注册时间")
    private Date registerTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "更新时间")
    private Date updateTime;

    /**
     * <p>
     * MonitorUserVo转MonitorUser
     * </p>
     *
     * @return {@link MonitorUser}
     * @author 皮锋
     * @custom.date 2020/7/8 9:20
     */
    public MonitorUser convertTo() {
        MonitorUser monitorUser = MonitorUser.builder().build();
        BeanUtils.copyProperties(this, monitorUser);
        return monitorUser;
    }

    /**
     * <p>
     * MonitorUser转MonitorUserVo
     * </p>
     *
     * @param monitorUser {@link MonitorUser}
     * @return {@link MonitorUserVo}
     * @author 皮锋
     * @custom.date 2020/7/8 9:22
     */
    public MonitorUserVo convertFor(MonitorUser monitorUser) {
        if (null != monitorUser) {
            BeanUtils.copyProperties(monitorUser, this);
        }
        return this;
    }

}
