package com.gitee.pifeng.monitoring.server.business.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.common.exception.NetException;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorNet;

/**
 * <p>
 * 网络信息服务接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/31 17:20
 */
public interface INetService extends IService<MonitorNet> {

    /**
     * <p>
     * 获取被监控网络源IP地址
     * </p>
     *
     * @return 被监控网络源IP地址
     * @throws NetException 自定义获取网络信息异常
     * @author 皮锋
     * @custom.date 2021/10/6 22:05
     */
    String getSourceIp() throws NetException;

    /**
     * <p>
     * 测试网络连通性
     * </p>
     *
     * @param ipTarget IP地址（目的地）
     * @return true 或者 false
     * @author 皮锋
     * @custom.date 2022/10/10 22:04
     */
    Boolean testMonitorNetwork(String ipTarget);
}
