package com.gitee.pifeng.monitoring.common.constant;

/**
 * <p>
 * 告警原因
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/2/1 13:02
 */
public enum AlarmReasonEnums {

    /**
     * 正常变异常
     */
    NORMAL_2_ABNORMAL,

    /**
     * 异常变正常
     */
    ABNORMAL_2_NORMAL,

    /**
     * 发现（应用程序、服务器、...）
     */
    DISCOVERY,

    /**
     * 忽略
     */
    IGNORE

}
