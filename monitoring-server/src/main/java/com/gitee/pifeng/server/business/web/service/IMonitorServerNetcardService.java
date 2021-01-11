package com.gitee.pifeng.server.business.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.server.business.web.entity.MonitorServerNetcard;
import com.gitee.pifeng.server.business.web.vo.ServerDetailPageServerNetcardVo;
import com.gitee.pifeng.server.business.web.vo.ServerDetailPageServerNetworkSpeedChartVo;

import java.util.List;

/**
 * <p>
 * 服务器网卡服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/4 20:50
 */
public interface IMonitorServerNetcardService extends IService<MonitorServerNetcard> {

    /**
     * <p>
     * 获取服务器详情页面服务器网卡信息
     * </p>
     *
     * @param ip 服务器IP地址
     * @return 服务器详情页面服务器网卡信息表现层对象
     * @author 皮锋
     * @custom.date 2020/10/30 19:49
     */
    List<ServerDetailPageServerNetcardVo> getServerDetailPageServerNetcardInfo(String ip);

    /**
     * <p>
     * 获取服务器详情页面服务器网速图表信息
     * </p>
     *
     * @param ip      服务器IP地址
     * @param address 服务器网卡地址
     * @param time    时间
     * @return 服务器详情页面服务器网速图表信息表现层对象
     * @author 皮锋
     * @custom.date 2021/1/10 20:44
     */
    List<ServerDetailPageServerNetworkSpeedChartVo> getServerDetailPageServerNetworkSpeedChartInfo(String ip, String address, String time);

}
