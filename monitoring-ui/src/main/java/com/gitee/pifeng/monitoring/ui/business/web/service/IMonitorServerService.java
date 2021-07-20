package com.gitee.pifeng.monitoring.ui.business.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorServer;
import com.gitee.pifeng.monitoring.ui.business.web.vo.HomeServerVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorServerVo;

import java.util.List;

/**
 * <p>
 * 服务器服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月7日 下午5:03:49
 */
public interface IMonitorServerService extends IService<MonitorServer> {

    /**
     * <p>
     * 获取home页的服务器信息
     * </p>
     *
     * @return home页的服务器表现层对象
     * @author 皮锋
     * @custom.date 2020/8/4 16:40
     */
    HomeServerVo getHomeServerInfo();

    /**
     * <p>
     * 获取服务器列表
     * </p>
     *
     * @param current    当前页
     * @param size       每页显示条数
     * @param ip         IP
     * @param serverName 服务器名
     * @param isOnline   状态
     * @return 简单分页模型
     * @author 皮锋
     * @custom.date 2020/9/4 12:37
     */
    Page<MonitorServerVo> getMonitorServerList(Long current, Long size, String ip, String serverName, String isOnline);

    /**
     * <p>
     * 删除服务器
     * </p>
     *
     * @param monitorServerVos 服务器信息
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/9/4 16:13
     */
    LayUiAdminResultVo deleteMonitorServer(List<MonitorServerVo> monitorServerVos);

    /**
     * <p>
     * 获取服务器网卡地址
     * </p>
     *
     * @param ip 服务器IP
     * @return 网卡地址列表
     * @author 皮锋
     * @custom.date 2021/1/11 9:54
     */
    List<String> getNetcardAddress(String ip);

}