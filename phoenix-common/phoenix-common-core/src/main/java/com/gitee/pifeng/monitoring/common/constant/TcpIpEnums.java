package com.gitee.pifeng.monitoring.common.constant;

import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * TCP/IP协议簇枚举
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/1/25 11:59
 */
public enum TcpIpEnums {

    /**
     * TCP
     */
    TCP;

    /**
     * <p>
     * TCP/IP协议簇字符串转枚举
     * </p>
     *
     * @param tcpIpStr TCP/IP协议簇字符串
     * @return TCP/IP协议簇枚举
     * @author 皮锋
     * @custom.date 2022/1/25 15:09
     */
    public static TcpIpEnums str2Enum(String tcpIpStr) {
        // TCP
        if (StringUtils.equalsIgnoreCase(TcpIpEnums.TCP.name(), tcpIpStr)) {
            return TcpIpEnums.TCP;
        }
        return null;
    }

}