package com.gitee.pifeng.monitoring.common.util.server.oshi;

import com.gitee.pifeng.monitoring.common.init.InitOshi;
import oshi.software.os.OperatingSystem;

/**
 * <p>
 * 操作系统工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/8/5 11:26
 */
public class OperatingSystemUtils extends InitOshi {

    /**
     * <p>
     * 获取操作系统信息
     * </p>
     *
     * @return {@link OperatingSystem}
     * @author 皮锋
     * @custom.date 2022/8/5 11:31
     */
    public static OperatingSystem getOperatingSystemInfo() {
        return SYSTEM_INFO.getOperatingSystem();
    }

}
