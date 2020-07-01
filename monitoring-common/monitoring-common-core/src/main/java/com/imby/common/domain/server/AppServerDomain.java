package com.imby.common.domain.server;

import com.imby.common.abs.AbstractSuperBean;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 应用服务器信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/3 13:43
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public final class AppServerDomain extends AbstractSuperBean {
    /**
     * 服务器IP
     */
    private String serverIp;
    /**
     * 服务器Url
     */
    private String serverUrl;
    /**
     * 服务器类型
     */
    private String serverType;
    /**
     * 服务器时间
     */
    private Date serverTime;

}
