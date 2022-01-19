package com.gitee.pifeng.monitoring.common.constant;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * <p>
 * 告警方式
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月9日 下午6:08:58
 */
public enum AlarmWayEnums {

    /**
     * 短信
     */
    SMS,

    /**
     * 邮件
     */
    MAIL;

    /**
     * <p>
     * 告警方式字符转告警方式枚举
     * </p>
     *
     * @param alarmWayStr 告警方式字符
     * @return 告警方式枚举
     * @author 皮锋
     * @custom.date 2021/1/28 19:51
     */
    public static AlarmWayEnums str2Enum(String alarmWayStr) {
        // 短信
        if (StringUtils.equalsIgnoreCase(AlarmWayEnums.SMS.name(), alarmWayStr)) {
            return AlarmWayEnums.SMS;
        }
        // 邮件
        if (StringUtils.equalsIgnoreCase(AlarmWayEnums.MAIL.name(), alarmWayStr)) {
            return AlarmWayEnums.MAIL;
        }
        return null;
    }

    /**
     * <p>
     * 告警方式字符串数组转告警方式枚举数组
     * </p>
     *
     * @param alarmWayStrs 告警方式字符串数组
     * @return 告警方式枚举数组
     * @author 皮锋
     * @custom.date 2021/1/28 14:34
     */
    public static AlarmWayEnums[] strs2Enums(String... alarmWayStrs) {
        List<AlarmWayEnums> alarmWayEnumsList = Lists.newArrayList();
        if (alarmWayStrs == null) {
            return new AlarmWayEnums[0];
        }
        for (String alarmStr : alarmWayStrs) {
            AlarmWayEnums alarmLevelEnum = str2Enum(alarmStr);
            alarmWayEnumsList.add(alarmLevelEnum);
        }
        return alarmWayEnumsList.toArray(new AlarmWayEnums[0]);
    }

    /**
     * <p>
     * 告警方式枚举数组转告警方式字符串数组
     * </p>
     *
     * @param alarmWayEnums 告警方式枚举数组
     * @return 告警方式字符串数组
     * @author 皮锋
     * @custom.date 2021/1/28 14:41
     */
    public static String[] enums2Strs(AlarmWayEnums... alarmWayEnums) {
        List<String> alarmStrList = Lists.newArrayList();
        for (AlarmWayEnums alarmWayEnum : alarmWayEnums) {
            alarmStrList.add(alarmWayEnum != null ? alarmWayEnum.name() : null);
        }
        return alarmStrList.toArray(new String[0]);
    }

}
