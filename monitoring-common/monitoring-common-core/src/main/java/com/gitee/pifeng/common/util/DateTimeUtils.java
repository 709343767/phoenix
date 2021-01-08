package com.gitee.pifeng.common.util;

import com.gitee.pifeng.common.constant.DateTimeStylesEnums;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * <p>
 * 日期时间工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/4/10 16:03
 */
@Slf4j
public class DateTimeUtils {

    /**
     * <p>
     * 将日期时间转换为字符串格式，返回的格式为：yyyy-MM-dd HH:mm:ss
     * </p>
     *
     * @param date {@link Date}
     * @return 日期时间字符串
     * @author 皮锋
     * @custom.date 2020/4/10 16:39
     */
    public static String dateToString(Date date) {
        return dateToString(date, DateTimeStylesEnums.YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * <p>
     * 将日期时间转换为字符串格式
     * </p>
     *
     * @param date                {@link Date}
     * @param dateTimeStylesEnums 日期时间样式-{@link DateTimeStylesEnums}
     * @return 日期时间字符串
     * @author 皮锋
     * @custom.date 2020/4/10 16:23
     */
    public static String dateToString(Date date, DateTimeStylesEnums dateTimeStylesEnums) {
        return dateToString(date, dateTimeStylesEnums.getValue());
    }

    /**
     * <p>
     * 将日期时间转换为字符串格式
     * </p>
     *
     * @param date     {@link Date}
     * @param parttern 日期时间字符串格式
     * @return 日期时间字符串
     * @author 皮锋
     * @custom.date 2020/4/10 16:35
     */
    public static String dateToString(Date date, String parttern) {
        LocalDateTime localDateTime = date2LocalDateTime(date);
        return DateTimeFormatter.ofPattern(parttern).format(localDateTime);
    }

    /**
     * <p>
     * {@link Date}转{@link LocalDateTime}
     * </p>
     *
     * @param date {@link Date}
     * @return {@link LocalDateTime}
     * @author 皮锋
     * @custom.date 2020/4/10 16:28
     */
    public static LocalDateTime date2LocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDateTime();
    }

    /**
     * <p>
     * {@link LocalDateTime}转{@link Date}
     * </p>
     *
     * @param localDateTime {@link LocalDateTime}
     * @return {@link Date}
     * @author 皮锋
     * @custom.date 2021/1/8 12:07
     */
    public static Date localDateTime2Data(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        return Date.from(instant);
    }

    /**
     * <p>
     * 获取指定时间对应的毫秒数
     * </p>
     *
     * @param timeStr 时间字符串："HH:mm:ss"
     * @return 指定时间对应的毫秒数
     * @author 皮锋
     * @custom.date 2021/1/8 11:12
     */
    public static long getTimeMillis(String timeStr) {
        // 年月日
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern(DateTimeStylesEnums.YYYY_MM_DD.getValue()));
        // 年月日时分秒
        String datetimeStr = dateStr + " " + timeStr;
        LocalDateTime localDateTime = LocalDateTime.parse(datetimeStr, DateTimeFormatter.ofPattern(DateTimeStylesEnums.YYYY_MM_DD_HH_MM_SS.getValue()));
        Date date = localDateTime2Data(localDateTime);
        return date.getTime();
    }

    public static void main(String[] args) {
        String str = DateTimeUtils.dateToString(new Date());
        log.info(str);
    }

}