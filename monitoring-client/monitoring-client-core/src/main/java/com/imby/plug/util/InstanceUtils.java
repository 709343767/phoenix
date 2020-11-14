package com.imby.plug.util;

import com.imby.common.exception.NetException;
import com.imby.common.util.Md5Utils;
import com.imby.common.util.NetUtils;
import com.imby.plug.core.ConfigLoader;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class InstanceUtils {

    /**
     * 应用ID
     */
    private static String instanceId;

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
        // 实例次序（不能重复）
        int order = ConfigLoader.MONITORING_PROPERTIES.getOwnProperties().getInstanceOrder();
        // 实例名称
        String instanceName = ConfigLoader.MONITORING_PROPERTIES.getOwnProperties().getInstanceName();
        // 实例ID
        instanceId = Md5Utils.encrypt(mac + ip + order + instanceName);
        return instanceId;
    }

    public static void main(String[] args) throws InterruptedException, NetException, SigarException {
        for (int i = 0; i < 100; i++) {
            String id = getInstanceId();
            log.info("当前应用的ID为：" + id);
            Thread.sleep(5L * 1000L);
        }
    }
}
