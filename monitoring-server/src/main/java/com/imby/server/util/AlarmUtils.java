package com.imby.server.util;

import com.imby.common.constant.AlarmLevelEnums;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * 告警工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/24 11:20
 */
public class AlarmUtils {

    /**
     * <p>
     * 私有化构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/9/9 8:43
     */
    private AlarmUtils() {
    }

    /**
     * <p>
     * 判断是否告警，当 “当前的告警级别” 大于等于 “配置的告警级别” 时，返回true
     * </p>
     *
     * @param configAlarmLevel 配置的告警级别
     * @param alarmLevel       当前的告警级别
     * @return boolean
     * @author 皮锋
     * @custom.date 2020/3/24 11:34
     */
    public static boolean isAlarm(String configAlarmLevel, String alarmLevel) {
        // INFO
        if (StringUtils.equalsIgnoreCase(AlarmLevelEnums.INFO.name(), configAlarmLevel)) {
            return true;
        }
        // WARN
        if (StringUtils.equalsIgnoreCase(AlarmLevelEnums.WARN.name(), configAlarmLevel)) {
            // WARN、ERROR、FATAL
            if (StringUtils.equalsIgnoreCase(AlarmLevelEnums.WARN.name(), alarmLevel)
                    || StringUtils.equalsIgnoreCase(AlarmLevelEnums.ERROR.name(), alarmLevel)
                    || StringUtils.equalsIgnoreCase(AlarmLevelEnums.FATAL.name(), alarmLevel)) {
                return true;
            }
        }
        // ERROR
        if (StringUtils.equalsIgnoreCase(AlarmLevelEnums.ERROR.name(), configAlarmLevel)) {
            // ERROR、FATAL
            if (StringUtils.equalsIgnoreCase(AlarmLevelEnums.ERROR.name(), alarmLevel)
                    || StringUtils.equalsIgnoreCase(AlarmLevelEnums.FATAL.name(), alarmLevel)) {
                return true;
            }
        }
        // FATAL
        if (StringUtils.equalsIgnoreCase(AlarmLevelEnums.FATAL.name(), configAlarmLevel)) {
            // FATAL
            return StringUtils.equalsIgnoreCase(AlarmLevelEnums.FATAL.name(), alarmLevel);
        }
        return false;
    }

    /**
     * <p>
     * 告警级别字符串转告警级别枚举
     * </p>
     *
     * @param alarmStr 告警级别字符串
     * @return 告警级别枚举
     * @author 皮锋
     * @custom.date 2020/11/19 15:00
     */
    public static AlarmLevelEnums str2Enum(String alarmStr) {
        // 消息
        if (StringUtils.equalsIgnoreCase(AlarmLevelEnums.INFO.name(), alarmStr)) {
            return AlarmLevelEnums.INFO;
        }
        // 警告
        if (StringUtils.equalsIgnoreCase(AlarmLevelEnums.WARN.name(), alarmStr)) {
            return AlarmLevelEnums.WARN;
        }
        // 错误
        if (StringUtils.equalsIgnoreCase(AlarmLevelEnums.ERROR.name(), alarmStr)) {
            return AlarmLevelEnums.ERROR;
        }
        // 致命
        if (StringUtils.equalsIgnoreCase(AlarmLevelEnums.FATAL.name(), alarmStr)) {
            return AlarmLevelEnums.FATAL;
        }
        return null;
    }

}
