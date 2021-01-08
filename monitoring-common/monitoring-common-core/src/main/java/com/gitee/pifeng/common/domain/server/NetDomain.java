package com.gitee.pifeng.common.domain.server;

import com.gitee.pifeng.common.abs.AbstractSuperBean;
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
    private List<NetInterfaceDomain> netList;

    @Data
    @ToString
    @NoArgsConstructor
    @Accessors(chain = true)
    @EqualsAndHashCode(callSuper = true)
    public static class NetInterfaceDomain extends AbstractSuperBean {
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
        /**
         * MAC地址
         */
        String hwAddr;
        /**
         * 网卡描述信息
         */
        String description;

        /**
         * 接收到的总字节数
         */
        long rxBytes;

        /**
         * 接收的总包数
         */
        long rxPackets;

        /**
         * 接收到的错误包数
         */
        long rxErrors;

        /**
         * 接收时丢弃的包数
         */
        long rxDropped;

        /**
         * 发送的总字节数
         */
        long txBytes;

        /**
         * 发送的总包数
         */
        long txPackets;

        /**
         * 发送时的错误包数
         */
        long txErrors;

        /**
         * 发送时丢弃的包数
         */
        long txDropped;

        /**
         * 下载速度
         */
        double downloadbps;

        /**
         * 上传速度
         */
        double uploadbps;

    }

}
