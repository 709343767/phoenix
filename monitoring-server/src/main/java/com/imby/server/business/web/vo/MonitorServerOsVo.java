package com.imby.server.business.web.vo;

import com.imby.common.inf.ISuperBean;
import com.imby.server.business.web.entity.MonitorServerOs;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * <p>
 * 服务器信息表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/3 16:47
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "服务器信息表现层对象")
public class MonitorServerOsVo implements ISuperBean {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "IP地址")
    private String ip;

    @ApiModelProperty(value = "服务器名")
    private String serverName;

    @ApiModelProperty(value = "操作系统名称")
    private String osName;

    @ApiModelProperty(value = "操作系统版本")
    private String osVersion;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "用户主目录")
    private String userHome;

    @ApiModelProperty(value = "操作系统时区")
    private String osTimeZone;

    @ApiModelProperty(value = "新增时间")
    private Date insertTime;

    @ApiModelProperty(value = "更新时间")
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
        BeanUtils.copyProperties(monitorServerOs, this);
        return this;
    }

}
