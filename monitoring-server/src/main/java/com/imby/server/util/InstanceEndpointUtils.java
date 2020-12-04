package com.imby.server.util;

import com.imby.common.constant.EndpointTypeEnums;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * 应用实例端点工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/12/4 12:42
 */
public final class InstanceEndpointUtils {

    /**
     * <p>
     * 构造方法私有化
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/12/4 12:59
     */
    private InstanceEndpointUtils() {
    }

    /**
     * <p>
     * 端点类型英文名转中文名
     * </p>
     *
     * @param instanceEndpointNameEn 端点类型英文名
     * @return 端点类型中文名
     * @author 皮锋
     * @custom.date 2020/12/4 12:55
     */
    public static String instanceEndpointNameEn2Cn(String instanceEndpointNameEn) {
        if (StringUtils.equals(instanceEndpointNameEn, EndpointTypeEnums.SERVER.getNameEn())) {
            return EndpointTypeEnums.SERVER.getNameCn();
        }
        if (StringUtils.equals(instanceEndpointNameEn, EndpointTypeEnums.AGENT.getNameEn())) {
            return EndpointTypeEnums.AGENT.getNameCn();
        }
        if (StringUtils.equals(instanceEndpointNameEn, EndpointTypeEnums.CLIENT.getNameEn())) {
            return EndpointTypeEnums.CLIENT.getNameCn();
        }
        return instanceEndpointNameEn;
    }

}
