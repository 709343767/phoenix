package com.gitee.pifeng.monitoring.common.property.client;

import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import com.gitee.pifeng.monitoring.common.init.InitSecure;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 监控属性
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月5日 下午3:24:54
 */
@Data
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class MonitoringProperties implements ISuperBean {

    /**
     * 安全相关监控属性
     * 注意：这个属性名称不能随意改动，因为在 {@link InitSecure} 用了反射来获取这个属性
     */
    private MonitoringSecureProperties secure;

    /**
     * 与通信相关的监控属性
     */
    private MonitoringCommProperties comm;

    /**
     * 应用程序监控属性
     */
    private MonitoringInstanceProperties instance;

    /**
     * 心跳属性
     */
    private MonitoringHeartbeatProperties heartbeat;

    /**
     * 服务器信息属性
     */
    private MonitoringServerInfoProperties serverInfo;

    /**
     * Java虚拟机信息属性
     */
    private MonitoringJvmInfoProperties jvmInfo;

}
