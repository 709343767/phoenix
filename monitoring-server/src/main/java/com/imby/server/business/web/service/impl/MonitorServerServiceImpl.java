package com.imby.server.business.web.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.imby.common.constant.AlarmTypeEnums;
import com.imby.server.business.web.dao.*;
import com.imby.server.business.web.entity.*;
import com.imby.server.business.web.service.IMonitorServerService;
import com.imby.server.business.web.vo.HomeServerVo;
import com.imby.server.business.web.vo.LayUiAdminResultVo;
import com.imby.server.business.web.vo.MonitorServerVo;
import com.imby.server.constant.WebResponseConstants;
import com.imby.server.core.ThreadPool;
import com.imby.server.inf.IServerMonitoringListener;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务器服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月7日 下午5:03:49
 */
@Service
public class MonitorServerServiceImpl extends ServiceImpl<IMonitorServerDao, MonitorServer> implements IMonitorServerService {

    /**
     * 服务器信息监听器
     */
    @Autowired
    private List<IServerMonitoringListener> serverMonitoringListeners;

    /**
     * 服务器数据访问对象
     */
    @Autowired
    private IMonitorServerDao monitorServerDao;

    /**
     * 服务器CPU数据访问对象
     */
    @Autowired
    private IMonitorServerCpuDao monitorServerCpuDao;

    /**
     * 服务器磁盘数据访问对象
     */
    @Autowired
    private IMonitorServerDiskDao monitorServerDiskDao;

    /**
     * 服务器内存数据访问对象
     */
    @Autowired
    private IMonitorServerMemoryDao monitorServerMemoryDao;

    /**
     * 服务器网卡数据访问对象
     */
    @Autowired
    private IMonitorServerNetcardDao monitorServerNetcardDao;

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
    public HomeServerVo getHomeServerInfo() {
        // 服务器类型统计
        Map<String, Object> map = this.monitorServerDao.getServerOsTypeStatistics();
        return HomeServerVo.builder()
                .serverSum(NumberUtil.parseInt(map.get("serverSum").toString()))
                .windowsSum(NumberUtil.parseInt(map.get("windowsSum").toString()))
                .linuxSum(NumberUtil.parseInt(map.get("linuxSum").toString()))
                .otherSum(NumberUtil.parseInt(map.get("otherSum").toString()))
                .build();
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
    public Page<MonitorServerVo> getMonitorServerList(Long current, Long size, String ip, String serverName, String osName, String osVersion, String userName) {
        // 查询数据库
        IPage<MonitorServer> ipage = new Page<>(current, size);
        LambdaQueryWrapper<MonitorServer> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(ip)) {
            lambdaQueryWrapper.like(MonitorServer::getIp, ip);
        }
        if (StringUtils.isNotBlank(serverName)) {
            lambdaQueryWrapper.like(MonitorServer::getServerName, serverName);
        }
        if (StringUtils.isNotBlank(osName)) {
            lambdaQueryWrapper.like(MonitorServer::getOsName, osName);
        }
        if (StringUtils.isNotBlank(osVersion)) {
            lambdaQueryWrapper.like(MonitorServer::getOsVersion, osVersion);
        }
        if (StringUtils.isNotBlank(userName)) {
            lambdaQueryWrapper.like(MonitorServer::getUserName, userName);
        }
        IPage<MonitorServer> monitorServerPage = this.monitorServerDao.selectPage(ipage, lambdaQueryWrapper);
        List<MonitorServer> monitorServers = monitorServerPage.getRecords();
        // 转换成服务器信息表现层对象
        List<MonitorServerVo> monitorServerVos = Lists.newLinkedList();
        for (MonitorServer monitorServer : monitorServers) {
            MonitorServerVo monitorServerVo = MonitorServerVo.builder().build().convertFor(monitorServer);
            monitorServerVos.add(monitorServerVo);
        }
        // 设置返回对象
        Page<MonitorServerVo> monitorServerVoPage = new Page<>();
        monitorServerVoPage.setRecords(monitorServerVos);
        monitorServerVoPage.setTotal(monitorServerPage.getTotal());
        return monitorServerVoPage;
    }

    /**
     * <p>
     * 删除服务器
     * </p>
     *
     * @param monitorServerVos 服务器信息
     * @return layUiAdmin响应对象：如果删除用户成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/9/4 16:13
     */
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public LayUiAdminResultVo deleteMonitorServer(List<MonitorServerVo> monitorServerVos) {
        List<String> ips = Lists.newArrayList();
        for (MonitorServerVo monitorServerVo : monitorServerVos) {
            ips.add(monitorServerVo.getIp());
        }
        // 服务器表
        LambdaUpdateWrapper<MonitorServer> serverLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverLambdaUpdateWrapper.in(MonitorServer::getIp, ips);
        this.monitorServerDao.delete(serverLambdaUpdateWrapper);
        // 服务器CPU表
        LambdaUpdateWrapper<MonitorServerCpu> serverCpuLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverCpuLambdaUpdateWrapper.in(MonitorServerCpu::getIp, ips);
        this.monitorServerCpuDao.delete(serverCpuLambdaUpdateWrapper);
        // 服务器磁盘表
        LambdaUpdateWrapper<MonitorServerDisk> serverDiskLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverDiskLambdaUpdateWrapper.in(MonitorServerDisk::getIp, ips);
        this.monitorServerDiskDao.delete(serverDiskLambdaUpdateWrapper);
        // 服务器内存表
        LambdaUpdateWrapper<MonitorServerMemory> serverMemoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverMemoryLambdaUpdateWrapper.in(MonitorServerMemory::getIp, ips);
        this.monitorServerMemoryDao.delete(serverMemoryLambdaUpdateWrapper);
        // 服务器网卡表
        LambdaUpdateWrapper<MonitorServerNetcard> serverNetcardLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverNetcardLambdaUpdateWrapper.in(MonitorServerNetcard::getIp, ips);
        this.monitorServerNetcardDao.delete(serverNetcardLambdaUpdateWrapper);

        // 调用监听器回调接口
        this.serverMonitoringListeners.forEach(e ->
                ThreadPool.COMMON_CPU_INTENSIVE_THREAD_POOL.execute(() ->
                        e.wakeUpMonitorPool(AlarmTypeEnums.SERVER, ips)));
        return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
    }

    /**
     * <p>
     * 获取服务器操作系统信息
     * </p>
     *
     * @param ip 服务器IP地址
     * @return 服务器信息表现层对象
     * @author 皮锋
     * @custom.date 2020/10/26 20:26
     */
    @Override
    public MonitorServerVo getMonitorServerInfo(String ip) {
        LambdaQueryWrapper<MonitorServer> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MonitorServer::getIp, ip);
        MonitorServer monitorServer = this.monitorServerDao.selectOne(lambdaQueryWrapper);
        return MonitorServerVo.builder().build().convertFor(monitorServer);
    }

}
