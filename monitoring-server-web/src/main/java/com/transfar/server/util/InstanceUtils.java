package com.transfar.server.util;

import org.apache.commons.lang3.StringUtils;

import com.transfar.common.util.LocalNetUtils;
import com.transfar.common.util.MD5Utils;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 * <p>
 * 应用实例ID工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月4日 下午10:41:27
 */
public class InstanceUtils {

    /**
     * 实例ID
     */
    private static String instanceId;

    /*
     * 实例名字
     */
    //private static String instanceName;

    /**
     * <p>
     * 获取java进程PID
     * </p>
     *
     * @return PID
     * @author 皮锋
     * @custom.date 2020年3月4日 下午10:48:07
     */
    public static String getJavaPid() {
        String pid = null;
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        String name = runtime.getName();
        int index = name.indexOf("@");
        if (index != -1) {
            pid = name.substring(0, index);
        }
        return pid;
    }

    /**
     * <p>
     * 获取应用实例ID
     * </p>
     *
     * @return 应用实例ID
     * @author 皮锋
     * @custom.date 2020年3月4日 下午11:12:46
     */
    public static String getInstanceId() {
        if (StringUtils.isNotEmpty(instanceId)) {
            return instanceId;
        }
        String mac = LocalNetUtils.getLocalMac();
        String ip = LocalNetUtils.getLocalHostAddress();
        instanceId = MD5Utils.encrypt16(mac + ip + getInstanceName());
        return instanceId;
    }

    /**
     * <p>
     * 获取应用实例名称
     * </p>
     *
     * @return 应用实例名称
     * @author 皮锋
     * @custom.date 2020年3月8日 下午3:35:21
     */
    public static String getInstanceName() {
        return "monitoring-server-web";
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            String id = getInstanceId();
            System.out.println("当前应用的ID为：" + id);
            Thread.sleep(5 * 1000);
        }
    }
}
