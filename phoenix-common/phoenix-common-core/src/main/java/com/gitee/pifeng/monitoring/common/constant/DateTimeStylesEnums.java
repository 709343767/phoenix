package com.gitee.pifeng.monitoring.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 日期时间样式
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/4/10 16:06
 */
@Getter
@AllArgsConstructor
public enum DateTimeStylesEnums {

    /**
     * yyyy
     */
    YYYY("yyyy", false),
    /**
     * yyyy-MM
     */
    YYYY_MM("yyyy-MM", false),
    /**
     * yyyy-MM-dd
     */
    YYYY_MM_DD("yyyy-MM-dd", false),
    /**
     * yyyy-MM-dd HH:mm
     */
    YYYY_MM_DD_HH_MM("yyyy-MM-dd HH:mm", false),
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss", false),

    /**
     * yyyyMM
     */
    YYYYMM("yyyyMM", false),
    /**
     * yyyyMMdd
     */
    YYYYMMDD("yyyyMMdd", false),
    /**
     * yyyyMMddHHmm
     */
    YYYYMMDDHHMM("yyyyMMddHHmm", false),
    /**
     * yyyyMMddHHmmss
     */
    YYYYMMDDHHMMSS("yyyyMMddHHmmss", false),

    /**
     * yyyy/MM
     */
    YYYY_MM_EN("yyyy/MM", false),
    /**
     * yyyy/MM/dd
     */
    YYYY_MM_DD_EN("yyyy/MM/dd", false),
    /**
     * yyyy/MM/dd HH:mm
     */
    YYYY_MM_DD_HH_MM_EN("yyyy/MM/dd HH:mm", false),
    /**
     * yyyy/MM/dd HH:mm:ss
     */
    YYYY_MM_DD_HH_MM_SS_EN("yyyy/MM/dd HH:mm:ss", false),

    /**
     * yyyy年
     */
    YYYY_CN("yyyy年", false),
    /**
     * yyyy年MM月
     */
    YYYY_MM_CN("yyyy年MM月", false),
    /**
     * yyyy年MM月dd日
     */
    YYYY_MM_DD_CN("yyyy年MM月dd日", false),
    /**
     * yyyy年MM月dd日 HH:mm
     */
    YYYY_MM_DD_HH_MM_CN("yyyy年MM月dd日 HH:mm", false),
    /**
     * yyyy年MM月dd日 HH:mm:ss
     */
    YYYY_MM_DD_HH_MM_SS_CN("yyyy年MM月dd日 HH:mm:ss", false),

    /**
     * HH:mm
     */
    HH_MM("HH:mm", true),
    /**
     * HH:mm:ss
     */
    HH_MM_SS("HH:mm:ss", true),

    /**
     * MM-dd
     */
    MM_DD("MM-dd", true),
    /**
     * MM-dd HH:mm
     */
    MM_DD_HH_MM("MM-dd HH:mm", true),
    /**
     * MM-dd HH:mm:ss
     */
    MM_DD_HH_MM_SS("MM-dd HH:mm:ss", true),

    /**
     * MM/dd
     */
    MM_DD_EN("MM/dd", true),
    /**
     * MM/dd HH:mm
     */
    MM_DD_HH_MM_EN("MM/dd HH:mm", true),
    /**
     * MM/dd HH:mm:ss
     */
    MM_DD_HH_MM_SS_EN("MM/dd HH:mm:ss", true),

    /**
     * MM月dd日
     */
    MM_DD_CN("MM月dd日", true),
    /**
     * MM月dd日 HH:mm
     */
    MM_DD_HH_MM_CN("MM月dd日 HH:mm", true),
    /**
     * MM月dd日 HH:mm:ss
     */
    MM_DD_HH_MM_SS_CN("MM月dd日 HH:mm:ss", true);

    private final String value;

    private final boolean isShowOnly;

}
