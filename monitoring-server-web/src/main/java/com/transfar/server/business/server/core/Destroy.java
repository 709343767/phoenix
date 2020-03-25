package com.transfar.server.business.server.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 在Spring容器销毁时清空一些资源
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/25 16:38
 */
@Component
@Slf4j
public class Destroy implements DisposableBean {

    /**
     * 应用实例池
     */
    @Autowired
    private InstancePool instancePool;

    /**
     * 网络信息池
     */
    @Autowired
    private NetPool netPool;

    /**
     * <p>
     * 在Spring容器销毁时清空资源
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/3/25 16:43
     */
    @Override
    public void destroy() {
        this.instancePool.clear();
        log.info("应用实例池已经清空！");
        this.netPool.clear();
        log.info("网络信息池已经清空！");
    }
}
