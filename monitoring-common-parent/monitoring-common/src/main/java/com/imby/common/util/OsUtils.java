package com.imby.common.util;

/**
 * <p> 
 * 操作系统工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年4月13日 下午5:05:53
 */
public class OsUtils {
	
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
        boolean isWindowsOS = false;
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().contains("windows")) {
            isWindowsOS = true;
        }
        return isWindowsOS;
    }

}

	