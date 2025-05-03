package com.gitee.pifeng.monitoring.server.business.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.common.dto.HeartbeatPackage;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorInstance;

/**
 * <p>
 * 应用实例服务接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/6/29 15:20
 */
public interface IInstanceService extends IService<MonitorInstance> {

    /**
     * <p>
     * 把应用实例添加或者更新到数据库
     * </p>
     *
     * @param heartbeatPackage 心跳包
     * @author 皮锋
     * @custom.date 2025/5/3 14:31
     */
    void operateMonitorInstance(HeartbeatPackage heartbeatPackage);

}
