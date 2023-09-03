package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * <p>
 * 监控分组信息表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/12/24 14:25
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "监控分组信息表现层对象")
public class MonitorGroupVo implements ISuperBean {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "分组名")
    private String groupName;

    @Schema(description = "分组描述")
    private String groupDesc;

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
     * MonitorGroupVo转MonitorGroup
     * </p>
     *
     * @return {@link MonitorGroup}
     * @author 皮锋
     * @custom.date 2020/12/20 9:20
     */
    public MonitorGroup convertTo() {
        MonitorGroup monitorGroup = MonitorGroup.builder().build();
        BeanUtils.copyProperties(this, monitorGroup);
        return monitorGroup;
    }

    /**
     * <p>
     * MonitorGroup转MonitorGroupVo
     * </p>
     *
     * @param monitorGroup {@link MonitorGroup}
     * @return {@link MonitorGroupVo}
     * @author 皮锋
     * @custom.date 2020/12/20 9:22
     */
    public MonitorGroupVo convertFor(MonitorGroup monitorGroup) {
        if (null != monitorGroup) {
            BeanUtils.copyProperties(monitorGroup, this);
        }
        return this;
    }

}
