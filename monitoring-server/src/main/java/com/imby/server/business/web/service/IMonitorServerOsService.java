package com.imby.server.business.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.imby.server.business.web.entity.MonitorServerOs;
import com.imby.server.business.web.vo.HomeServerVo;
import com.imby.server.business.web.vo.LayUiAdminResultVo;
import com.imby.server.business.web.vo.MonitorServerOsVo;

import java.util.List;

/**
 * <p>
 * 服务器服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月7日 下午5:03:49
 */
public interface IMonitorServerOsService extends IService<MonitorServerOs> {

    /**
     * <p>
     * 获取home页的服务器信息
     * </p>
     *
     * @return home页的服务器表现层对象
     * @author 皮锋
     * @custom.date 2020/8/4 16:40
     */
    HomeServerVo getHomeServerOsInfo();

    /**
     * <p>
     * 获取服务器列表
     * </p>
     *
     * @param current    当前页
     * @param size       每页显示条数
     * @param ip         IP
     * @param serverName 服务器名
     * @param osName     操作系统名称
     * @param osVersion  操作系统版本
     * @param userName   用户名称
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2020/9/4 12:37
     */
    Page<MonitorServerOsVo> getMonitorServerOsList(Long current, Long size, String ip, String serverName, String osName, String osVersion, String userName);

    /**
     * <p>
     * 删除服务器
     * </p>
     *
     * @param monitorServerOsVos 服务器信息
     * @return layUiAdmin响应对象：如果删除用户成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/9/4 16:13
     */
    LayUiAdminResultVo deleteMonitorServer(List<MonitorServerOsVo> monitorServerOsVos);
}
