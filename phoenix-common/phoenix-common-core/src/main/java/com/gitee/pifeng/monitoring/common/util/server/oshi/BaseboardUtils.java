package com.gitee.pifeng.monitoring.common.util.server.oshi;

import com.gitee.pifeng.monitoring.common.init.InitOshi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import oshi.hardware.Baseboard;
import oshi.hardware.ComputerSystem;
import oshi.hardware.HardwareAbstractionLayer;

import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>
 * 主板工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2024/12/13 13:57
 */
@Slf4j
public class BaseboardUtils extends InitOshi {

    /**
     * <p>
     * 主板序列号
     * </p>
     */
    private static final AtomicReference<String> SERIAL_NUMBER = new AtomicReference<>("未知");

    /**
     * <p>
     * 是否已经成功获取过主板序列号
     * </p>
     */
    private static volatile boolean isSerialNumberInitialized = false;

    /**
     * <p>
     * 获取主板序列号
     * </p>
     *
     * @return 主板序列号，如果获取失败，则返回 "未知"
     * @author 皮锋
     * @custom.date 2024/12/13 14:07
     */
    public static String getBaseboardSerialNumber() {
        if (isSerialNumberInitialized) {
            return SERIAL_NUMBER.get();
        }
        synchronized (BaseboardUtils.class) {
            if (!isSerialNumberInitialized) {
                try {
                    HardwareAbstractionLayer hal = SYSTEM_INFO.getHardware();
                    ComputerSystem computerSystem = hal.getComputerSystem();
                    // 从 ComputerSystem 对象获取主板信息
                    Baseboard baseboard = computerSystem.getBaseboard();
                    // 获取主板序列号
                    String serialNumber = baseboard.getSerialNumber();
                    if (StringUtils.isNotBlank(serialNumber)) {
                        SERIAL_NUMBER.set(serialNumber);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                } finally {
                    // 标记为已初始化
                    isSerialNumberInitialized = true;
                }
            }
            return SERIAL_NUMBER.get();
        }
    }

}