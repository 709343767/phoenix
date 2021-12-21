package com.gitee.pifeng.monitoring.common.constant;

import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * 企业枚举
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月10日 下午1:23:59
 */
public enum EnterpriseEnums {

    /**
     * monitoring
     */
    MONITORING;

    /**
     * <p>
     * 企业名字符串转企业名字枚举
     * </p>
     *
     * @param enterpriseStr 企业名字符串
     * @return 企业名字枚举
     * @author 皮锋
     * @custom.date 2021/1/28 21:03
     */
    public static EnterpriseEnums str2Enum(String enterpriseStr) {
        // monitoring
        if (StringUtils.equalsIgnoreCase(EnterpriseEnums.MONITORING.name(), enterpriseStr)) {
            return EnterpriseEnums.MONITORING;
        }
        return null;
    }

}
