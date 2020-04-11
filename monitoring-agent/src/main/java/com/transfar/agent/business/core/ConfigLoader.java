package com.transfar.agent.business.core;

import com.transfar.agent.AgentApplication;
import com.transfar.common.property.MonitoringProperties;
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
    public static final MonitoringProperties monitoringProperties = AgentApplication.applicationContext
            .getBean(MonitoringProperties.class);

    /**
     * 项目端口号
     */
    public static final int serverPort = AgentApplication.serverPort;

    /**
     * 服务器配置
     */
    public static final ServerProperties serverProperties =
            AgentApplication.applicationContext.getBean(ServerProperties.class);

}
