package com.gitee.pifeng.common.util;

import com.gitee.pifeng.common.constant.DateTimeStylesEnums;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
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

    public static void main(String[] args) {
        String str = DateTimeUtils.dateToString(new Date());
        log.info(str);
    }

}