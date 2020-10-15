package com.imby.common.util;

import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * 数据大小计算工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/10/14 17:33
 */
public class DataSizeUtils {

    /**
     * 私有化构造方法
     */
    private DataSizeUtils() {
    }

    /**
     * <p>
     * 解析数据大小字符串，转换为bytes大小
     * </p>
     *
     * @param text 数据大小字符串，类似于：12KB、12.2KB、5MB、5.2MB等
     * @return bytes大小
     * @author 皮锋
     * @custom.date 2020/10/14 18:01
     */
    public static double parse(String text) {
        if (StringUtils.isBlank(text)) {
            return 0D;
        }
        if (StringUtils.equals("0", text)) {
            return 0D;
        }
        // 去除单位和空格
        String tmp = StringUtils.trimToEmpty(text.substring(0, text.length() - 2));
        if (StringUtils.containsIgnoreCase(text, DataUnitEnums.KB.name())) {
            return Double.parseDouble(tmp) * 1024D;
        }
        if (StringUtils.containsIgnoreCase(text, DataUnitEnums.MB.name())) {
            return Double.parseDouble(tmp) * 1024D * 1024D;
        }
        return 0D;
    }
}

/**
 * <p>
 * 数据单位枚举类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/10/14 18:17
 */
enum DataUnitEnums {

    /**
     * KB
     */
    KB,

    /**
     * MB
     */
    MB
}
