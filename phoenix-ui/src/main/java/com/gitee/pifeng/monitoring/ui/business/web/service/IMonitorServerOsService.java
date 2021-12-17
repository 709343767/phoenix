package com.gitee.pifeng.monitoring.ui.business.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorServerOs;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorServerOsVo;

/**
 * <p>
 * 服务器操作系统服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-01-21
 */
public interface IMonitorServerOsService extends IService<MonitorServerOs> {

    /**
     * <p>
     * 获取服务器操作系统信息
     * </p>
     *
     * @param ip IP地址
     * @return 服务器操作系统信息表现层对象
     * @author 皮锋
     * @custom.date 2021/1/21 14:40
     */
    MonitorServerOsVo getMonitorServerOsInfo(String ip);

}
