package com.imby.common.dto;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

import com.imby.common.abs.InstanceBean;
import com.imby.common.domain.server.ServerDomain;

/**
 * <p>
 * 服务器信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月6日 上午9:54:41
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ServerPackage extends InstanceBean {

    /**
     * ID
     */
    private String id;

    /**
     * 服务器信息
     */
    private ServerDomain serverDomain;

    /**
     * 时间
     */
    private Date dateTime;

    /**
     * 传输频率
     */
    private Long rate;

}
