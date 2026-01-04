package com.gitee.pifeng.monitoring.ui.business.web.service.impl;

import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.constant.ZeroOrOneConstants;
import com.gitee.pifeng.monitoring.common.constant.monitortype.MonitorTypeEnums;
import com.gitee.pifeng.monitoring.common.util.DataSizeUtils;
import com.gitee.pifeng.monitoring.ui.business.web.dao.*;
import com.gitee.pifeng.monitoring.ui.business.web.entity.*;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorLinkService;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorRealtimeMonitoringService;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorServerService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.HomeServerVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorServerVo;
import com.gitee.pifeng.monitoring.ui.constant.TimeSelectConstants;
import com.gitee.pifeng.monitoring.ui.constant.WebResponseConstants;
import com.gitee.pifeng.monitoring.ui.core.CalculateDateTime;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
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
     * 链路 服务类
     */
    @Autowired
    private IMonitorLinkService monitorLinkService;

    /**
     * 实时监控服务类
     */
    @Autowired
    private IMonitorRealtimeMonitoringService monitorRealtimeMonitoringService;

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
     * 服务器GPU数据访问对象
     */
    @Autowired
    private IMonitorServerGpuDao monitorServerGpuDao;

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
     * 服务器进程信息数据访问对象
     */
    @Autowired
    private IMonitorServerProcessDao monitorServerProcessDao;

    /**
     * 服务器进程历史记录信息数据访问对象
     */
    @Autowired
    private IMonitorServerProcessHistoryDao monitorServerProcessHistoryDao;

    /**
     * 服务器平均负载数据访问对象
     */
    @Autowired
    private IMonitorServerLoadAverageDao monitorServerLoadAverageDao;

    /**
     * 服务器平均负载历史记录数据访问对象
     */
    @Autowired
    private IMonitorServerLoadAverageHistoryDao monitorServerLoadAverageHistoryDao;

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
                .serverUnknownLineSum(NumberUtil.parseInt(serverOnlineRateMap.get("serverUnknownLineSum").toString()))
                .serverOnLineRate(NumberUtil.round(serverOnlineRateMap.get("serverOnLineRate").toString(), 2).toString())
                .build();
    }

    /**
     * <p>
     * 获取服务器列表
     * </p>
     *
     * @param current         当前页
     * @param size            每页显示条数
     * @param ip              IP
     * @param serverName      服务器名
     * @param isOnline        状态
     * @param monitorEnv      监控环境
     * @param monitorGroup    监控分组
     * @param osName          系统
     * @param serverSummary   描述
     * @param isEnableMonitor 是否开启监控（0：不开启监控；1：开启监控）
     * @param isEnableAlarm   是否开启告警（0：不开启告警；1：开启告警）
     * @return 简单分页模型
     * @author 皮锋
     * @custom.date 2020/9/4 12:38
     */
    @Override
    public Page<MonitorServerVo> getMonitorServerList(Long current, Long size, String ip, String serverName,
                                                      String isOnline, String monitorEnv, String monitorGroup,
                                                      String osName, String serverSummary, String isEnableMonitor,
                                                      String isEnableAlarm) {
        // 查询数据库
        IPage<MonitorServer> ipage = new Page<>(current, size);
        // 查询条件
        Map<String, Object> criteria = new HashMap<>(16);
        criteria.put("ip", ip);
        criteria.put("serverName", serverName);
        criteria.put("isOnline", isOnline);
        criteria.put("monitorEnv", monitorEnv);
        criteria.put("monitorGroup", monitorGroup);
        criteria.put("osName", osName);
        criteria.put("isEnableMonitor", isEnableMonitor);
        criteria.put("isEnableAlarm", isEnableAlarm);
        IPage<MonitorServerVo> monitorServerPage = this.monitorServerDao.getMonitorServerList(ipage, criteria);
        List<MonitorServerVo> monitorServerVos = monitorServerPage.getRecords();
        // 当前时间
        Date currentDateTime = new Date();
        for (MonitorServerVo monitorServerVo : monitorServerVos) {
            // 下行带宽
            String downloadBps = monitorServerVo.getDownloadBps();
            // 上行带宽
            String uploadBps = monitorServerVo.getUploadBps();
            if (StringUtils.isNoneBlank(downloadBps)) {
                String format = DataSizeUtils.format(Double.parseDouble(downloadBps));
                monitorServerVo.setDownloadBps(ZeroOrOneConstants.ZERO.equals(format) ? ZeroOrOneConstants.ZERO : format + "/s");
            }
            if (StringUtils.isNoneBlank(uploadBps)) {
                String format = DataSizeUtils.format(Double.parseDouble(uploadBps));
                monitorServerVo.setUploadBps(ZeroOrOneConstants.ZERO.equals(format) ? ZeroOrOneConstants.ZERO : format + "/s");
            }
            Date updateTime = monitorServerVo.getUpdateTime();
            // 最后心跳时间
            String finalHeartbeat = updateTime != null ? DateUtil.formatBetween(currentDateTime, updateTime, BetweenFormatter.Level.SECOND) + "前" : "";
            monitorServerVo.setFinalHeartbeat(finalHeartbeat);
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
    @Retryable
    @Transactional(rollbackFor = Throwable.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public LayUiAdminResultVo deleteMonitorServer(List<MonitorServerVo> monitorServerVos) {
        List<String> ips = Lists.newArrayList();
        List<String> ids = Lists.newArrayList();
        for (MonitorServerVo monitorServerVo : monitorServerVos) {
            ips.add(monitorServerVo.getIp());
            Long id = monitorServerVo.getId();
            if (id != null) {
                ids.add(String.valueOf(id));
            }
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
        // 服务器GPU表
        LambdaUpdateWrapper<MonitorServerGpu> serverGpuLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverGpuLambdaUpdateWrapper.in(MonitorServerGpu::getIp, ips);
        this.monitorServerGpuDao.delete(serverGpuLambdaUpdateWrapper);
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
        // 服务器进程表
        LambdaUpdateWrapper<MonitorServerProcess> serverProcessLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverProcessLambdaUpdateWrapper.in(MonitorServerProcess::getIp, ips);
        this.monitorServerProcessDao.delete(serverProcessLambdaUpdateWrapper);
        // 服务器进程历史记录表
        LambdaUpdateWrapper<MonitorServerProcessHistory> serverProcessHistoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverProcessHistoryLambdaUpdateWrapper.in(MonitorServerProcessHistory::getIp, ips);
        this.monitorServerProcessHistoryDao.delete(serverProcessHistoryLambdaUpdateWrapper);
        // 服务器平均负载历史记录表
        LambdaUpdateWrapper<MonitorServerLoadAverageHistory> serverLoadAverageHistoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverLoadAverageHistoryLambdaUpdateWrapper.in(MonitorServerLoadAverageHistory::getIp, ips);
        this.monitorServerLoadAverageHistoryDao.delete(serverLoadAverageHistoryLambdaUpdateWrapper);
        // 服务器平均负载表
        LambdaUpdateWrapper<MonitorServerLoadAverage> serverLoadAverageLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverLoadAverageLambdaUpdateWrapper.in(MonitorServerLoadAverage::getIp, ips);
        this.monitorServerLoadAverageDao.delete(serverLoadAverageLambdaUpdateWrapper);
        // 服务器表
        LambdaUpdateWrapper<MonitorServer> serverLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverLambdaUpdateWrapper.in(MonitorServer::getIp, ips);
        this.monitorServerDao.delete(serverLambdaUpdateWrapper);
        // 注意：删除服务器相关链路信息，这个不要忘记了
        this.monitorLinkService.deleteMonitorLinks(ips, MonitorTypeEnums.SERVER);
        // 注意：删除服务器相关实时监控信息，这个不要忘记了
        this.monitorRealtimeMonitoringService.delete(MonitorTypeEnums.SERVER, null, ids);
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
     * @param ip   IP地址
     * @param time 时间
     * @return layUiAdmin响应对象：如果清理成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2021/7/21 8:57
     */
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public LayUiAdminResultVo clearMonitorServerHistory(String ip, String time) {
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
        serverCpuHistoryLambdaUpdateWrapper.eq(MonitorServerCpuHistory::getIp, ip);
        serverCpuHistoryLambdaUpdateWrapper.lt(MonitorServerCpuHistory::getInsertTime, clearTime);
        this.monitorServerCpuHistoryDao.delete(serverCpuHistoryLambdaUpdateWrapper);
        // 服务器磁盘历史记录
        LambdaUpdateWrapper<MonitorServerDiskHistory> serverDiskHistoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverDiskHistoryLambdaUpdateWrapper.eq(MonitorServerDiskHistory::getIp, ip);
        serverDiskHistoryLambdaUpdateWrapper.lt(MonitorServerDiskHistory::getInsertTime, clearTime);
        this.monitorServerDiskHistoryDao.delete(serverDiskHistoryLambdaUpdateWrapper);
        // 服务器内存历史记录表
        LambdaUpdateWrapper<MonitorServerMemoryHistory> serverMemoryHistoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverMemoryHistoryLambdaUpdateWrapper.eq(MonitorServerMemoryHistory::getIp, ip);
        serverMemoryHistoryLambdaUpdateWrapper.lt(MonitorServerMemoryHistory::getInsertTime, clearTime);
        this.monitorServerMemoryHistoryDao.delete(serverMemoryHistoryLambdaUpdateWrapper);
        // 服务器网卡历史记录表
        LambdaUpdateWrapper<MonitorServerNetcardHistory> serverNetcardHistoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverNetcardHistoryLambdaUpdateWrapper.eq(MonitorServerNetcardHistory::getIp, ip);
        serverNetcardHistoryLambdaUpdateWrapper.lt(MonitorServerNetcardHistory::getInsertTime, clearTime);
        this.monitorServerNetcardHistoryDao.delete(serverNetcardHistoryLambdaUpdateWrapper);
        // 服务器进程历史记录表
        LambdaUpdateWrapper<MonitorServerProcessHistory> serverProcessHistoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverProcessHistoryLambdaUpdateWrapper.eq(MonitorServerProcessHistory::getIp, ip);
        serverProcessHistoryLambdaUpdateWrapper.lt(MonitorServerProcessHistory::getInsertTime, clearTime);
        this.monitorServerProcessHistoryDao.delete(serverProcessHistoryLambdaUpdateWrapper);
        // 服务器平均负载历史记录表
        LambdaUpdateWrapper<MonitorServerLoadAverageHistory> serverLoadAverageHistoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverLoadAverageHistoryLambdaUpdateWrapper.eq(MonitorServerLoadAverageHistory::getIp, ip);
        serverLoadAverageHistoryLambdaUpdateWrapper.lt(MonitorServerLoadAverageHistory::getInsertTime, clearTime);
        this.monitorServerLoadAverageHistoryDao.delete(serverLoadAverageHistoryLambdaUpdateWrapper);
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
        if (StringUtils.isBlank(monitorServer.getMonitorEnv())) {
            monitorServer.setMonitorEnv(null);
        }
        if (StringUtils.isBlank(monitorServer.getMonitorGroup())) {
            monitorServer.setMonitorGroup(null);
        }
        if (StringUtils.isBlank(monitorServer.getIsEnableMonitor())) {
            monitorServer.setIsEnableMonitor(ZeroOrOneConstants.ZERO);
        }
        if (StringUtils.isBlank(monitorServer.getIsEnableAlarm())) {
            monitorServer.setIsEnableAlarm(ZeroOrOneConstants.ZERO);
        }
        LambdaUpdateWrapper<MonitorServer> serverLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverLambdaUpdateWrapper.eq(MonitorServer::getId, monitorServer.getId());
        serverLambdaUpdateWrapper.eq(MonitorServer::getIp, monitorServer.getIp());
        this.monitorServerDao.update(monitorServer, serverLambdaUpdateWrapper);
        return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
    }

    /**
     * <p>
     * 设置是否开启监控（0：不开启监控；1：开启监控）
     * </p>
     *
     * @param id              主键ID
     * @param ip              IP地址
     * @param isEnableMonitor 是否开启监控（0：不开启监控；1：开启监控）
     * @return 如果编辑成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2024/12/10 21:20
     */
    @Override
    public LayUiAdminResultVo setIsEnableMonitor(Long id, String ip, String isEnableMonitor) {
        LambdaUpdateWrapper<MonitorServer> serverLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        // 设置更新条件
        serverLambdaUpdateWrapper.eq(MonitorServer::getId, id);
        serverLambdaUpdateWrapper.eq(MonitorServer::getIp, ip);
        // 设置更新字段
        serverLambdaUpdateWrapper.set(MonitorServer::getIsEnableMonitor, isEnableMonitor);
        // 如果不监控，状态改为未知
        if (StringUtils.equals(isEnableMonitor, ZeroOrOneConstants.ZERO)) {
            serverLambdaUpdateWrapper.set(MonitorServer::getIsOnline, null);
        }
        this.monitorServerDao.update(null, serverLambdaUpdateWrapper);
        return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
    }

    /**
     * <p>
     * 设置是否开启告警（0：不开启告警；1：开启告警）
     * </p>
     *
     * @param id            主键ID
     * @param ip            IP地址
     * @param isEnableAlarm 是否开启告警（0：不开启告警；1：开启告警）
     * @return 如果设置成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2024/12/10 21:37
     */
    @Override
    public LayUiAdminResultVo setIsEnableAlarm(Long id, String ip, String isEnableAlarm) {
        LambdaUpdateWrapper<MonitorServer> serverLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        // 设置更新条件
        serverLambdaUpdateWrapper.eq(MonitorServer::getId, id);
        serverLambdaUpdateWrapper.eq(MonitorServer::getIp, ip);
        // 设置更新字段
        serverLambdaUpdateWrapper.set(MonitorServer::getIsEnableAlarm, isEnableAlarm);
        this.monitorServerDao.update(null, serverLambdaUpdateWrapper);
        return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
    }

    /**
     * <p>
     * 根据条件获取服务器信息
     * </p>
     *
     * @param id 服务器主键ID
     * @param ip IP地址
     * @return MonitorServerVo 服务器信息
     * @author 皮锋
     * @custom.date 2021/8/27 14:32
     */
    @Override
    public MonitorServerVo getMonitorServerInfo(Long id, String ip) {
        LambdaQueryWrapper<MonitorServer> monitorServerLambdaQueryWrapper = new LambdaQueryWrapper<>();
        monitorServerLambdaQueryWrapper.eq(MonitorServer::getId, id);
        monitorServerLambdaQueryWrapper.eq(MonitorServer::getIp, ip);
        MonitorServer monitorServer = this.getOne(monitorServerLambdaQueryWrapper);
        return new MonitorServerVo().convertFor(monitorServer);
    }

    /**
     * <p>
     * 获取服务器信息(Map形式)
     * </p>
     *
     * @return 服务器信息
     * @author 皮锋
     * @custom.date 2022/12/21 14:29
     */
    @Override
    public Map<String, MonitorServerVo> getMonitorServer2Map() {
        List<MonitorServerVo> monitorServerVos = this.monitorServerDao.getMonitorServers();
        Map<String, MonitorServerVo> result = Maps.newHashMap();
        for (MonitorServerVo monitorServerVo : monitorServerVos) {
            String ip = monitorServerVo.getIp();
            // 下行带宽
            String downloadBps = monitorServerVo.getDownloadBps();
            // 上行带宽
            String uploadBps = monitorServerVo.getUploadBps();
            if (StringUtils.isNoneBlank(downloadBps)) {
                String format = DataSizeUtils.format(Double.parseDouble(downloadBps));
                monitorServerVo.setDownloadBps(ZeroOrOneConstants.ZERO.equals(format) ? ZeroOrOneConstants.ZERO : format + "/s");
            }
            if (StringUtils.isNoneBlank(uploadBps)) {
                String format = DataSizeUtils.format(Double.parseDouble(uploadBps));
                monitorServerVo.setUploadBps(ZeroOrOneConstants.ZERO.equals(format) ? ZeroOrOneConstants.ZERO : format + "/s");
            }
            result.put(ip, monitorServerVo);
        }
        return result;
    }

}
