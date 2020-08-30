package com.imby.plug.util;

import com.imby.common.exception.NetException;
import com.imby.common.util.Md5Utils;
import com.imby.common.util.NetUtils;
import com.imby.plug.core.ConfigLoader;
import org.apache.commons.lang3.StringUtils;
import org.hyperic.sigar.SigarException;

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
     * 应用描述
     */
    private static String instanceDesc;

    /**
     * <p>
     * 获取应用实例ID
     * </p>
     *
     * @return 应用实例ID
     * @throws NetException   获取网络信息异常
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020年3月4日 下午11:12:46
     */
    public static String getInstanceId() throws NetException, SigarException {
        if (StringUtils.isNotEmpty(instanceId)) {
            return instanceId;
        }
        String mac = NetUtils.getLocalMac();
        String ip = NetUtils.getLocalIp();
        int order = ConfigLoader.monitoringProperties.getOwnProperties().getInstanceOrder();
        instanceId = Md5Utils.encrypt16(mac + ip + order + getInstanceName());
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

    /**
     * <p>
     * 获取应用实例描述
     * </p>
     *
     * @return 应用实例描述
     * @author 皮锋
     * @custom.date 2020/7/31 17:44
     */
    public static String getInstanceDesc() {
        if (StringUtils.isNotEmpty(instanceDesc)) {
            return instanceDesc;
        }
        instanceDesc = ConfigLoader.monitoringProperties.getOwnProperties().getInstanceDesc();
        return instanceDesc;
    }

    public static void main(String[] args) throws InterruptedException, NetException, SigarException {
        for (int i = 0; i < 100; i++) {
            String id = getInstanceId();
            System.out.println("当前应用的ID为：" + id);
            Thread.sleep(5 * 1000);
        }
    }
}
