package com.imby.server.business.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.imby.common.constant.ResultMsgConstants;
import com.imby.common.domain.Result;
import com.imby.common.domain.server.*;
import com.imby.common.dto.ServerPackage;
import com.imby.server.business.server.dao.*;
import com.imby.server.business.server.entity.*;
import com.imby.server.business.server.service.IServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 服务器信息服务层接口实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/23 15:23
 */
@Service
public class ServerServiceImpl implements IServerService {

    /**
     * 服务器操作系统数据访问对象
     */
    @Autowired
    private IMonitorServerOsDao monitorServerOsDao;

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
        // 把服务器内存信息添加到数据库
        this.operateServerMemory(serverPackage);
        // 把服务器CPU信息添加到数据库
        this.operateServerCpu(serverPackage);
        // 把服务器网卡信息添加或更新到数据库
        this.operateServerNetcard(serverPackage);
        // 把服务器磁盘信息添加到数据库
        this.operateServerDisk(serverPackage);
        // 把服务器操作系统信息添加或更新到数据库
        this.operateServerOs(serverPackage);
        // 返回结果
        return Result.builder().isSuccess(true).msg(ResultMsgConstants.SUCCESS).build();
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
     * 把服务器网卡信息添加或更新到数据库
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
        List<NetDomain.NetInterfaceConfigDomain> netInterfaceConfigDomains = netDomain.getNetList();
        for (int i = 0; i < netInterfaceConfigDomains.size(); i++) {
            NetDomain.NetInterfaceConfigDomain netInterfaceConfigDomain = netInterfaceConfigDomains.get(i);
            MonitorServerNetcard monitorServerNetcard = new MonitorServerNetcard();
            monitorServerNetcard.setIp(ip);
            monitorServerNetcard.setNetNo(i + 1);
            monitorServerNetcard.setAddress(netInterfaceConfigDomain.getAddress());
            monitorServerNetcard.setBroadcast(netInterfaceConfigDomain.getBroadcast());
            monitorServerNetcard.setMask(netInterfaceConfigDomain.getMask());
            monitorServerNetcard.setName(netInterfaceConfigDomain.getName());
            monitorServerNetcard.setType(netInterfaceConfigDomain.getType());
            monitorServerNetcard.setHwAddr(netInterfaceConfigDomain.getHwAddr());
            monitorServerNetcard.setDescription(netInterfaceConfigDomain.getDescription());
            // 查询数据库中是否有当前网卡信息
            LambdaQueryWrapper<MonitorServerNetcard> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(MonitorServerNetcard::getIp, ip);
            lambdaQueryWrapper.eq(MonitorServerNetcard::getNetNo, i + 1);
            MonitorServerNetcard monitorServerNetcardDb = this.monitorServerNetcardDao.selectOne(lambdaQueryWrapper);
            // 新增网卡信息
            if (monitorServerNetcardDb == null) {
                monitorServerNetcard.setInsertTime(serverPackage.getDateTime());
                this.monitorServerNetcardDao.insert(monitorServerNetcard);
            }
            // 更新网卡信息
            else {
                monitorServerNetcard.setUpdateTime(serverPackage.getDateTime());
                LambdaUpdateWrapper<MonitorServerNetcard> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(MonitorServerNetcard::getIp, ip);
                lambdaUpdateWrapper.eq(MonitorServerNetcard::getNetNo, i + 1);
                this.monitorServerNetcardDao.update(monitorServerNetcard, lambdaUpdateWrapper);
            }
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
        MonitorServerMemory monitorServerMemory = new MonitorServerMemory();
        monitorServerMemory.setIp(ip);
        monitorServerMemory.setMenTotal(memoryDomain.getMemTotal());
        monitorServerMemory.setMenUsed(memoryDomain.getMemUsed());
        monitorServerMemory.setMenFree(memoryDomain.getMemFree());
        monitorServerMemory.setMenUsedPercent(memoryDomain.getMenUsedPercent());
        monitorServerMemory.setInsertTime(serverPackage.getDateTime());
        monitorServerMemory.setUpdateTime(serverPackage.getDateTime());
        this.monitorServerMemoryDao.insert(monitorServerMemory);
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
        MonitorServerOs monitorServerDb = this.monitorServerOsDao.selectOne(lambdaQueryWrapper);
        MonitorServerOs monitorServerOs = new MonitorServerOs();
        monitorServerOs.setIp(ip);
        monitorServerOs.setServerName(osDomain.getComputerName());
        monitorServerOs.setOsName(osDomain.getOsName());
        monitorServerOs.setOsVersion(osDomain.getOsVersion());
        monitorServerOs.setOsTimeZone(osDomain.getOsTimeZone());
        monitorServerOs.setUserHome(osDomain.getUserHome());
        monitorServerOs.setUserName(osDomain.getUserName());
        // 新增服务器信息
        if (monitorServerDb == null) {
            monitorServerOs.setInsertTime(serverPackage.getDateTime());
            this.monitorServerOsDao.insert(monitorServerOs);
        }
        // 更新服务器信息
        else {
            monitorServerOs.setUpdateTime(serverPackage.getDateTime());
            LambdaUpdateWrapper<MonitorServerOs> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(MonitorServerOs::getIp, ip);
            this.monitorServerOsDao.update(monitorServerOs, lambdaUpdateWrapper);
        }
    }

}
