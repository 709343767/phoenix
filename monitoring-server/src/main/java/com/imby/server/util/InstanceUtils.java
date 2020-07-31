package com.imby.server.util;

import com.imby.common.util.Md5Utils;
import com.imby.common.util.NetUtils;
import org.apache.commons.lang3.StringUtils;

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
    private static String instanceName;

    /*
     * 实例描述
     */
    private static String instanceDesc;

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
        String mac = NetUtils.getLocalMac();
        String ip = NetUtils.getLocalIp();
        instanceId = Md5Utils.encrypt16(mac + ip + getInstanceName());
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
        if (StringUtils.isNotEmpty(instanceName)) {
            return instanceName;
        }
        instanceName = "monitoring-server";
        return instanceName;
    }

    /**
     * <p>
     * 获取应用实例描述
     * </p>
     *
     * @return 应用实例描述
     * @author 皮锋
     * @custom.date 2020/7/31 21:24
     */
    public static String getInstanceDesc() {
        if (StringUtils.isNotEmpty(instanceDesc)) {
            return instanceDesc;
        }
        instanceDesc = "监控服务端程序";
        return instanceDesc;
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            String id = getInstanceId();
            System.out.println("当前应用的ID为：" + id);
            Thread.sleep(5 * 1000);
        }
    }
}
