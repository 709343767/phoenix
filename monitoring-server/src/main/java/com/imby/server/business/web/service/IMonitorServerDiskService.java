package com.imby.server.business.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.imby.server.business.web.entity.MonitorServerDisk;
import com.imby.server.business.web.vo.ServerDetailPageServerDiskChartVo;

import java.util.List;

/**
 * <p>
 * 服务器磁盘服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/4 20:50
 */
public interface IMonitorServerDiskService extends IService<MonitorServerDisk> {

    /**
     * <p>
     * 获取服务器详情页面服务器磁盘图表信息
     * </p>
     *
     * @param ip 服务器IP地址
     * @return 服务器详情页面服务器磁盘图表信息表现层对象
     * @author 皮锋
     * @custom.date 2020/10/22 17:52
     */
    List<ServerDetailPageServerDiskChartVo> getServerDetailPageServerDiskChartInfo(String ip);

}
