package com.transfar.listener;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.transfar.Monitor;

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

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Monitor.start();
        log.info("监控功能开启成功！");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
