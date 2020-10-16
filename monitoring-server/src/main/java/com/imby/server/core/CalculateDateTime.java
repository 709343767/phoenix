package com.imby.server.core;

import com.imby.common.constant.DateTimeStylesEnums;
import com.imby.common.util.DateTimeUtils;
import com.imby.server.constant.TimeSelectConstants;
import lombok.Data;
import org.joda.time.format.DateTimeFormat;

import java.util.Date;

/**
 * <p>
 * 计算日期时间
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/10/15 8:50
 */
@Data
public class CalculateDateTime {

    public static final String YYYY_MM_DD_HH_00_00 = "yyyy-MM-dd HH:00:00";
    public static final String YYYY_MM_DD_HH_59_59 = "yyyy-MM-dd HH:59:59";
    public static final String YYYY_MM_DD_00_00_00 = "yyyy-MM-dd 00:00:00";
    public static final String YYYY_MM_DD_23_59_59 = "yyyy-MM-dd 23:59:59";

    /**
     * 时间类型
     */
    private String timeType;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * <p>
     * 构造方法
     * </p>
     *
     * @param timeType 时间类型
     * @author 皮锋
     * @custom.date 2020/10/15 8:57
     */
    public CalculateDateTime(String timeType) {
        this.timeType = timeType;
    }

    /**
     * <p>
     * 计算日期时间
     * </p>
     *
     * @return {@link CalculateDateTime}
     * @author 皮锋
     * @custom.date 2020/10/15 8:53
     */
    public CalculateDateTime invoke() {
        switch (timeType) {
            case TimeSelectConstants.HOUR:
                startTime = DateTimeFormat.forPattern(DateTimeStylesEnums.YYYY_MM_DD_HH_MM_SS.getValue())
                        .parseDateTime(DateTimeUtils.dateToString(new Date(), YYYY_MM_DD_HH_00_00))
                        .plusMinutes(-59).toDate();
                endTime = DateTimeFormat.forPattern(DateTimeStylesEnums.YYYY_MM_DD_HH_MM_SS.getValue())
                        .parseDateTime(DateTimeUtils.dateToString(new Date(), YYYY_MM_DD_HH_59_59))
                        .toDate();
                break;
            case TimeSelectConstants.WEEK:
                startTime = DateTimeFormat.forPattern(DateTimeStylesEnums.YYYY_MM_DD_HH_MM_SS.getValue())
                        .parseDateTime(DateTimeUtils.dateToString(new Date(), YYYY_MM_DD_00_00_00))
                        .plusDays(-6).toDate();
                endTime = DateTimeFormat.forPattern(DateTimeStylesEnums.YYYY_MM_DD_HH_MM_SS.getValue())
                        .parseDateTime(DateTimeUtils.dateToString(new Date(), YYYY_MM_DD_23_59_59))
                        .toDate();
                break;
            case TimeSelectConstants.MONTH:
                startTime = DateTimeFormat.forPattern(DateTimeStylesEnums.YYYY_MM_DD_HH_MM_SS.getValue())
                        .parseDateTime(DateTimeUtils.dateToString(new Date(), YYYY_MM_DD_00_00_00))
                        .plusMonths(-1).plusDays(1).toDate();
                endTime = DateTimeFormat.forPattern(DateTimeStylesEnums.YYYY_MM_DD_HH_MM_SS.getValue())
                        .parseDateTime(DateTimeUtils.dateToString(new Date(), YYYY_MM_DD_23_59_59))
                        .toDate();
                break;
            case TimeSelectConstants.HALF_YEAR:
                startTime = DateTimeFormat.forPattern(DateTimeStylesEnums.YYYY_MM_DD_HH_MM_SS.getValue())
                        .parseDateTime(DateTimeUtils.dateToString(new Date(), YYYY_MM_DD_00_00_00))
                        .plusMonths(-6).plusDays(1).toDate();
                endTime = DateTimeFormat.forPattern(DateTimeStylesEnums.YYYY_MM_DD_HH_MM_SS.getValue())
                        .parseDateTime(DateTimeUtils.dateToString(new Date(), YYYY_MM_DD_23_59_59))
                        .toDate();
                break;
            case TimeSelectConstants.YEAR:
                startTime = DateTimeFormat.forPattern(DateTimeStylesEnums.YYYY_MM_DD_HH_MM_SS.getValue())
                        .parseDateTime(DateTimeUtils.dateToString(new Date(), YYYY_MM_DD_00_00_00))
                        .plusMonths(-12).plusDays(1).toDate();
                endTime = DateTimeFormat.forPattern(DateTimeStylesEnums.YYYY_MM_DD_HH_MM_SS.getValue())
                        .parseDateTime(DateTimeUtils.dateToString(new Date(), YYYY_MM_DD_23_59_59))
                        .toDate();
                break;
            default:
                break;
        }
        return this;
    }
}
