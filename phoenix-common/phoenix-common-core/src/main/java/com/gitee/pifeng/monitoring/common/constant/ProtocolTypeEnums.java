package com.gitee.pifeng.monitoring.common.constant;

import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * 协议类型
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月9日 下午6:19:20
 */
public enum ProtocolTypeEnums {

    /**
     * HTTP协议
     */
    HTTP;

    /**
     * <p>
     * 协议类型字符串转协议类型枚举
     * </p>
     *
     * @param protocolTypeStr 协议类型字符串
     * @return 协议类型枚举
     * @author 皮锋
     * @custom.date 2021/1/28 21:03
     */
    public static ProtocolTypeEnums str2Enum(String protocolTypeStr) {
        // HTTP协议
        if (StringUtils.equalsIgnoreCase(ProtocolTypeEnums.HTTP.name(), protocolTypeStr)) {
            return ProtocolTypeEnums.HTTP;
        }
        return null;
    }

}
