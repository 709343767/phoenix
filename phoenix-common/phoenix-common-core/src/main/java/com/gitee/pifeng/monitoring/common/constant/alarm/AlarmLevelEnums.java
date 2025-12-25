package com.gitee.pifeng.monitoring.common.constant.alarm;

import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * 告警级别
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/3 10:06
 */
public enum AlarmLevelEnums {

    /**
     * 忽略，此级别只保存告警记录，不发送告警消息
     */
    IGNORE,

    /**
     * 消息
     */
    INFO,

    /**
     * 警告
     */
    WARN,

    /**
     * 错误
     */
    ERROR,

    /**
     * 严重
     */
    FATAL;

    /**
     * <p>
     * 判断是否告警，当 “当前的告警级别” 大于等于 “配置的告警级别” 时，返回true，如果是 “忽略（IGNORE）” ，直接返回false
     * </p>
     *
     * @param configAlarmLevel 配置的告警级别
     * @param alarmLevel       当前的告警级别
     * @return boolean
     * @author 皮锋
     * @custom.date 2020/3/24 11:34
     */
    public static boolean isAlarm(AlarmLevelEnums configAlarmLevel, AlarmLevelEnums alarmLevel) {
        // IGNORE
        if (AlarmLevelEnums.IGNORE == alarmLevel) {
            // 直接忽略
            return false;
        }
        // INFO
        if (AlarmLevelEnums.INFO == configAlarmLevel) {
            return true;
        }
        // WARN
        if (AlarmLevelEnums.WARN == configAlarmLevel) {
            // WARN、ERROR、FATAL
            if (AlarmLevelEnums.WARN == alarmLevel || AlarmLevelEnums.ERROR == alarmLevel || AlarmLevelEnums.FATAL == alarmLevel) {
                return true;
            }
        }
        // ERROR
        if (AlarmLevelEnums.ERROR == configAlarmLevel) {
            // ERROR、FATAL
            if (AlarmLevelEnums.ERROR == alarmLevel || AlarmLevelEnums.FATAL == alarmLevel) {
                return true;
            }
        }
        // FATAL
        if (AlarmLevelEnums.FATAL == configAlarmLevel) {
            // FATAL
            return AlarmLevelEnums.FATAL == alarmLevel;
        }
        return false;
    }

    /**
     * <p>
     * 告警级别字符串转告警级别枚举
     * </p>
     *
     * @param alarmLevelStr 告警级别字符串
     * @return 告警级别枚举
     * @author 皮锋
     * @custom.date 2020/11/19 15:00
     */
    public static AlarmLevelEnums str2Enum(String alarmLevelStr) {
        // 忽略
        if (StringUtils.equalsIgnoreCase(AlarmLevelEnums.IGNORE.name(), alarmLevelStr)) {
            return AlarmLevelEnums.IGNORE;
        }
        // 消息
        if (StringUtils.equalsIgnoreCase(AlarmLevelEnums.INFO.name(), alarmLevelStr)) {
            return AlarmLevelEnums.INFO;
        }
        // 警告
        if (StringUtils.equalsIgnoreCase(AlarmLevelEnums.WARN.name(), alarmLevelStr)) {
            return AlarmLevelEnums.WARN;
        }
        // 错误
        if (StringUtils.equalsIgnoreCase(AlarmLevelEnums.ERROR.name(), alarmLevelStr)) {
            return AlarmLevelEnums.ERROR;
        }
        // 严重
        if (StringUtils.equalsIgnoreCase(AlarmLevelEnums.FATAL.name(), alarmLevelStr)) {
            return AlarmLevelEnums.FATAL;
        }
        return null;
    }

}
