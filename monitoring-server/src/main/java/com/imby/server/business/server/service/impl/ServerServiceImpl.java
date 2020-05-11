package com.imby.server.business.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.imby.common.constant.ResultMsgConstants;
import com.imby.common.domain.Result;
import com.imby.common.domain.server.*;
import com.imby.common.dto.ServerPackage;
import com.imby.server.business.server.dao.IMonitorServerCpuDao;
import com.imby.server.business.server.dao.IMonitorServerMemoryDao;
import com.imby.server.business.server.dao.IMonitorServerNetcardDao;
import com.imby.server.business.server.dao.IMonitorServerOsDao;
import com.imby.server.business.server.entity.MonitorServerCpu;
import com.imby.server.business.server.entity.MonitorServerMemory;
import com.imby.server.business.server.entity.MonitorServerNetcard;
import com.imby.server.business.server.entity.MonitorServerOs;
import com.imby.server.business.server.service.IServerService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
     * <p>
     * 处理服务器信息包
     * </p>
     *
     * @param serverPackage 服务器信息包
     * @return {@link Result}
     * @author 皮锋
     * @custom.date 2020/3/23 15:29
     */
    @Override
    public Result dealServerPackage(ServerPackage serverPackage) {
        // 返回结果
        Result result = new Result();
        // IP地址
        String ip = serverPackage.getIp();
        // 服务器信息
        ServerDomain serverDomain = serverPackage.getServerDomain();
        // 把服务器内存信息添加到数据库
        this.operateServerMemory(serverPackage);
        // 把服务器CPU信息添加到数据库
        this.operateServerCpu(serverPackage);
        // 把服务器网卡信息添加或更新到数据库
        this.operateServerNetcard(serverPackage);


        // java虚拟机信息
        JvmDomain jvmDomain = serverDomain.getJvmDomain();
        // 磁盘信息
        DiskDomain diskDomain = serverDomain.getDiskDomain();
        // 把服务器操作系统信息添加或更新到数据库
        this.operateServerOs(serverPackage);
        return result.setSuccess(true).setMsg(ResultMsgConstants.SUCCESS);
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
        // 服务器信息
        ServerDomain serverDomain = serverPackage.getServerDomain();
        // 网卡信息
        NetDomain netDomain = serverDomain.getNetDomain();
        LambdaQueryWrapper<MonitorServerNetcard> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MonitorServerNetcard::getIp, ip);
        List<MonitorServerNetcard> monitorServerNetcardsDb = this.monitorServerNetcardDao.selectList(lambdaQueryWrapper);
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
            // 新增网卡信息
            if (CollectionUtils.isEmpty(monitorServerNetcardsDb)) {
                monitorServerNetcard.setInsertTime(serverPackage.getDateTime());
                this.monitorServerNetcardDao.insert(monitorServerNetcard);
            }
            // 更新网卡信息
            else {
                monitorServerNetcard.setUpdateTime(serverPackage.getDateTime());
                LambdaUpdateWrapper<MonitorServerNetcard> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(MonitorServerNetcard::getIp, ip);
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
        // 服务器信息
        ServerDomain serverDomain = serverPackage.getServerDomain();
        // Cpu信息
        CpuDomain cpuDomain = serverDomain.getCpuDomain();
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
        // 服务器信息
        ServerDomain serverDomain = serverPackage.getServerDomain();
        // 内存信息
        MemoryDomain memoryDomain = serverDomain.getMemoryDomain();
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
        // 服务器信息
        ServerDomain serverDomain = serverPackage.getServerDomain();
        // 操作系统信息
        OsDomain osDomain = serverDomain.getOsDomain();
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
