package com.imby.plug.util;

import com.imby.common.util.Md5Utils;
import com.imby.common.util.NetUtils;
import com.imby.plug.core.ConfigLoader;

import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * 应用实例工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月4日 下午10:41:27
 */
public class InstanceUtils {

    /**
     * 应用ID
     */
    private static String instanceId;

    /**
     * 应用名字
     */
    private static String instanceName;

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
        // 配置文件中的ID
        String id = ConfigLoader.monitoringProperties.getOwnProperties().getInstanceId();
        if (StringUtils.isNotBlank(id)) {
            instanceId = id;
            return instanceId;
        }
        String mac = NetUtils.getLocalMac();
        String ip = NetUtils.getLocalIp();
        instanceId = Md5Utils.encrypt16(mac + ip + getInstanceName());
        return instanceId;
    }

    /**
     * <p>
     * 获取应用实例名字
     * </p>
     *
     * @return 应用实例名字
     * @author 皮锋
     * @custom.date 2020年3月6日 下午9:16:00
     */
    public static String getInstanceName() {
        if (StringUtils.isNotEmpty(instanceName)) {
            return instanceName;
        }
        instanceName = ConfigLoader.monitoringProperties.getOwnProperties().getInstanceName();
        return instanceName;
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            String id = getInstanceId();
            System.out.println("当前应用的ID为：" + id);
            Thread.sleep(5 * 1000);
        }
    }
}
