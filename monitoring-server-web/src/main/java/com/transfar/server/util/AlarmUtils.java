package com.transfar.server.util;

import com.transfar.common.constant.AlarmLevelEnums;
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
     * 判断是否告警
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
}
