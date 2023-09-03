package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorLink;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * <p>
 * 链路表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-12-19
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "链路表现层对象")
public class MonitorLinkVo implements ISuperBean {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "根节点")
    private String rootNode;

    @Schema(description = "根节点名字")
    private String rootNodeName;

    @Schema(description = "根节点IP")
    private String rootNodeIp;

    @Schema(description = "根节点时间")
    private String rootNodeTime;

    @Schema(description = "链路")
    private String link;

    @Schema(description = "链路名字")
    private String linkName;

    @Schema(description = "链路IP")
    private String linkIp;

    @Schema(description = "链路时间")
    private String linkTime;

    @Schema(description = "链路类型（SERVER、INSTANCE）")
    private String type;

    @Schema(description = "新增时间")
    private Date insertTime;

    @Schema(description = "更新时间")
    private Date updateTime;

    /**
     * <p>
     * MonitorLinkVo转MonitorLink
     * </p>
     *
     * @return {@link MonitorLink}
     * @author 皮锋
     * @custom.date 2022/12/19 22:22
     */
    public MonitorLink convertTo() {
        MonitorLink monitorLink = MonitorLink.builder().build();
        BeanUtils.copyProperties(this, monitorLink);
        return monitorLink;
    }

    /**
     * <p>
     * MonitorLink转MonitorLinkVo
     * </p>
     *
     * @param monitorLink {@link MonitorLink}
     * @return {@link MonitorLinkVo}
     * @author 皮锋
     * @custom.date 2022/12/19 22:22
     */
    public MonitorLinkVo convertFor(MonitorLink monitorLink) {
        if (null != monitorLink) {
            BeanUtils.copyProperties(monitorLink, this);
        }
        return this;
    }

}
