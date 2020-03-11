package com.transfar.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 应用实例
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月6日 上午9:41:33
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public abstract class InstanceBean extends SuperBean {

    /**
     * 端点（服务端、代理端、客户端）
     */
    @ApiModelProperty(value = "端点（服务端、代理端、客户端）")
    protected String endpoint;

    /**
     * 应用实例ID
     */
    @ApiModelProperty(value = "应用实例ID")
    protected String instanceId;

    /**
     * 应用实例名
     */
    @ApiModelProperty(value = "应用实例名")
    protected String instanceName;

}
