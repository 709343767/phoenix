package com.gitee.pifeng.monitoring.ui.business.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorServerGpu;
import com.gitee.pifeng.monitoring.ui.business.web.vo.ServerDetailPageServerGpuVo;

import java.util.List;

/**
 * <p>
 * 服务器GPU服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2025-06-09
 */
public interface IMonitorServerGpuService extends IService<MonitorServerGpu> {

    /**
     * <p>
     * 获取服务器详情页面服务器GPU信息
     * </p>
     *
     * @param ip 服务器IP地址
     * @return 服务器详情页面服务器GPU信息表现层对象
     * @author 皮锋
     * @custom.date 2025/6/16 11:38
     */
    List<ServerDetailPageServerGpuVo> getServerDetailPageServerGpuInfo(String ip);

}
