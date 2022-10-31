package com.gitee.pifeng.monitoring.server.business.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorTcp;

/**
 * <p>
 * TCP信息服务接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/1/11 16:14
 */
public interface ITcpService extends IService<MonitorTcp> {

    /**
     * <p>
     * 测试TCP连通性
     * </p>
     *
     * @param hostnameTarget 主机名（目的地）
     * @param portTarget     端口号
     * @return true 或者 false
     * @author 皮锋
     * @custom.date 2022/10/10 22:04
     */
    Boolean testMonitorTcp(String hostnameTarget, Integer portTarget);

}
