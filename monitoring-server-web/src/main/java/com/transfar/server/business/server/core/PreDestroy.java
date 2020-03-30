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
public class PreDestroy implements DisposableBean {

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
     * 服务器内存信息池
     */
    @Autowired
    private MemoryPool memoryPool;

    /**
     * 服务器CPU信息池
     */
    @Autowired
    private CpuPool cpuPool;

    /**
     * 服务器磁盘信息池
     */
    @Autowired
    private DiskPool diskPool;

    /**
     * <p>
     * 在Spring容器销毁时清空资源
     * </p>
     * 1.清空应用实例池；<br>
     * 2.清空网络信息池；<br>
     * 3.清空服务器内存信息池；<br>
     * 4.清空服务器CPU信息池；<br>
     * 5.清空服务器磁盘信息池；<br>
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
        this.memoryPool.clear();
        log.info("服务器内存信息池已经清空！");
        this.cpuPool.clear();
        log.info("服务器CPU信息池已经清空！");
        this.diskPool.clear();
        log.info("服务器磁盘信息池已经清空！");
    }
}
