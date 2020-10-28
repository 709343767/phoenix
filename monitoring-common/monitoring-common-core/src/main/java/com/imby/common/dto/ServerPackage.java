package com.imby.common.dto;

import com.imby.common.abs.AbstractInstanceBean;
import com.imby.common.domain.Server;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 服务器信息包
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月6日 上午9:54:41
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ServerPackage extends AbstractInstanceBean {

    /**
     * ID
     */
    private String id;

    /**
     * 服务器信息
     */
    private Server server;

    /**
     * 时间
     */
    private Date dateTime;

    /**
     * 传输频率
     */
    private long rate;

}
