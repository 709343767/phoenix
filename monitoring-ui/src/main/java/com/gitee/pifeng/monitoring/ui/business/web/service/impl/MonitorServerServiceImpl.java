package com.gitee.pifeng.monitoring.ui.business.web.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.ui.business.web.dao.*;
import com.gitee.pifeng.monitoring.ui.business.web.entity.*;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorServerService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.HomeServerVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorServerVo;
import com.gitee.pifeng.monitoring.ui.constant.TimeSelectConstants;
import com.gitee.pifeng.monitoring.ui.constant.WebResponseConstants;
import com.gitee.pifeng.monitoring.ui.core.CalculateDateTime;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
     * 服务器数据访问对象
     */
    @Autowired
    private IMonitorServerDao monitorServerDao;

    /**
     * 服务器操作系统数据访问对象
     */
    @Autowired
    private IMonitorServerOsDao monitorServerOsDao;

    /**
     * 服务器CPU数据访问对象
     */
    @Autowired
    private IMonitorServerCpuDao monitorServerCpuDao;

    /**
     * 服务器CPU历史记录数据访问对象
     */
    @Autowired
    private IMonitorServerCpuHistoryDao monitorServerCpuHistoryDao;

    /**
     * 服务器磁盘数据访问对象
     */
    @Autowired
    private IMonitorServerDiskDao monitorServerDiskDao;

    /**
     * 服务器磁盘历史记录数据访问对象
     */
    @Autowired
    private IMonitorServerDiskHistoryDao monitorServerDiskHistoryDao;

    /**
     * 服务器内存数据访问对象
     */
    @Autowired
    private IMonitorServerMemoryDao monitorServerMemoryDao;

    /**
     * 服务器内存历史记录数据访问对象
     */
    @Autowired
    private IMonitorServerMemoryHistoryDao monitorServerMemoryHistoryDao;

    /**
     * 服务器网卡数据访问对象
     */
    @Autowired
    private IMonitorServerNetcardDao monitorServerNetcardDao;

    /**
     * 服务器网卡历史记录数据访问对象
     */
    @Autowired
    private IMonitorServerNetcardHistoryDao monitorServerNetcardHistoryDao;

    /**
     * 服务器电池数据访问对象
     */
    @Autowired
    private IMonitorServerPowerSourcesDao monitorServerPowerSourcesDao;

    /**
     * 服务器传感器数据访问对象
     */
    @Autowired
    private IMonitorServerSensorsDao monitorServerSensorsDao;

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
        Map<String, Object> serverOsTypeMap = this.monitorServerOsDao.getServerOsTypeStatistics();
        // 服务器在线率统计
        Map<String, Object> serverOnlineRateMap = this.monitorServerDao.getServerOnlineRateStatistics();
        return HomeServerVo.builder()
                .serverSum(NumberUtil.parseInt(serverOsTypeMap.get("serverSum").toString()))
                .windowsSum(NumberUtil.parseInt(serverOsTypeMap.get("windowsSum").toString()))
                .linuxSum(NumberUtil.parseInt(serverOsTypeMap.get("linuxSum").toString()))
                .otherSum(NumberUtil.parseInt(serverOsTypeMap.get("otherSum").toString()))
                .serverOnLineSum(NumberUtil.parseInt(serverOnlineRateMap.get("serverOnLineSum").toString()))
                .serverOffLineSum(NumberUtil.parseInt(serverOnlineRateMap.get("serverOffLineSum").toString()))
                .serverOnLineRate(serverOnlineRateMap.get("serverOnLineRate").toString())
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
     * @param isOnline   状态
     * @return 简单分页模型
     * @author 皮锋
     * @custom.date 2020/9/4 12:38
     */
    @Override
    public Page<MonitorServerVo> getMonitorServerList(Long current, Long size, String ip, String serverName, String isOnline) {
        // 查询数据库
        IPage<MonitorServer> ipage = new Page<>(current, size);
        LambdaQueryWrapper<MonitorServer> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(ip)) {
            lambdaQueryWrapper.like(MonitorServer::getIp, ip);
        }
        if (StringUtils.isNotBlank(serverName)) {
            lambdaQueryWrapper.like(MonitorServer::getServerName, serverName);
        }
        if (StringUtils.isNotBlank(isOnline)) {
            lambdaQueryWrapper.eq(MonitorServer::getIsOnline, isOnline);
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
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
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
        // 服务器操作系统表
        LambdaUpdateWrapper<MonitorServerOs> serverOsLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverOsLambdaUpdateWrapper.in(MonitorServerOs::getIp, ips);
        this.monitorServerOsDao.delete(serverOsLambdaUpdateWrapper);
        // 服务器CPU历史记录表
        LambdaUpdateWrapper<MonitorServerCpuHistory> serverCpuHistoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverCpuHistoryLambdaUpdateWrapper.in(MonitorServerCpuHistory::getIp, ips);
        this.monitorServerCpuHistoryDao.delete(serverCpuHistoryLambdaUpdateWrapper);
        // 服务器CPU表
        LambdaUpdateWrapper<MonitorServerCpu> serverCpuLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverCpuLambdaUpdateWrapper.in(MonitorServerCpu::getIp, ips);
        this.monitorServerCpuDao.delete(serverCpuLambdaUpdateWrapper);
        // 服务器磁盘历史记录
        LambdaUpdateWrapper<MonitorServerDiskHistory> serverDiskHistoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverDiskHistoryLambdaUpdateWrapper.in(MonitorServerDiskHistory::getIp, ips);
        this.monitorServerDiskHistoryDao.delete(serverDiskHistoryLambdaUpdateWrapper);
        // 服务器磁盘表
        LambdaUpdateWrapper<MonitorServerDisk> serverDiskLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverDiskLambdaUpdateWrapper.in(MonitorServerDisk::getIp, ips);
        this.monitorServerDiskDao.delete(serverDiskLambdaUpdateWrapper);
        // 服务器内存历史记录表
        LambdaUpdateWrapper<MonitorServerMemoryHistory> serverMemoryHistoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverMemoryHistoryLambdaUpdateWrapper.in(MonitorServerMemoryHistory::getIp, ips);
        this.monitorServerMemoryHistoryDao.delete(serverMemoryHistoryLambdaUpdateWrapper);
        // 服务器内存表
        LambdaUpdateWrapper<MonitorServerMemory> serverMemoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverMemoryLambdaUpdateWrapper.in(MonitorServerMemory::getIp, ips);
        this.monitorServerMemoryDao.delete(serverMemoryLambdaUpdateWrapper);
        // 服务器网卡历史记录表
        LambdaUpdateWrapper<MonitorServerNetcardHistory> serverNetcardHistoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverNetcardHistoryLambdaUpdateWrapper.in(MonitorServerNetcardHistory::getIp, ips);
        this.monitorServerNetcardHistoryDao.delete(serverNetcardHistoryLambdaUpdateWrapper);
        // 服务器网卡表
        LambdaUpdateWrapper<MonitorServerNetcard> serverNetcardLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverNetcardLambdaUpdateWrapper.in(MonitorServerNetcard::getIp, ips);
        this.monitorServerNetcardDao.delete(serverNetcardLambdaUpdateWrapper);
        // 服务器电池表
        LambdaUpdateWrapper<MonitorServerPowerSources> serverPowerSourcesLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverPowerSourcesLambdaUpdateWrapper.in(MonitorServerPowerSources::getIp, ips);
        this.monitorServerPowerSourcesDao.delete(serverPowerSourcesLambdaUpdateWrapper);
        // 服务器传感器表
        LambdaUpdateWrapper<MonitorServerSensors> serverSensorsLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverSensorsLambdaUpdateWrapper.in(MonitorServerSensors::getIp, ips);
        this.monitorServerSensorsDao.delete(serverSensorsLambdaUpdateWrapper);
        // 服务器表
        LambdaUpdateWrapper<MonitorServer> serverLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverLambdaUpdateWrapper.in(MonitorServer::getIp, ips);
        this.monitorServerDao.delete(serverLambdaUpdateWrapper);
        return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
    }

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
    @Override
    public List<String> getNetcardAddress(String ip) {
        return this.monitorServerNetcardDao.getNetcardAddress(ip);
    }

    /**
     * <p>
     * 清理服务器监控历史数据
     * </p>
     *
     * @param id   服务器主键ID
     * @param ip   IP地址
     * @param time 时间
     * @return layUiAdmin响应对象：如果清理成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2021/7/21 8:57
     */
    @Override
    public LayUiAdminResultVo clearMonitorServerHistory(Long id, String ip, String time) {
        // 时间为空
        if (StringUtils.isBlank(time)) {
            return LayUiAdminResultVo.ok(WebResponseConstants.REQUIRED_IS_NULL);
        }
        CalculateDateTime calculateDateTime = new CalculateDateTime(time).invoke();
        // 清理时间
        Date clearTime = calculateDateTime.getStartTime();
        // 清理所有时间点的数据，相当于清理当前时间前的数据
        if (StringUtils.equalsIgnoreCase(time, TimeSelectConstants.ALL)) {
            clearTime = new Date();
        }
        // 服务器CPU历史记录表
        LambdaUpdateWrapper<MonitorServerCpuHistory> serverCpuHistoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverCpuHistoryLambdaUpdateWrapper.eq(MonitorServerCpuHistory::getId, id);
        serverCpuHistoryLambdaUpdateWrapper.eq(MonitorServerCpuHistory::getIp, ip);
        serverCpuHistoryLambdaUpdateWrapper.lt(MonitorServerCpuHistory::getInsertTime, clearTime);
        this.monitorServerCpuHistoryDao.delete(serverCpuHistoryLambdaUpdateWrapper);
        // 服务器磁盘历史记录
        LambdaUpdateWrapper<MonitorServerDiskHistory> serverDiskHistoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverDiskHistoryLambdaUpdateWrapper.eq(MonitorServerDiskHistory::getId, id);
        serverDiskHistoryLambdaUpdateWrapper.eq(MonitorServerDiskHistory::getIp, ip);
        serverDiskHistoryLambdaUpdateWrapper.lt(MonitorServerDiskHistory::getInsertTime, clearTime);
        this.monitorServerDiskHistoryDao.delete(serverDiskHistoryLambdaUpdateWrapper);
        // 服务器内存历史记录表
        LambdaUpdateWrapper<MonitorServerMemoryHistory> serverMemoryHistoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverMemoryHistoryLambdaUpdateWrapper.eq(MonitorServerMemoryHistory::getId, id);
        serverMemoryHistoryLambdaUpdateWrapper.eq(MonitorServerMemoryHistory::getIp, ip);
        serverMemoryHistoryLambdaUpdateWrapper.lt(MonitorServerMemoryHistory::getInsertTime, clearTime);
        this.monitorServerMemoryHistoryDao.delete(serverMemoryHistoryLambdaUpdateWrapper);
        // 服务器网卡历史记录表
        LambdaUpdateWrapper<MonitorServerNetcardHistory> serverNetcardHistoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverNetcardHistoryLambdaUpdateWrapper.eq(MonitorServerNetcardHistory::getId, id);
        serverNetcardHistoryLambdaUpdateWrapper.eq(MonitorServerNetcardHistory::getIp, ip);
        serverNetcardHistoryLambdaUpdateWrapper.lt(MonitorServerNetcardHistory::getInsertTime, clearTime);
        this.monitorServerNetcardHistoryDao.delete(serverNetcardHistoryLambdaUpdateWrapper);
        return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
    }

    /**
     * <p>
     * 编辑服务器信息
     * </p>
     *
     * @param monitorServerVo 服务器信息
     * @return 如果编辑成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2021/8/27 13:48
     */
    @Override
    public LayUiAdminResultVo editMonitorServer(MonitorServerVo monitorServerVo) {
        MonitorServer monitorServer = monitorServerVo.convertTo();
        LambdaUpdateWrapper<MonitorServer> serverLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverLambdaUpdateWrapper.eq(MonitorServer::getId, monitorServer.getId());
        serverLambdaUpdateWrapper.eq(MonitorServer::getIp, monitorServer.getIp());
        this.monitorServerDao.update(monitorServer, serverLambdaUpdateWrapper);
        return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
    }

}
