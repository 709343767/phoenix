package com.gitee.pifeng.common.util.server;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * IP地址工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/12/17 11:28
 */
public class IpAddressUtils {

    /**
     * <p>
     * 私有化构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/12/17 11:44
     */
    private IpAddressUtils() {
    }

    /**
     * <p>
     * 判断字符串是否为IP地址
     * </p>
     *
     * @param str 字符串
     * @return 是否为IP地址
     * @author 皮锋
     * @custom.date 2020/12/17 11:31
     */
    public static boolean isIpAddress(String str) {
        if (StringUtils.isBlank(str) || str.length() < 7 || str.length() > 15) {
            return false;
        }
        String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pat = Pattern.compile(rexp);
        Matcher mat = pat.matcher(str);
        return mat.find();
    }

}
