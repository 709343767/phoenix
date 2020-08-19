package com.imby.agent.core;

import com.imby.agent.AgentApplication;
import com.imby.common.property.MonitoringProperties;

import org.springframework.boot.autoconfigure.web.ServerProperties;

/**
 * <p>
 * 监控代理端加载配置信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月4日 下午2:36:18
 */
public class ConfigLoader {

    /**
     * 监控配置
     */
    public static final MonitoringProperties MONITORING_PROPERTIES = AgentApplication.applicationContext
            .getBean(MonitoringProperties.class);

    /**
     * 项目端口号
     */
    public static final int SERVER_PORT = AgentApplication.serverPort;

    /**
     * 服务器配置
     */
    public static final ServerProperties SERVER_PROPERTIES =
            AgentApplication.applicationContext.getBean(ServerProperties.class);

}
