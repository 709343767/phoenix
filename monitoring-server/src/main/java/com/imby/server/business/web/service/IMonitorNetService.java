package com.imby.server.business.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.imby.server.business.web.entity.MonitorNet;
import com.imby.server.business.web.vo.HomeNetVo;

/**
 * <p>
 * 网络信息服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/1 15:09
 */
public interface IMonitorNetService extends IService<MonitorNet> {

    /**
     * <p>
     * 获取home页的网络信息
     * </p>
     *
     * @return home页的网络信息表现层对象
     * @author 皮锋
     * @custom.date 2020/9/1 15:20
     */
    HomeNetVo getHomeNetInfo();
}
