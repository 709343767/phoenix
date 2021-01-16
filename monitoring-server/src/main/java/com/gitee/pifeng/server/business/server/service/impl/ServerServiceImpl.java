package com.gitee.pifeng.server.business.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.gitee.pifeng.common.constant.ResultMsgConstants;
import com.gitee.pifeng.common.constant.ZeroOrOneConstants;
import com.gitee.pifeng.common.domain.Result;
import com.gitee.pifeng.common.domain.server.*;
import com.gitee.pifeng.common.dto.ServerPackage;
import com.gitee.pifeng.server.business.server.dao.*;
import com.gitee.pifeng.server.business.server.entity.*;
import com.gitee.pifeng.server.business.server.service.IServerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Slf4j
@Service
public class ServerServiceImpl implements IServerService {

    /**
     * 服务器数据访问对象
     */
    @Autowired
    private IMonitorServerDao monitorServerDao;

    /**
     * 服务器内存数据访问对象
     */
    @Autowired
    private IMonitorServerMemoryDao monitorServerMemoryDao;

    /**
     * 服务器CPU数据访问对象
     */
    @Autowired
    private IMonitorServerCpuDao monitorServerCpuDao;

    /**
     * 服务器网卡数据访问对象
     */
    @Autowired
    private IMonitorServerNetcardDao monitorServerNetcardDao;

    /**
     * 服务器磁盘数据访问对象
     */
    @Autowired
    private IMonitorServerDiskDao monitorServerDiskDao;

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
     * 处理服务器信息包
     * </p>
     *
     * @param serverPackage 服务器信息包
     * @return {@link Result}
     * @author 皮锋
     * @custom.date 2020/3/23 15:29
     */
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public Result dealServerPackage(ServerPackage serverPackage) {
        // 打印服务器信息包内容
        log.info(serverPackage.toJsonString());
        // 把服务器信息添加或更新到数据库
        this.operateServer(serverPackage);
        // 把服务器内存信息添加到数据库
        this.operateServerMemory(serverPackage);
        // 把服务器CPU信息添加到数据库
        this.operateServerCpu(serverPackage);
        // 把服务器网卡信息添加到数据库
        this.operateServerNetcard(serverPackage);
        // 把服务器磁盘信息添加到数据库
        this.operateServerDisk(serverPackage);
        // 把服务器电池信息添加或更新到数据库
        this.operateServerPowerSources(serverPackage);
        // 把服务器传感器信息添加或更新到数据库
        this.operateServerSensors(serverPackage);
        // 返回结果
        return Result.builder().isSuccess(true).msg(ResultMsgConstants.SUCCESS).build();
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
        Integer selectCountDb = this.monitorServerSensorsDao.selectCount(lambdaQueryWrapper);
        // 添加或更新到数据库
        MonitorServerSensors monitorServerSensors = new MonitorServerSensors();
        monitorServerSensors.setIp(ip);
        monitorServerSensors.setCpuTemperature(sensorsDomain.getCpuTemperature());
        monitorServerSensors.setCpuVoltage(sensorsDomain.getCpuVoltage());
        List<SensorsDomain.FanSpeedDomain> fanSpeedDomainList = sensorsDomain.getFanSpeedDomainList();
        if (CollectionUtils.isNotEmpty(fanSpeedDomainList)) {
            monitorServerSensors.setFanSpeed(sensorsDomain.getFanSpeedDomainList().stream().map(String::valueOf).collect(Collectors.joining(",")));
        }
        // 没有
        if (selectCountDb == null || selectCountDb == 0) {
            monitorServerSensors.setInsertTime(new Date());
            this.monitorServerSensorsDao.insert(monitorServerSensors);
        }
        // 有
        else {
            monitorServerSensors.setUpdateTime(new Date());
            LambdaUpdateWrapper<MonitorServerSensors> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaQueryWrapper.eq(MonitorServerSensors::getIp, ip);
            this.monitorServerSensorsDao.update(monitorServerSensors, lambdaUpdateWrapper);
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
        for (int i = 0; i < powerSourceDomains.size(); i++) {
            PowerSourcesDomain.PowerSourceDomain powerSourceDomain = powerSourceDomains.get(i);
            // 查询数据库中是否有此IP的电池信息
            LambdaQueryWrapper<MonitorServerPowerSources> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(MonitorServerPowerSources::getIp, ip);
            lambdaQueryWrapper.eq(MonitorServerPowerSources::getPowerSourcesNo, i + 1);
            Integer selectCountDb = this.monitorServerPowerSourcesDao.selectCount(lambdaQueryWrapper);
            // 更新或添加到数据库
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
            if (selectCountDb == null || selectCountDb == 0) {
                monitorServerPowerSources.setInsertTime(new Date());
                this.monitorServerPowerSourcesDao.insert(monitorServerPowerSources);
            }
            // 有
            else {
                monitorServerPowerSources.setUpdateTime(new Date());
                LambdaUpdateWrapper<MonitorServerPowerSources> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(MonitorServerPowerSources::getIp, ip);
                lambdaUpdateWrapper.eq(MonitorServerPowerSources::getPowerSourcesNo, i + 1);
                this.monitorServerPowerSourcesDao.update(monitorServerPowerSources, lambdaUpdateWrapper);
            }
        }
    }

    /**
     * <p>
     * 把服务器磁盘信息添加到数据库
     * </p>
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2020/5/12 10:29
     */
    private void operateServerDisk(ServerPackage serverPackage) {
        // IP地址
        String ip = serverPackage.getIp();
        // 磁盘信息
        DiskDomain diskDomain = serverPackage.getServer().getDiskDomain();
        List<DiskDomain.DiskInfoDomain> diskInfoDomains = diskDomain.getDiskInfoList();
        for (int i = 0; i < diskInfoDomains.size(); i++) {
            DiskDomain.DiskInfoDomain diskInfoDomain = diskInfoDomains.get(i);
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
            monitorServerDisk.setInsertTime(serverPackage.getDateTime());
            monitorServerDisk.setUpdateTime(serverPackage.getDateTime());
            this.monitorServerDiskDao.insert(monitorServerDisk);
        }
    }

    /**
     * <p>
     * 把服务器网卡信息添加到数据库
     * </p>
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2020/5/11 23:39
     */
    private void operateServerNetcard(ServerPackage serverPackage) {
        // IP地址
        String ip = serverPackage.getIp();
        // 网卡信息
        NetDomain netDomain = serverPackage.getServer().getNetDomain();
        // 设置网卡信息
        List<NetDomain.NetInterfaceDomain> netInterfaceDomains = netDomain.getNetList();
        for (int i = 0; i < netInterfaceDomains.size(); i++) {
            NetDomain.NetInterfaceDomain netInterfaceDomain = netInterfaceDomains.get(i);
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
            // 时间
            monitorServerNetcard.setInsertTime(serverPackage.getDateTime());
            monitorServerNetcard.setUpdateTime(serverPackage.getDateTime());
            this.monitorServerNetcardDao.insert(monitorServerNetcard);
        }
    }

    /**
     * <p>
     * 把服务器CPU信息添加到数据库
     * </p>
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2020/5/11 17:19
     */
    private void operateServerCpu(ServerPackage serverPackage) {
        // IP地址
        String ip = serverPackage.getIp();
        // Cpu信息
        CpuDomain cpuDomain = serverPackage.getServer().getCpuDomain();
        List<CpuDomain.CpuInfoDomain> cpuInfoDomains = cpuDomain.getCpuList();
        for (int i = 0; i < cpuInfoDomains.size(); i++) {
            CpuDomain.CpuInfoDomain cpuInfoDomain = cpuInfoDomains.get(i);
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
            monitorServerCpu.setInsertTime(serverPackage.getDateTime());
            monitorServerCpu.setUpdateTime(serverPackage.getDateTime());
            // 以后可能优化成批量插入
            this.monitorServerCpuDao.insert(monitorServerCpu);
        }
    }

    /**
     * <p>
     * 把服务器内存信息添加到数据库
     * </p>
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2020/5/11 16:43
     */
    private void operateServerMemory(ServerPackage serverPackage) {
        // IP地址
        String ip = serverPackage.getIp();
        // 内存信息
        MemoryDomain memoryDomain = serverPackage.getServer().getMemoryDomain();
        MemoryDomain.MenDomain menDomain = memoryDomain.getMenDomain();
        MemoryDomain.SwapDomain swapDomain = memoryDomain.getSwapDomain();
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
        monitorServerMemory.setInsertTime(serverPackage.getDateTime());
        monitorServerMemory.setUpdateTime(serverPackage.getDateTime());
        this.monitorServerMemoryDao.insert(monitorServerMemory);
    }

    /**
     * <p>
     * 把服务器信息添加或更新到数据库
     * </p>
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2020/5/11 16:01
     */
    private void operateServer(ServerPackage serverPackage) {
        // IP地址
        String ip = serverPackage.getIp();
        // 操作系统信息
        OsDomain osDomain = serverPackage.getServer().getOsDomain();
        LambdaQueryWrapper<MonitorServer> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MonitorServer::getIp, ip);
        Integer selectCountDb = this.monitorServerDao.selectCount(lambdaQueryWrapper);
        MonitorServer monitorServer = new MonitorServer();
        monitorServer.setIp(ip);
        monitorServer.setServerName(osDomain.getComputerName());
        monitorServer.setOsName(osDomain.getOsName());
        monitorServer.setOsVersion(osDomain.getOsVersion());
        monitorServer.setOsTimeZone(osDomain.getOsTimeZone());
        monitorServer.setUserHome(osDomain.getUserHome());
        monitorServer.setUserName(osDomain.getUserName());
        // 新增服务器信息
        if (selectCountDb == null || selectCountDb == 0) {
            monitorServer.setInsertTime(serverPackage.getDateTime());
            this.monitorServerDao.insert(monitorServer);
        }
        // 更新服务器信息
        else {
            monitorServer.setUpdateTime(serverPackage.getDateTime());
            LambdaUpdateWrapper<MonitorServer> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(MonitorServer::getIp, ip);
            this.monitorServerDao.update(monitorServer, lambdaUpdateWrapper);
        }
    }

}
