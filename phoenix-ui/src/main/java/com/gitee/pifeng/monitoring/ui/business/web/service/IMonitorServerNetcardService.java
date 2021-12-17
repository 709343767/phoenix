package com.gitee.pifeng.monitoring.ui.business.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorServerNetcard;
import com.gitee.pifeng.monitoring.ui.business.web.vo.ServerDetailPageServerNetcardVo;

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

}
