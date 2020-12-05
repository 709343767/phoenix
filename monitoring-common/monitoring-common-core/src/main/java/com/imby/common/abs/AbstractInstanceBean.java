package com.imby.common.abs;

import com.imby.common.constant.AppServerTypeEnums;
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
public abstract class AbstractInstanceBean extends AbstractSuperBean {

    /**
     * 应用实例端点（服务端、代理端、客户端）
     */
    protected String instanceEndpoint;

    /**
     * 应用实例ID
     */
    protected String instanceId;

    /**
     * 应用实例名
     */
    protected String instanceName;

    /**
     * 应用实例描述
     */
    protected String instanceDesc;

    /**
     * 应用实例程序语言
     */
    protected String instanceLanguage;

    /**
     * 应用服务器类型
     */
    protected AppServerTypeEnums appServerType;

    /**
     * IP地址
     */
    protected String ip;

    /**
     * 计算机名
     */
    protected String computerName;

}
