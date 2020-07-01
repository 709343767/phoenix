package com.imby.common.domain.server;

import com.imby.common.abs.AbstractSuperBean;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 网卡信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月3日 下午3:28:03
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public final class NetDomain extends AbstractSuperBean {

    /**
     * 网卡总数
     */
    private int netNum;
    /**
     * 网卡信息
     */
    private List<NetInterfaceConfigDomain> netList;

    @Data
    @ToString
    @NoArgsConstructor
    @Accessors(chain = true)
    @EqualsAndHashCode(callSuper = true)
    public static class NetInterfaceConfigDomain extends AbstractSuperBean {
        /**
         * 网卡名字
         */
        String name;
        /**
         * 网卡类型
         */
        String type;
        /**
         * 网卡地址
         */
        String address;
        /**
         * 子网掩码
         */
        String mask;
        /**
         * 广播地址
         */
        String broadcast;

    }

}
