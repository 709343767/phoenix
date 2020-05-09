package com.imby.agent.util;

import com.imby.agent.business.core.ConfigLoader;
import com.imby.common.util.MD5Utils;
import com.imby.common.util.NetUtils;
import lombok.SneakyThrows;
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
     * 应用实例ID
     */
    private static String instanceId;

    /**
     * 应用实例名字
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
        // 如果配置了实例ID，用配置的ID
        String id = ConfigLoader.monitoringProperties.getOwnProperties().getInstanceId();
        if (StringUtils.isNotBlank(id)) {
            instanceId = id;
            return instanceId;
        }
        String rootUrl = InstanceUtils.getRootUrl();
        instanceId = MD5Utils.encrypt16(rootUrl);
        return instanceId;
    }

    /**
     * <p>
     * 获取应用实例名称
     * </p>
     *
     * @return 应用实例名称
     * @author 皮锋
     * @custom.date 2020年3月4日 下午11:12:46
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
     * 获取项目根路径
     * </p>
     *
     * @return 项目根URL
     * @author 皮锋
     * @custom.date 2020/3/12 21:46
     */
    @SneakyThrows
    private static String getRootUrl() {
        String contextPath = StringUtils.isBlank(ConfigLoader.serverProperties.getServlet().getContextPath()) ? ""
                : ConfigLoader.serverProperties.getServlet().getContextPath();
        return "http://" + NetUtils.getLocalIp() + ":" + ConfigLoader.serverPort + contextPath;
    }

}
