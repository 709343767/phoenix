package com.gitee.pifeng.monitoring.plug.core;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.util.IdUtil;
import com.gitee.pifeng.monitoring.common.exception.NetException;
import com.gitee.pifeng.monitoring.common.property.client.MonitoringInstanceProperties;
import com.gitee.pifeng.monitoring.common.property.client.MonitoringServerInfoProperties;
import com.gitee.pifeng.monitoring.common.util.DirUtils;
import com.gitee.pifeng.monitoring.common.util.Md5Utils;
import com.gitee.pifeng.monitoring.common.util.server.NetUtils;
import com.gitee.pifeng.monitoring.common.util.server.oshi.BaseboardUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Optional;

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
     * 应用实例ID文件（相对路径 + 文件名）
     */
    private static final String INSTANCE_ID_FILENAME;

    static {
        String instanceIdFileName;
        try {
            MonitoringInstanceProperties instance = ConfigLoader.getMonitoringProperties().getInstance();
            String endpoint = StringUtils.lowerCase(instance.getEndpoint());
            String name = "phoenix-" + endpoint;
            instanceIdFileName = "liblog4phoenix" + File.separator + "data" + File.separator + name + File.separator + endpoint + "InstanceId";
        } catch (Exception e) {
            // 防止静态初始化块抛异常导致类加载失败
            instanceIdFileName = "liblog4phoenix" + File.separator + "data" + File.separator + "instanceId";
        }
        INSTANCE_ID_FILENAME = instanceIdFileName;
    }

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
        if (StringUtils.isNotBlank(instanceId)) {
            return instanceId;
        }
        synchronized (InstanceGenerator.class) {
            // 第二次检查
            if (StringUtils.isNotBlank(instanceId)) {
                return instanceId;
            }
            // 先尝试从文件读取
            instanceId = readInstanceIdInFile();
            if (StringUtils.isNotBlank(instanceId)) {
                return instanceId;
            }
            // 获取配置信息
            MonitoringInstanceProperties monitoringInstanceProperties = ConfigLoader.getMonitoringProperties().getInstance();
            MonitoringServerInfoProperties monitoringServerInfoProperties = ConfigLoader.getMonitoringProperties().getServerInfo();
            // UUID
            String uuid = IdUtil.fastSimpleUUID();
            // 实例次序（不能重复）
            int order = monitoringInstanceProperties.getOrder();
            // 实例名称
            String instanceName = monitoringInstanceProperties.getName();
            // 获取主板序列号
            String baseboardSerialNumber = BaseboardUtils.getBaseboardSerialNumber();
            // 能获取到主板号
            if (!"未知".equals(baseboardSerialNumber) && StringUtils.isNotBlank(baseboardSerialNumber)) {
                instanceId = Md5Utils.encrypt(uuid + baseboardSerialNumber + order + instanceName);
            }
            // 不能获取到主板号
            else {
                // MAC地址
                String mac = NetUtils.getLocalMac();
                // 如果配置了服务器IP，用配置的，如果没有配置服务器IP，则自己获取
                String ip = Optional.ofNullable(monitoringServerInfoProperties.getIp()).orElseGet(NetUtils::getLocalIp);
                instanceId = Md5Utils.encrypt(uuid + mac + ip + order + instanceName);
            }
            // 写入文件
            writeInstanceIdToFile(instanceId);
            return instanceId;
        }
    }

    /**
     * <p>
     * 把应用实例ID写入文件
     * </p>
     *
     * @param instanceId 应用实例ID
     * @author 皮锋
     * @custom.date 2025年5月26日 下午21:59:39
     */
    private static void writeInstanceIdToFile(String instanceId) {
        // 根据相对路径获取绝对路径
        String pathname = DirUtils.getAbsolutePathByRelativePath(INSTANCE_ID_FILENAME);
        try {
            FileWriter writer = new FileWriter(pathname);
            writer.write(instanceId);
        } catch (Exception e) {
            log.error("把应用实例ID写入文件失败：{}", e.getMessage(), e);
        }
    }

    /**
     * <p>
     * 从文件读取应用实例ID
     * </p>
     *
     * @return 应用实例ID
     * @author 皮锋
     * @custom.date 2025年5月26日 22:02:18
     */
    private static String readInstanceIdInFile() {
        // 根据相对路径获取绝对路径
        String pathname = DirUtils.getAbsolutePathByRelativePath(INSTANCE_ID_FILENAME);
        try {
            FileReader fileReader = new FileReader(pathname);
            return fileReader.readString();
        } catch (Exception e) {
            return null;
        }
    }

}
