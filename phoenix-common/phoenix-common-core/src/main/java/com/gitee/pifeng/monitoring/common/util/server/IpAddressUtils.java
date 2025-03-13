package com.gitee.pifeng.monitoring.common.util.server;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.util.SubnetUtils;

import java.util.Arrays;
import java.util.List;
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

    /**
     * <p>
     * 获取某个IP所在网段的所有IP地址
     * </p>
     *
     * @param ipAddress IP地址
     * @param netmask   子网掩码
     * @return IP地址列表
     * @author 皮锋
     * @custom.date 2024/11/13 16:20
     */
    public static List<String> getAllIPsInRange(String ipAddress, String netmask) {
        // 创建SubnetUtils对象，计算给定IP和子网掩码的网段信息
        SubnetUtils subnetUtils = new SubnetUtils(ipAddress, netmask);
        // 包括网络地址和广播地址
        subnetUtils.setInclusiveHostCount(true);
        // 获取网段的起始和结束 IP 地址
        String[] allIps = subnetUtils.getInfo().getAllAddresses();
        return Lists.newArrayList(Arrays.asList(allIps));
    }

}
