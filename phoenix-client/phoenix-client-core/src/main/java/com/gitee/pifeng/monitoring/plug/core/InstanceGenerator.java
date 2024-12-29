package com.gitee.pifeng.monitoring.plug.core;

import com.gitee.pifeng.monitoring.common.exception.NetException;
import com.gitee.pifeng.monitoring.common.util.Md5Utils;
import com.gitee.pifeng.monitoring.common.util.server.NetUtils;
import com.gitee.pifeng.monitoring.common.util.server.oshi.BaseboardUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * 应用实例生成器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月4日 下午10:41:27
 */
@Slf4j
public class InstanceGenerator {

    /**
     * <p>
     * 应用实例ID
     * </p>
     * 使用 volatile 关键字确保在多线程环境下的可见性。
     */
    private static volatile String instanceId;

    /**
     * <p>
     * 私有化构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2021/1/4 9:51
     */
    private InstanceGenerator() {
    }

    /**
     * <p>
     * 获取应用实例ID
     * </p>
     * 使用双重检查锁定来确保在多线程环境下只有一个应用实例ID被创建。
     *
     * @return 应用实例ID
     * @throws NetException 获取网络信息异常
     * @author 皮锋
     * @custom.date 2020年3月4日 下午11:12:46
     */
    public static String getInstanceId() throws NetException {
        // 第一次检查
        if (instanceId == null) {
            synchronized (InstanceGenerator.class) {
                // 第二次检查
                if (instanceId == null) {
                    // 实例次序（不能重复）
                    int order = ConfigLoader.getMonitoringProperties().getInstance().getOrder();
                    // 实例名称
                    String instanceName = ConfigLoader.getMonitoringProperties().getInstance().getName();
                    // 获取主板序列号
                    String baseboardSerialNumber = BaseboardUtils.getBaseboardSerialNumber();
                    // 能获取到主板号
                    String unknown = "未知";
                    if (!StringUtils.equals(baseboardSerialNumber, unknown)) {
                        instanceId = Md5Utils.encrypt(baseboardSerialNumber + order + instanceName);
                    }
                    // 不能获取到主板号
                    else {
                        String mac = NetUtils.getLocalMac();
                        // 如果配置了服务器IP，用配置的，如果没有配置服务器IP，则自己获取
                        String ip = ConfigLoader.getMonitoringProperties().getServerInfo().getIp() == null ? NetUtils.getLocalIp() : ConfigLoader.getMonitoringProperties().getServerInfo().getIp();
                        instanceId = Md5Utils.encrypt(mac + ip + order + instanceName);
                    }
                }
            }
        }
        return instanceId;
    }

}
