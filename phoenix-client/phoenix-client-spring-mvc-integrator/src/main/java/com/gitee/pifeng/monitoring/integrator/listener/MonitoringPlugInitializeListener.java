package com.gitee.pifeng.monitoring.integrator.listener;

import com.gitee.pifeng.monitoring.common.exception.BadListenerConfigException;
import com.gitee.pifeng.monitoring.plug.Monitor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;

/**
 * <p>
 * 监控客户端初始化监听器，在Servlet上下文初始化完成的时候开启监控功能
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/14 18:31
 */
@Slf4j
public class MonitoringPlugInitializeListener implements ServletContextListener {

    /**
     * <p>
     * 开启监控
     * </p>
     *
     * @param sce Servlet上下文事件
     * @author 皮锋
     * @custom.date 2020/4/11 22:30
     */
    @SneakyThrows
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 配置参数值
        String configLocation = sce.getServletContext().getInitParameter("configLocation");
        // 自定义了配置文件路径和名字
        if (StringUtils.isNotBlank(configLocation)) {
            String[] config = this.getConfigPathAndName(configLocation);
            Monitor.start(config[0], config[1]);
        } else {
            Monitor.start();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("Servlet上下文销毁！");
    }

    /**
     * <p>
     * 获取配置文件路径和名字
     * </p>
     *
     * @param configLocation 配置参数值
     * @return 配置文件路径和名字
     * @throws BadListenerConfigException 错误的监听器配置异常
     * @author 皮锋
     * @custom.date 2020年3月14日 下午9:29:16
     */
    private String[] getConfigPathAndName(String configLocation) throws BadListenerConfigException {
        // 异常信息
        String expMsg = "\r\n监控客户端初始化监听器配置有误，请参考如下配置：\r\n"
                + "<context-param>\r\n"
                + " <param-name>configLocation</param-name>\r\n"
                + " <param-value>classpath:conf/monitoring.properties</param-value>\r\n"
                + "</context-param>\r\n"
                + "<listener>\r\n"
                + " <listener-class>com.gitee.pifeng.monitoring.integrator.listener.MonitoringPlugInitializeListener</listener-class>\r\n"
                + "</listener>\r\n";
        // 返回值
        String[] result = new String[2];
        if (!StringUtils.containsIgnoreCase(configLocation, "classpath:")) {
            throw new BadListenerConfigException(expMsg);
        }
        String[] temp = configLocation.split(":");
        if (temp.length != 2) {
            throw new BadListenerConfigException(expMsg);
        }
        String[] pathAndName = temp[1].split("/");
        // 配置文件路径
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < pathAndName.length - 1; i++) {
            builder.append(pathAndName[i]).append(File.separator);
        }
        String path = builder.toString();
        // 配置文件名字
        String name = pathAndName[pathAndName.length - 1];
        result[0] = path;
        result[1] = name;
        return result;
    }

}
