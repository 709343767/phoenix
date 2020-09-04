package com.imby.server.business.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.imby.common.constant.OsTypeConstants;
import com.imby.server.business.web.dao.IMonitorServerOsDao;
import com.imby.server.business.web.entity.MonitorServerOs;
import com.imby.server.business.web.service.IMonitorServerOsService;
import com.imby.server.business.web.vo.HomeServerOsVo;
import com.imby.server.business.web.vo.MonitorServerOsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务器服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月7日 下午5:03:49
 */
@Service
public class MonitorServerOsServiceImpl extends ServiceImpl<IMonitorServerOsDao, MonitorServerOs> implements IMonitorServerOsService {

    /**
     * 服务器数据访问对象
     */
    @Autowired
    private IMonitorServerOsDao monitorServerOsDao;

    /**
     * <p>
     * 获取home页的服务器信息
     * </p>
     *
     * @return home页的服务器表现层对象
     * @author 皮锋
     * @custom.date 2020/8/4 16:40
     */
    @Override
    public HomeServerOsVo getHomeServerOsInfo() {
        List<MonitorServerOs> monitorServerOss = this.monitorServerOsDao.selectList(new LambdaQueryWrapper<>());
        // home页的服务器表现层对象
        HomeServerOsVo homeServerOsVo = new HomeServerOsVo();
        homeServerOsVo.setServerSum(monitorServerOss.size());
        homeServerOsVo.setLinuxSum((int) monitorServerOss.stream().filter(e -> StringUtils.containsIgnoreCase(e.getOsName(), OsTypeConstants.LINUX)).count());
        homeServerOsVo.setWindowsSum((int) monitorServerOss.stream().filter(e -> StringUtils.containsIgnoreCase(e.getOsName(), OsTypeConstants.WINDOWS)).count());
        return homeServerOsVo;
    }

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
     * @custom.date 2020/9/4 12:38
     */
    @Override
    public Page<MonitorServerOsVo> getMonitorServerOsList(Long current, Long size, String ip, String serverName, String osName, String osVersion, String userName) {
        // 查询数据库
        IPage<MonitorServerOs> ipage = new Page<>(current, size);
        LambdaQueryWrapper<MonitorServerOs> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(ip)) {
            lambdaQueryWrapper.like(MonitorServerOs::getIp, ip);
        }
        if (StringUtils.isNotBlank(serverName)) {
            lambdaQueryWrapper.like(MonitorServerOs::getServerName, serverName);
        }
        if (StringUtils.isNotBlank(osName)) {
            lambdaQueryWrapper.like(MonitorServerOs::getOsName, osName);
        }
        if (StringUtils.isNotBlank(osVersion)) {
            lambdaQueryWrapper.like(MonitorServerOs::getOsVersion, osVersion);
        }
        if (StringUtils.isNotBlank(userName)) {
            lambdaQueryWrapper.like(MonitorServerOs::getUserName, userName);
        }
        IPage<MonitorServerOs> monitorServerOsPage = this.monitorServerOsDao.selectPage(ipage, lambdaQueryWrapper);
        List<MonitorServerOs> monitorServerOss = monitorServerOsPage.getRecords();
        // 转换成服务器信息表现层对象
        List<MonitorServerOsVo> monitorServerOsVos = Lists.newLinkedList();
        for (MonitorServerOs monitorServerOs : monitorServerOss) {
            MonitorServerOsVo monitorServerOsVo = MonitorServerOsVo.builder().build().convertFor(monitorServerOs);
            monitorServerOsVos.add(monitorServerOsVo);
        }
        // 设置返回对象
        Page<MonitorServerOsVo> monitorServerOsVoPage = new Page<>();
        monitorServerOsVoPage.setRecords(monitorServerOsVos);
        monitorServerOsVoPage.setTotal(monitorServerOsPage.getTotal());
        return monitorServerOsVoPage;
    }
}
