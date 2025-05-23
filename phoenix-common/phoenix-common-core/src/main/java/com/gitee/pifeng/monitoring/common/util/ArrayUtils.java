package com.gitee.pifeng.monitoring.common.util;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * <p>
 * 数组工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2025/5/23 09:07
 */
public class ArrayUtils {

    /**
     * <p>
     * 私有化构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2025/5/23 09:07
     */
    private ArrayUtils() {
    }

    /**
     * <p>
     * 合并多个字符串数组，并返回去重后的数组（保持插入顺序）
     * </p>
     *
     * @param arrays 多个字符串数组
     * @return 去重后的字符串数组
     * @author 皮锋
     * @custom.date 2025/5/23 09:08
     */
    public static String[] mergeAndDeduplicateStrings(String[]... arrays) {
        // 保持顺序 + 去重
        Set<String> result = Sets.newLinkedHashSet();
        for (String[] array : arrays) {
            if (array != null) {
                for (String item : array) {
                    if (item != null) {
                        result.add(item);
                    }
                }
            }
        }
        return result.toArray(new String[0]);
    }

}