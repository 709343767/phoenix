package com.gitee.pifeng.monitoring.common.util;

import cn.hutool.core.io.unit.DataUnit;

import java.text.DecimalFormat;

/**
 * <p>
 * 数据大小工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/10/13 20:00
 */
public class DataSizeUtils {

    /**
     * <p>
     * 私有化构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2021/10/13 20:02
     */
    private DataSizeUtils() {
    }

    /**
     * <p>
     * 可读的数据大小
     * </p>
     *
     * @param size double类型大小
     * @return 大小
     * @author 皮锋
     * @custom.date 2021/10/13 20:01
     */
    public static String format(double size) {
        if (size <= 0) {
            return "0";
        }
        int digitGroups = Math.min(DataUnit.UNIT_NAMES.length - 1, (int) (Math.log10(size) / Math.log10(1024)));
        return new DecimalFormat("#,##0.##")
                .format(size / Math.pow(1024, digitGroups)) + " " + DataUnit.UNIT_NAMES[digitGroups];
    }

}
