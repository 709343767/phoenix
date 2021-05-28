package com.gitee.pifeng.monitoring.common.util.server;

import com.gitee.pifeng.monitoring.common.domain.server.OsDomain;
import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.util.Calendar;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;

/**
 * <p>
 * 操作系统工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年4月13日 下午5:05:53
 */
public final class OsUtils {

    /**
     * 系统属性
     */
    private static final Properties PROPS = System.getProperties();

    /**
     * 环境属性
     */
    private static final Map<String, String> ENVS = System.getenv();

    /**
     * <p>
     * 私有化构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2021/1/14 10:09
     */
    private OsUtils() {
    }

    /**
     * <p>
     * 判断操作系统是不是Windows
     * </p>
     *
     * @return boolean
     * @author 皮锋
     * @custom.date 2020年3月20日 上午10:30:30
     */
    public static boolean isWindowsOs() {
        boolean isWindowsOs = false;
        String osName = System.getProperty("os.name");
        if (StringUtils.containsIgnoreCase(osName, "windows")) {
            isWindowsOs = true;
        }
        return isWindowsOs;
    }

    /**
     * <p>
     * 获取操作系统信息
     * </p>
     *
     * @return {@link OsDomain}
     * @author 皮锋
     * @custom.date 2020年3月3日 下午2:15:45
     */
    public static OsDomain getOsInfo() {
        Calendar cal = Calendar.getInstance();
        TimeZone timeZone = cal.getTimeZone();
        return new OsDomain()
                .setOsName(PROPS.getProperty("os.name") + " " + PROPS.getProperty("os.arch"))
                .setOsVersion(PROPS.getProperty("os.version"))
                .setUserName(PROPS.getProperty("user.name"))
                .setUserHome(PROPS.getProperty("user.home"))
                .setOsTimeZone(timeZone.getDisplayName())
                .setComputerName(getComputerName());
    }

    /**
     * <p>
     * 获取计算机名
     * </p>
     *
     * @return 计算机名
     * @author 皮锋
     * @custom.date 2020/4/10 13:56
     */
    public static String getComputerName() {
        // Windows操作系统
        if (isWindowsOs()) {
            return ENVS.get("COMPUTERNAME");
        } else {
            try {
                return InetAddress.getLocalHost().getHostName();
            } catch (Exception e) {
                return "UnknownHost";
            }
        }
    }

}

	