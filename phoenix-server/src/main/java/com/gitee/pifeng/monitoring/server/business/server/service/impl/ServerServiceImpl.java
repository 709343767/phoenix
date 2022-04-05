package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.constant.ResultMsgConstants;
import com.gitee.pifeng.monitoring.common.constant.ZeroOrOneConstants;
import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.common.domain.server.*;
import com.gitee.pifeng.monitoring.common.dto.ServerPackage;
import com.gitee.pifeng.monitoring.server.business.server.dao.*;
import com.gitee.pifeng.monitoring.server.business.server.entity.*;
import com.gitee.pifeng.monitoring.server.business.server.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务器信息服务层接口实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/23 15:23
 */
@Service
public class ServerServiceImpl extends ServiceImpl<IMonitorServerDao, MonitorServer> implements IServerService {

    /**
     * 服务器操作系统信息服务层接口
     */
    @Autowired
    private IServerOsService serverOsService;

    /**
     * 服务器内存信息服务层接口
     */
    @Autowired
    private IServerMemoryService serverMemoryService;

    /**
     * 服务器内存历史记录信息服务层接口
     */
    @Autowired
    private IServerMemoryHistoryService serverMemoryHistoryService;

    /**
     * 服务器CPU信息服务层接口
     */
    @Autowired
    private IServerCpuService serverCpuService;

    /**
     * 服务器CPU历史记录信息服务层接口
     */
    @Autowired
    private IServerCpuHistoryService serverCpuHistoryService;

    /**
     * 服务器网卡信息服务层接口
     */
    @Autowired
    private IServerNetcardService serverNetcardService;

    /**
     * 服务器网卡历史记录信息服务层接口
     */
    @Autowired
    private IServerNetcardHistoryService serverNetcardHistoryService;

    /**
     * 服务器磁盘信息服务层接口
     */
    @Autowired
    private IServerDiskService serverDiskService;

    /**
     * 服务器磁盘历史记录服务层接口
     */
    @Autowired
    private IServerDiskHistoryService serverDiskHistoryService;

    /**
     * 服务器电池信息服务层接口
     */
    @Autowired
    private IServerPowerSourcesService serverPowerSourcesService;

    /**
     * 服务器传感器信息服务层接口
     */
    @Autowired
    private IServerSensorsService serverSensorsService;

    /**
     * 服务器进程信息服务层接口
     */
    @Autowired
    private IServerProcessService serverProcessService;

    /**
     * 服务器进程历史记录信息服务层接口
     */
    @Autowired
    private IServerProcessHistoryService serverProcessHistoryService;

    /**
     * 服务器进程历史记录信息数据访问对象
     */
    @Autowired
    private IMonitorServerProcessHistoryDao monitorServerProcessHistoryDao;

    /**
     * 服务器CPU历史记录数据访问对象
     */
    @Autowired
    private IMonitorServerCpuHistoryDao monitorServerCpuHistoryDao;

    /**
     * 服务器磁盘历史记录数据访问对象
     */
    @Autowired
    private IMonitorServerDiskHistoryDao monitorServerDiskHistoryDao;

    /**
     * 服务器内存历史记录数据访问对象
     */
    @Autowired
    private IMonitorServerMemoryHistoryDao monitorServerMemoryHistoryDao;

    /**
     * 服务器网卡历史记录数据访问对象
     */
    @Autowired
    private IMonitorServerNetcardHistoryDao monitorServerNetcardHistoryDao;

    /**
     * <p>
     * 处理服务器信息包
     * </p>
     *
     * @param serverPackage 服务器信息包
     * @return {@link Result}
     * @author 皮锋
     * @custom.date 2020/3/23 15:29
     */
    //@Transactional(rollbackFor = Throwable.class)
    @Override
    @Retryable
    public Result dealServerPackage(ServerPackage serverPackage) {
        // 把服务器信息添加或更新到数据库
        this.operateServer(serverPackage);
        // 把服务器操作系统信息添加或更新到数据库
        this.operateServerOs(serverPackage);
        // 把服务器内存信息添加或更新到数据库
        this.operateServerMemory(serverPackage);
        // 把服务器内存历史记录添加到数据库
        this.operateServerMemoryHistory(serverPackage);
        // 把服务器CPU信息添加或更新到数据库
        this.operateServerCpu(serverPackage);
        // 把服务器CPU历史记录添加到数据库
        this.operateServerCpuHistory(serverPackage);
        // 把服务器网卡信息添加或更新到数据库
        this.operateServerNetcard(serverPackage);
        // 把服务器网卡历史记录添加到数据库
        this.operateServerNetcardHistory(serverPackage);
        // 把服务器磁盘信息添加或更新到数据库
        this.operateServerDisk(serverPackage);
        // 把服务器磁盘历史记录添加到数据库
        this.operateServerDiskHistory(serverPackage);
        // 把服务器电池信息添加或更新到数据库
        this.operateServerPowerSources(serverPackage);
        // 把服务器传感器信息添加或更新到数据库
        this.operateServerSensors(serverPackage);
        // 把服务器进程信息添加或更新到数据库
        this.operateServerProcess(serverPackage);
        // 把服务器进程历史记录添加到数据库
        this.operateServerProcessHistory(serverPackage);
        // 返回结果
        return Result.builder().isSuccess(true).msg(ResultMsgConstants.SUCCESS).build();
    }

    /**
     * <p>
     * 把服务器信息添加或更新到数据库
     * </p>
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2021/1/21 13:21
     */
    private void operateServer(ServerPackage serverPackage) {
        // IP地址
        String ip = serverPackage.getIp();
        // 查询数据库中是否有此IP的服务器
        LambdaQueryWrapper<MonitorServer> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MonitorServer::getIp, ip);
        int selectCountDb = this.count(lambdaQueryWrapper);
        // 封装对象
        MonitorServer monitorServer = new MonitorServer();
        monitorServer.setIp(ip);
        monitorServer.setServerName(serverPackage.getComputerName());
        monitorServer.setConnFrequency((int) serverPackage.getRate());
        // 没有
        if (selectCountDb == 0) {
            monitorServer.setInsertTime(new Date());
            monitorServer.setOfflineCount(0);
            this.save(monitorServer);
        }
        // 有
        else {
            monitorServer.setUpdateTime(new Date());
            LambdaUpdateWrapper<MonitorServer> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(MonitorServer::getIp, ip);
            this.update(monitorServer, lambdaUpdateWrapper);
        }
    }

    /**
     * <p>
     * 把服务器进程信息添加或更新到数据库
     * </p>
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2021/9/15 14:49
     */
    private void operateServerProcess(ServerPackage serverPackage) {
        // IP地址
        String ip = serverPackage.getIp();
        ProcessDomain processDomain = serverPackage.getServer().getProcessDomain();
        List<ProcessDomain.ProcessInfoDomain> processInfoList = processDomain.getProcessInfoList();
        // 先删除此服务器对应的进程信息
        LambdaUpdateWrapper<MonitorServerProcess> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(MonitorServerProcess::getIp, ip);
        this.serverProcessService.remove(lambdaUpdateWrapper);
        // 循环所有进程信息
        List<MonitorServerProcess> insertMonitorServerProcesses = new ArrayList<>();
        for (ProcessDomain.ProcessInfoDomain processInfo : processInfoList) {
            MonitorServerProcess monitorServerProcess = MonitorServerProcess.builder().build();
            monitorServerProcess.setIp(ip);
            monitorServerProcess.setProcessId(processInfo.getProcessId());
            monitorServerProcess.setName(processInfo.getName());
            monitorServerProcess.setPath(processInfo.getPath());
            monitorServerProcess.setCommandLine(processInfo.getCommandLine());
            monitorServerProcess.setCurrentWorkingDirectory(processInfo.getCurrentWorkingDirectory());
            monitorServerProcess.setUser(processInfo.getUser());
            monitorServerProcess.setState(processInfo.getState());
            monitorServerProcess.setUpTime(processInfo.getUpTime());
            monitorServerProcess.setStartTime(processInfo.getStartTime());
            monitorServerProcess.setCpuLoadCumulative(processInfo.getCpuLoadCumulative());
            monitorServerProcess.setBitness(processInfo.getBitness());
            monitorServerProcess.setMemorySize(processInfo.getMemorySize());
            monitorServerProcess.setInsertTime(serverPackage.getDateTime());
            monitorServerProcess.setUpdateTime(serverPackage.getDateTime());
            insertMonitorServerProcesses.add(monitorServerProcess);
        }
        this.serverProcessService.saveBatch(insertMonitorServerProcesses);
    }

    /**
     * <p>
     * 把服务器进程历史记录添加到数据库
     * </p>
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2021/9/15 15:04
     */
    private void operateServerProcessHistory(ServerPackage serverPackage) {
        // IP地址
        String ip = serverPackage.getIp();
        ProcessDomain processDomain = serverPackage.getServer().getProcessDomain();
        int processNum = processDomain.getProcessNum();
        // 添加记录到数据库
        MonitorServerProcessHistory monitorServerProcessHistory = MonitorServerProcessHistory.builder().build();
        monitorServerProcessHistory.setIp(ip);
        monitorServerProcessHistory.setProcessNum(processNum);
        monitorServerProcessHistory.setInsertTime(serverPackage.getDateTime());
        monitorServerProcessHistory.setUpdateTime(serverPackage.getDateTime());
        this.serverProcessHistoryService.save(monitorServerProcessHistory);
    }

    /**
     * <p>
     * 把服务器传感器信息添加或更新到数据库
     * </p>
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2021/1/15 20:43
     */
    private void operateServerSensors(ServerPackage serverPackage) {
        // IP地址
        String ip = serverPackage.getIp();
        SensorsDomain sensorsDomain = serverPackage.getServer().getSensorsDomain();
        // 查询数据库中是否有此IP的传感器
        LambdaQueryWrapper<MonitorServerSensors> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MonitorServerSensors::getIp, ip);
        int selectCountDb = this.serverSensorsService.count(lambdaQueryWrapper);
        // 封装对象
        MonitorServerSensors monitorServerSensors = new MonitorServerSensors();
        monitorServerSensors.setIp(ip);
        monitorServerSensors.setCpuTemperature(sensorsDomain.getCpuTemperature());
        monitorServerSensors.setCpuVoltage(sensorsDomain.getCpuVoltage());
        List<SensorsDomain.FanSpeedDomain> fanSpeedDomainList = sensorsDomain.getFanSpeedDomainList();
        if (CollectionUtils.isNotEmpty(fanSpeedDomainList)) {
            monitorServerSensors.setFanSpeed(fanSpeedDomainList.stream().map(SensorsDomain.FanSpeedDomain::getFanSpeed).collect(Collectors.joining(";")));
        }
        // 没有
        if (selectCountDb == 0) {
            monitorServerSensors.setInsertTime(serverPackage.getDateTime());
            this.serverSensorsService.save(monitorServerSensors);
        }
        // 有
        else {
            monitorServerSensors.setUpdateTime(serverPackage.getDateTime());
            LambdaUpdateWrapper<MonitorServerSensors> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(MonitorServerSensors::getIp, ip);
            this.serverSensorsService.update(monitorServerSensors, lambdaUpdateWrapper);
        }
    }

    /**
     * <p>
     * 把服务器电池信息添加或更新到数据库
     * </p>
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2021/1/15 20:42
     */
    private void operateServerPowerSources(ServerPackage serverPackage) {
        // IP地址
        String ip = serverPackage.getIp();
        // 电池信息
        PowerSourcesDomain powerSourcesDomain = serverPackage.getServer().getPowerSourcesDomain();
        List<PowerSourcesDomain.PowerSourceDomain> powerSourceDomains = powerSourcesDomain.getPowerSourceDomainList();
        // 要添加的电池信息
        List<MonitorServerPowerSources> saveMonitorServerPowerSources = new ArrayList<>();
        for (int i = 0; i < powerSourceDomains.size(); i++) {
            PowerSourcesDomain.PowerSourceDomain powerSourceDomain = powerSourceDomains.get(i);
            // 查询数据库中是否有此IP的电池信息
            LambdaQueryWrapper<MonitorServerPowerSources> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(MonitorServerPowerSources::getIp, ip);
            lambdaQueryWrapper.eq(MonitorServerPowerSources::getPowerSourcesNo, i + 1);
            int selectCountDb = this.serverPowerSourcesService.count(lambdaQueryWrapper);
            // 封装对象
            MonitorServerPowerSources monitorServerPowerSources = new MonitorServerPowerSources();
            monitorServerPowerSources.setIp(ip);
            monitorServerPowerSources.setPowerSourcesNo(i + 1);
            monitorServerPowerSources.setName(powerSourceDomain.getName());
            monitorServerPowerSources.setDeviceName(powerSourceDomain.getDeviceName());
            monitorServerPowerSources.setRemainingCapacityPercent(powerSourceDomain.getRemainingCapacityPercent());
            monitorServerPowerSources.setTimeRemainingEstimated(powerSourceDomain.getTimeRemainingEstimated());
            monitorServerPowerSources.setTimeRemainingInstant(powerSourceDomain.getTimeRemainingInstant());
            monitorServerPowerSources.setPowerUsageRate(powerSourceDomain.getPowerUsageRate());
            monitorServerPowerSources.setVoltage(powerSourceDomain.getVoltage());
            monitorServerPowerSources.setAmperage(powerSourceDomain.getAmperage());
            monitorServerPowerSources.setIsPowerOnLine(powerSourceDomain.isPowerOnLine() ? ZeroOrOneConstants.ONE : ZeroOrOneConstants.ZERO);
            monitorServerPowerSources.setIsCharging(powerSourceDomain.isCharging() ? ZeroOrOneConstants.ONE : ZeroOrOneConstants.ZERO);
            monitorServerPowerSources.setIsDischarging(powerSourceDomain.isDischarging() ? ZeroOrOneConstants.ONE : ZeroOrOneConstants.ZERO);
            monitorServerPowerSources.setCurrentCapacity(powerSourceDomain.getCurrentCapacity());
            monitorServerPowerSources.setMaxCapacity(powerSourceDomain.getMaxCapacity());
            monitorServerPowerSources.setDesignCapacity(powerSourceDomain.getDesignCapacity());
            monitorServerPowerSources.setCycleCount(powerSourceDomain.getCycleCount());
            monitorServerPowerSources.setChemistry(powerSourceDomain.getChemistry());
            monitorServerPowerSources.setManufactureDate(powerSourceDomain.getManufactureDate());
            monitorServerPowerSources.setManufacturer(powerSourceDomain.getManufacturer());
            monitorServerPowerSources.setSerialNumber(powerSourceDomain.getSerialNumber());
            monitorServerPowerSources.setTemperature(powerSourceDomain.getTemperature());
            // 没有
            if (selectCountDb == 0) {
                monitorServerPowerSources.setInsertTime(serverPackage.getDateTime());
                saveMonitorServerPowerSources.add(monitorServerPowerSources);
            }
            // 有
            else {
                monitorServerPowerSources.setUpdateTime(serverPackage.getDateTime());
                LambdaUpdateWrapper<MonitorServerPowerSources> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(MonitorServerPowerSources::getIp, ip);
                lambdaUpdateWrapper.eq(MonitorServerPowerSources::getPowerSourcesNo, i + 1);
                this.serverPowerSourcesService.update(monitorServerPowerSources, lambdaUpdateWrapper);
            }
        }
        // 有要添加的电池信息
        if (CollectionUtils.isNotEmpty(saveMonitorServerPowerSources)) {
            this.serverPowerSourcesService.saveBatch(saveMonitorServerPowerSources);
        }
    }

    /**
     * <p>
     * 把服务器磁盘信息添加或更新到数据库
     * </p>
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2021/1/25 9:56
     */
    private void operateServerDisk(ServerPackage serverPackage) {
        // IP地址
        String ip = serverPackage.getIp();
        // 磁盘信息
        DiskDomain diskDomain = serverPackage.getServer().getDiskDomain();
        List<DiskDomain.DiskInfoDomain> diskInfoDomains = diskDomain.getDiskInfoList();
        // 要添加的磁盘信息
        List<MonitorServerDisk> saveMonitorServerDisk = new ArrayList<>();
        for (int i = 0; i < diskInfoDomains.size(); i++) {
            DiskDomain.DiskInfoDomain diskInfoDomain = diskInfoDomains.get(i);
            // 查询数据库中有没有此IP和磁盘的磁盘信息
            LambdaQueryWrapper<MonitorServerDisk> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(MonitorServerDisk::getIp, ip);
            lambdaQueryWrapper.eq(MonitorServerDisk::getDiskNo, i + 1);
            int selectCountDb = this.serverDiskService.count(lambdaQueryWrapper);
            // 封装对象
            MonitorServerDisk monitorServerDisk = new MonitorServerDisk();
            monitorServerDisk.setIp(ip);
            monitorServerDisk.setDiskNo(i + 1);
            monitorServerDisk.setDevName(diskInfoDomain.getDevName());
            monitorServerDisk.setDirName(diskInfoDomain.getDirName());
            monitorServerDisk.setTypeName(diskInfoDomain.getTypeName());
            monitorServerDisk.setSysTypeName(diskInfoDomain.getSysTypeName());
            monitorServerDisk.setAvail(diskInfoDomain.getAvail());
            monitorServerDisk.setFree(diskInfoDomain.getFree());
            monitorServerDisk.setTotal(diskInfoDomain.getTotal());
            monitorServerDisk.setUsed(diskInfoDomain.getUsed());
            monitorServerDisk.setUsePercent(diskInfoDomain.getUsePercent());
            // 没有
            if (selectCountDb == 0) {
                monitorServerDisk.setInsertTime(serverPackage.getDateTime());
                saveMonitorServerDisk.add(monitorServerDisk);
            }
            // 有
            else {
                monitorServerDisk.setUpdateTime(serverPackage.getDateTime());
                LambdaUpdateWrapper<MonitorServerDisk> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(MonitorServerDisk::getIp, ip);
                lambdaUpdateWrapper.eq(MonitorServerDisk::getDiskNo, i + 1);
                this.serverDiskService.update(monitorServerDisk, lambdaUpdateWrapper);
            }
        }
        // 有要添加的磁盘信息
        if (CollectionUtils.isNotEmpty(saveMonitorServerDisk)) {
            this.serverDiskService.saveBatch(saveMonitorServerDisk);
        }
    }

    /**
     * <p>
     * 把服务器磁盘历史记录添加到数据库
     * </p>
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2020/5/12 10:29
     */
    private void operateServerDiskHistory(ServerPackage serverPackage) {
        // IP地址
        String ip = serverPackage.getIp();
        // 磁盘信息
        DiskDomain diskDomain = serverPackage.getServer().getDiskDomain();
        List<DiskDomain.DiskInfoDomain> diskInfoDomains = diskDomain.getDiskInfoList();
        // 要添加的磁盘信息
        List<MonitorServerDiskHistory> saveMonitorServerDiskHistory = new ArrayList<>();
        for (int i = 0; i < diskInfoDomains.size(); i++) {
            DiskDomain.DiskInfoDomain diskInfoDomain = diskInfoDomains.get(i);
            // 封装对象
            MonitorServerDiskHistory monitorServerDiskHistory = new MonitorServerDiskHistory();
            monitorServerDiskHistory.setIp(ip);
            monitorServerDiskHistory.setDiskNo(i + 1);
            monitorServerDiskHistory.setDevName(diskInfoDomain.getDevName());
            monitorServerDiskHistory.setDirName(diskInfoDomain.getDirName());
            monitorServerDiskHistory.setTypeName(diskInfoDomain.getTypeName());
            monitorServerDiskHistory.setSysTypeName(diskInfoDomain.getSysTypeName());
            monitorServerDiskHistory.setAvail(diskInfoDomain.getAvail());
            monitorServerDiskHistory.setFree(diskInfoDomain.getFree());
            monitorServerDiskHistory.setTotal(diskInfoDomain.getTotal());
            monitorServerDiskHistory.setUsed(diskInfoDomain.getUsed());
            monitorServerDiskHistory.setUsePercent(diskInfoDomain.getUsePercent());
            monitorServerDiskHistory.setInsertTime(serverPackage.getDateTime());
            monitorServerDiskHistory.setUpdateTime(serverPackage.getDateTime());
            saveMonitorServerDiskHistory.add(monitorServerDiskHistory);
        }
        this.serverDiskHistoryService.saveBatch(saveMonitorServerDiskHistory);
    }

    /**
     * <p>
     * 把服务器网卡信息添加或更新到数据库
     * </p>
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2021/1/26 11:21
     */
    private void operateServerNetcard(ServerPackage serverPackage) {
        // IP地址
        String ip = serverPackage.getIp();
        // 网卡信息
        NetDomain netDomain = serverPackage.getServer().getNetDomain();
        // 设置网卡信息
        List<NetDomain.NetInterfaceDomain> netInterfaceDomains = netDomain.getNetList();
        // 要添加的网卡信息
        List<MonitorServerNetcard> saveMonitorServerNetcard = new ArrayList<>();
        for (int i = 0; i < netInterfaceDomains.size(); i++) {
            NetDomain.NetInterfaceDomain netInterfaceDomain = netInterfaceDomains.get(i);
            // 查询数据库中有没有此IP和网卡的网卡信息
            LambdaQueryWrapper<MonitorServerNetcard> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(MonitorServerNetcard::getIp, ip);
            lambdaQueryWrapper.eq(MonitorServerNetcard::getNetNo, i + 1);
            int selectCountDb = this.serverNetcardService.count(lambdaQueryWrapper);
            // 封装对象
            MonitorServerNetcard monitorServerNetcard = new MonitorServerNetcard();
            monitorServerNetcard.setIp(ip);
            monitorServerNetcard.setNetNo(i + 1);
            // 网卡配置信息
            monitorServerNetcard.setAddress(netInterfaceDomain.getAddress());
            monitorServerNetcard.setBroadcast(netInterfaceDomain.getBroadcast());
            monitorServerNetcard.setMask(netInterfaceDomain.getMask());
            monitorServerNetcard.setName(netInterfaceDomain.getName());
            monitorServerNetcard.setType(netInterfaceDomain.getType());
            monitorServerNetcard.setHwAddr(netInterfaceDomain.getHwAddr());
            monitorServerNetcard.setDescription(netInterfaceDomain.getDescription());
            // 网卡状态信息
            monitorServerNetcard.setRxBytes(netInterfaceDomain.getRxBytes());
            monitorServerNetcard.setRxDropped(netInterfaceDomain.getRxDropped());
            monitorServerNetcard.setRxErrors(netInterfaceDomain.getRxErrors());
            monitorServerNetcard.setRxPackets(netInterfaceDomain.getRxPackets());
            monitorServerNetcard.setTxBytes(netInterfaceDomain.getTxBytes());
            monitorServerNetcard.setTxDropped(netInterfaceDomain.getTxDropped());
            monitorServerNetcard.setTxErrors(netInterfaceDomain.getTxErrors());
            monitorServerNetcard.setTxPackets(netInterfaceDomain.getTxPackets());
            // 网速
            monitorServerNetcard.setDownloadBps(netInterfaceDomain.getDownloadBps());
            monitorServerNetcard.setUploadBps(netInterfaceDomain.getUploadBps());
            // 没有
            if (selectCountDb == 0) {
                monitorServerNetcard.setInsertTime(serverPackage.getDateTime());
                saveMonitorServerNetcard.add(monitorServerNetcard);
            }
            // 有
            else {
                monitorServerNetcard.setUpdateTime(serverPackage.getDateTime());
                LambdaUpdateWrapper<MonitorServerNetcard> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(MonitorServerNetcard::getIp, ip);
                lambdaUpdateWrapper.eq(MonitorServerNetcard::getNetNo, i + 1);
                this.serverNetcardService.update(monitorServerNetcard, lambdaUpdateWrapper);
            }
        }
        // 有要添加的网卡信息
        if (CollectionUtils.isNotEmpty(saveMonitorServerNetcard)) {
            this.serverNetcardService.saveBatch(saveMonitorServerNetcard);
        }
    }

    /**
     * <p>
     * 把服务器网卡历史记录添加到数据库
     * </p>
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2020/5/11 23:39
     */
    private void operateServerNetcardHistory(ServerPackage serverPackage) {
        // IP地址
        String ip = serverPackage.getIp();
        // 网卡信息
        NetDomain netDomain = serverPackage.getServer().getNetDomain();
        // 设置网卡信息
        List<NetDomain.NetInterfaceDomain> netInterfaceDomains = netDomain.getNetList();
        // 要添加的网卡信息
        List<MonitorServerNetcardHistory> saveMonitorServerNetcardHistory = new ArrayList<>();
        for (int i = 0; i < netInterfaceDomains.size(); i++) {
            NetDomain.NetInterfaceDomain netInterfaceDomain = netInterfaceDomains.get(i);
            // 封装对象
            MonitorServerNetcardHistory monitorServerNetcardHistory = new MonitorServerNetcardHistory();
            monitorServerNetcardHistory.setIp(ip);
            monitorServerNetcardHistory.setNetNo(i + 1);
            // 网卡配置信息
            monitorServerNetcardHistory.setAddress(netInterfaceDomain.getAddress());
            monitorServerNetcardHistory.setBroadcast(netInterfaceDomain.getBroadcast());
            monitorServerNetcardHistory.setMask(netInterfaceDomain.getMask());
            monitorServerNetcardHistory.setName(netInterfaceDomain.getName());
            monitorServerNetcardHistory.setType(netInterfaceDomain.getType());
            monitorServerNetcardHistory.setHwAddr(netInterfaceDomain.getHwAddr());
            monitorServerNetcardHistory.setDescription(netInterfaceDomain.getDescription());
            // 网卡状态信息
            monitorServerNetcardHistory.setRxBytes(netInterfaceDomain.getRxBytes());
            monitorServerNetcardHistory.setRxDropped(netInterfaceDomain.getRxDropped());
            monitorServerNetcardHistory.setRxErrors(netInterfaceDomain.getRxErrors());
            monitorServerNetcardHistory.setRxPackets(netInterfaceDomain.getRxPackets());
            monitorServerNetcardHistory.setTxBytes(netInterfaceDomain.getTxBytes());
            monitorServerNetcardHistory.setTxDropped(netInterfaceDomain.getTxDropped());
            monitorServerNetcardHistory.setTxErrors(netInterfaceDomain.getTxErrors());
            monitorServerNetcardHistory.setTxPackets(netInterfaceDomain.getTxPackets());
            // 网速
            monitorServerNetcardHistory.setDownloadBps(netInterfaceDomain.getDownloadBps());
            monitorServerNetcardHistory.setUploadBps(netInterfaceDomain.getUploadBps());
            // 时间
            monitorServerNetcardHistory.setInsertTime(serverPackage.getDateTime());
            monitorServerNetcardHistory.setUpdateTime(serverPackage.getDateTime());
            saveMonitorServerNetcardHistory.add(monitorServerNetcardHistory);
        }
        this.serverNetcardHistoryService.saveBatch(saveMonitorServerNetcardHistory);
    }

    /**
     * <p>
     * 把服务器CPU信息添加或更新到数据库
     * </p>
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2021/1/25 9:48
     */
    private void operateServerCpu(ServerPackage serverPackage) {
        // IP地址
        String ip = serverPackage.getIp();
        // Cpu信息
        CpuDomain cpuDomain = serverPackage.getServer().getCpuDomain();
        List<CpuDomain.CpuInfoDomain> cpuInfoDomains = cpuDomain.getCpuList();
        // 要添加的Cpu信息集合
        List<MonitorServerCpu> saveMonitorServerCpus = new ArrayList<>();
        for (int i = 0; i < cpuInfoDomains.size(); i++) {
            CpuDomain.CpuInfoDomain cpuInfoDomain = cpuInfoDomains.get(i);
            // 查询数据库中有没有此IP和此CPU的CPU信息
            LambdaQueryWrapper<MonitorServerCpu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(MonitorServerCpu::getIp, ip);
            lambdaQueryWrapper.eq(MonitorServerCpu::getCpuNo, i + 1);
            int selectCountDb = this.serverCpuService.count(lambdaQueryWrapper);
            // 封装对象
            MonitorServerCpu monitorServerCpu = new MonitorServerCpu();
            monitorServerCpu.setIp(ip);
            monitorServerCpu.setCpuNo(i + 1);
            monitorServerCpu.setCpuMhz(cpuInfoDomain.getCpuMhz());
            monitorServerCpu.setCpuVendor(cpuInfoDomain.getCpuVendor());
            monitorServerCpu.setCpuModel(cpuInfoDomain.getCpuModel());
            monitorServerCpu.setCpuUser(cpuInfoDomain.getCpuUser());
            monitorServerCpu.setCpuSys(cpuInfoDomain.getCpuSys());
            monitorServerCpu.setCpuNice(cpuInfoDomain.getCpuNice());
            monitorServerCpu.setCpuWait(cpuInfoDomain.getCpuWait());
            monitorServerCpu.setCpuCombined(cpuInfoDomain.getCpuCombined());
            monitorServerCpu.setCpuIdle(cpuInfoDomain.getCpuIdle());
            // 没有
            if (selectCountDb == 0) {
                monitorServerCpu.setInsertTime(serverPackage.getDateTime());
                saveMonitorServerCpus.add(monitorServerCpu);
            }
            // 有
            else {
                monitorServerCpu.setUpdateTime(serverPackage.getDateTime());
                LambdaUpdateWrapper<MonitorServerCpu> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(MonitorServerCpu::getIp, ip);
                lambdaUpdateWrapper.eq(MonitorServerCpu::getCpuNo, i + 1);
                this.serverCpuService.update(monitorServerCpu, lambdaUpdateWrapper);
            }
        }
        // 有要新增的Cpu
        if (CollectionUtils.isNotEmpty(saveMonitorServerCpus)) {
            this.serverCpuService.saveBatch(saveMonitorServerCpus);
        }
    }

    /**
     * <p>
     * 把服务器CPU历史记录添加到数据库
     * </p>
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2020/5/11 17:19
     */
    private void operateServerCpuHistory(ServerPackage serverPackage) {
        // IP地址
        String ip = serverPackage.getIp();
        // Cpu信息
        CpuDomain cpuDomain = serverPackage.getServer().getCpuDomain();
        List<CpuDomain.CpuInfoDomain> cpuInfoDomains = cpuDomain.getCpuList();
        // 要添加的Cpu信息
        List<MonitorServerCpuHistory> saveMonitorServerCpuHistory = new ArrayList<>();
        for (int i = 0; i < cpuInfoDomains.size(); i++) {
            CpuDomain.CpuInfoDomain cpuInfoDomain = cpuInfoDomains.get(i);
            // 封装对象
            MonitorServerCpuHistory monitorServerCpuHistory = new MonitorServerCpuHistory();
            monitorServerCpuHistory.setIp(ip);
            monitorServerCpuHistory.setCpuNo(i + 1);
            monitorServerCpuHistory.setCpuMhz(cpuInfoDomain.getCpuMhz());
            monitorServerCpuHistory.setCpuVendor(cpuInfoDomain.getCpuVendor());
            monitorServerCpuHistory.setCpuModel(cpuInfoDomain.getCpuModel());
            monitorServerCpuHistory.setCpuUser(cpuInfoDomain.getCpuUser());
            monitorServerCpuHistory.setCpuSys(cpuInfoDomain.getCpuSys());
            monitorServerCpuHistory.setCpuNice(cpuInfoDomain.getCpuNice());
            monitorServerCpuHistory.setCpuWait(cpuInfoDomain.getCpuWait());
            monitorServerCpuHistory.setCpuCombined(cpuInfoDomain.getCpuCombined());
            monitorServerCpuHistory.setCpuIdle(cpuInfoDomain.getCpuIdle());
            monitorServerCpuHistory.setInsertTime(serverPackage.getDateTime());
            monitorServerCpuHistory.setUpdateTime(serverPackage.getDateTime());
            saveMonitorServerCpuHistory.add(monitorServerCpuHistory);
        }
        this.serverCpuHistoryService.saveBatch(saveMonitorServerCpuHistory);
    }

    /**
     * <p>
     * 把服务器内存信息添加或更新到数据库
     * </p>
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2021/1/25 9:38
     */
    private void operateServerMemory(ServerPackage serverPackage) {
        // IP地址
        String ip = serverPackage.getIp();
        // 内存信息
        MemoryDomain memoryDomain = serverPackage.getServer().getMemoryDomain();
        MemoryDomain.MenDomain menDomain = memoryDomain.getMenDomain();
        MemoryDomain.SwapDomain swapDomain = memoryDomain.getSwapDomain();
        // 判断数据库中是否有此IP的服务器内存
        LambdaQueryWrapper<MonitorServerMemory> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MonitorServerMemory::getIp, ip);
        int selectCountDb = this.serverMemoryService.count(lambdaQueryWrapper);
        // 封装对象
        MonitorServerMemory monitorServerMemory = new MonitorServerMemory();
        monitorServerMemory.setIp(ip);
        monitorServerMemory.setMenTotal(menDomain.getMemTotal());
        monitorServerMemory.setMenUsed(menDomain.getMemUsed());
        monitorServerMemory.setMenFree(menDomain.getMemFree());
        monitorServerMemory.setMenUsedPercent(menDomain.getMenUsedPercent());
        monitorServerMemory.setSwapTotal(swapDomain.getSwapTotal());
        monitorServerMemory.setSwapUsed(swapDomain.getSwapUsed());
        monitorServerMemory.setSwapFree(swapDomain.getSwapFree());
        monitorServerMemory.setSwapUsedPercent(swapDomain.getSwapUsedPercent());
        // 没有
        if (selectCountDb == 0) {
            monitorServerMemory.setInsertTime(serverPackage.getDateTime());
            this.serverMemoryService.save(monitorServerMemory);
        }
        // 有
        else {
            monitorServerMemory.setUpdateTime(serverPackage.getDateTime());
            LambdaUpdateWrapper<MonitorServerMemory> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(MonitorServerMemory::getIp, ip);
            this.serverMemoryService.update(monitorServerMemory, lambdaUpdateWrapper);
        }
    }

    /**
     * <p>
     * 把服务器内存历史记录添加到数据库
     * </p>
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2020/5/11 16:43
     */
    private void operateServerMemoryHistory(ServerPackage serverPackage) {
        // IP地址
        String ip = serverPackage.getIp();
        // 内存信息
        MemoryDomain memoryDomain = serverPackage.getServer().getMemoryDomain();
        MemoryDomain.MenDomain menDomain = memoryDomain.getMenDomain();
        MemoryDomain.SwapDomain swapDomain = memoryDomain.getSwapDomain();
        // 封装对象
        MonitorServerMemoryHistory monitorServerMemoryHistory = new MonitorServerMemoryHistory();
        monitorServerMemoryHistory.setIp(ip);
        monitorServerMemoryHistory.setMenTotal(menDomain.getMemTotal());
        monitorServerMemoryHistory.setMenUsed(menDomain.getMemUsed());
        monitorServerMemoryHistory.setMenFree(menDomain.getMemFree());
        monitorServerMemoryHistory.setMenUsedPercent(menDomain.getMenUsedPercent());
        monitorServerMemoryHistory.setSwapTotal(swapDomain.getSwapTotal());
        monitorServerMemoryHistory.setSwapUsed(swapDomain.getSwapUsed());
        monitorServerMemoryHistory.setSwapFree(swapDomain.getSwapFree());
        monitorServerMemoryHistory.setSwapUsedPercent(swapDomain.getSwapUsedPercent());
        monitorServerMemoryHistory.setInsertTime(serverPackage.getDateTime());
        monitorServerMemoryHistory.setUpdateTime(serverPackage.getDateTime());
        this.serverMemoryHistoryService.save(monitorServerMemoryHistory);
    }

    /**
     * <p>
     * 把服务器操作系统信息添加或更新到数据库
     * </p>
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2020/5/11 16:01
     */
    private void operateServerOs(ServerPackage serverPackage) {
        // IP地址
        String ip = serverPackage.getIp();
        // 操作系统信息
        OsDomain osDomain = serverPackage.getServer().getOsDomain();
        LambdaQueryWrapper<MonitorServerOs> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MonitorServerOs::getIp, ip);
        int selectCountDb = this.serverOsService.count(lambdaQueryWrapper);
        // 封装对象
        MonitorServerOs monitorServerOs = new MonitorServerOs();
        monitorServerOs.setIp(ip);
        monitorServerOs.setServerName(osDomain.getComputerName());
        monitorServerOs.setOsName(osDomain.getOsName());
        monitorServerOs.setOsArch(osDomain.getOsArch());
        monitorServerOs.setOsVersion(osDomain.getOsVersion());
        monitorServerOs.setOsTimeZone(osDomain.getOsTimeZone());
        monitorServerOs.setUserHome(osDomain.getUserHome());
        monitorServerOs.setUserName(osDomain.getUserName());
        // 没有
        if (selectCountDb == 0) {
            monitorServerOs.setInsertTime(serverPackage.getDateTime());
            this.serverOsService.save(monitorServerOs);
        }
        // 有
        else {
            monitorServerOs.setUpdateTime(serverPackage.getDateTime());
            LambdaUpdateWrapper<MonitorServerOs> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(MonitorServerOs::getIp, ip);
            this.serverOsService.update(monitorServerOs, lambdaUpdateWrapper);
        }
    }

    /**
     * <p>
     * 清理服务器历史记录
     * </p>
     *
     * @param historyTime 时间点，清理这个时间点以前的数据
     * @return 清理记录数
     * @author 皮锋
     * @custom.date 2021/12/9 20:59
     */
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public int clearHistoryData(Date historyTime) {
        LambdaUpdateWrapper<MonitorServerProcessHistory> serverProcessHistoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverProcessHistoryLambdaUpdateWrapper.le(MonitorServerProcessHistory::getInsertTime, historyTime);
        int deleteServerProcessHistoryNum = this.monitorServerProcessHistoryDao.delete(serverProcessHistoryLambdaUpdateWrapper);
        LambdaUpdateWrapper<MonitorServerCpuHistory> serverCpuHistoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverCpuHistoryLambdaUpdateWrapper.le(MonitorServerCpuHistory::getInsertTime, historyTime);
        int deleteServerCpuHistoryNum = this.monitorServerCpuHistoryDao.delete(serverCpuHistoryLambdaUpdateWrapper);
        LambdaUpdateWrapper<MonitorServerDiskHistory> serverDiskHistoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverDiskHistoryLambdaUpdateWrapper.le(MonitorServerDiskHistory::getInsertTime, historyTime);
        int deleteServerDiskHistoryNum = this.monitorServerDiskHistoryDao.delete(serverDiskHistoryLambdaUpdateWrapper);
        LambdaUpdateWrapper<MonitorServerMemoryHistory> serverMemoryHistoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverMemoryHistoryLambdaUpdateWrapper.le(MonitorServerMemoryHistory::getInsertTime, historyTime);
        int deleteServerMemoryHistoryNum = this.monitorServerMemoryHistoryDao.delete(serverMemoryHistoryLambdaUpdateWrapper);
        LambdaUpdateWrapper<MonitorServerNetcardHistory> serverNetcardHistoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverNetcardHistoryLambdaUpdateWrapper.le(MonitorServerNetcardHistory::getInsertTime, historyTime);
        int deleteServerNetcardHistoryNum = this.monitorServerNetcardHistoryDao.delete(serverNetcardHistoryLambdaUpdateWrapper);
        return deleteServerProcessHistoryNum
                + deleteServerCpuHistoryNum
                + deleteServerDiskHistoryNum
                + deleteServerMemoryHistoryNum
                + deleteServerNetcardHistoryNum;
    }

}
