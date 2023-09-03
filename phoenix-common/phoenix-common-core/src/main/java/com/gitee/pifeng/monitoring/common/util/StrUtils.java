package com.gitee.pifeng.monitoring.common.util;

import com.gitee.pifeng.monitoring.common.exception.MonitoringUniversalException;

/**
 * <p>
 * 字符串工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/11/21 12:41
 */
public class StrUtils {

    /**
     * <p>
     * 从字符串末尾字母起点位置点分割成数组，例如：<br>
     * 30kB 变成 ["30","kB"]<br>
     * kB 变成 ["","kB"]<br>
     * 30 变成 ["30",""]
     * </p>
     *
     * @param in 输入字符串
     * @return 输出字符串
     * @author 皮锋
     * @custom.date 2022/11/21 12:45
     */
    public static String[] splitAllEndLetter(String in) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(in)) {
            throw new MonitoringUniversalException("输入字符串不能为空！");
        }
        char[] chars = in.toCharArray();
        int index = chars.length;
        for (int i = chars.length - 1; i >= 0; i--) {
            char c = chars[i];
            boolean isLetter = Character.isLetter(c);
            if (isLetter) {
                index = i;
            } else {
                break;
            }
        }
        String[] result = new String[2];
        result[0] = org.apache.commons.lang3.StringUtils.substring(in, 0, index);
        result[1] = org.apache.commons.lang3.StringUtils.substring(in, index);
        return result;
    }

}
