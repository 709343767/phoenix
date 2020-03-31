package com.transfar.server.business.server.service.impl;

import com.transfar.common.domain.server.*;
import com.transfar.common.dto.ServerPackage;
import com.transfar.server.business.server.core.CpuPool;
import com.transfar.server.business.server.core.DiskPool;
import com.transfar.server.business.server.core.MemoryPool;
import com.transfar.server.business.server.domain.Cpu;
import com.transfar.server.business.server.domain.Disk;
import com.transfar.server.business.server.domain.Memory;
import com.transfar.server.business.server.service.IServerService;
import com.transfar.server.inf.IServerMonitoringListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * 服务器信息监听器
     */
    @Autowired
    private List<IServerMonitoringListener> serverMonitoringListeners;

    /**
     * 服务器内存信息池
     */
    @Autowired
    private MemoryPool memoryPool;

    /**
     * 服务器CPU信息池
     */
    @Autowired
    private CpuPool cpuPool;

    /**
     * 服务器磁盘信息池
     */
    @Autowired
    private DiskPool diskPool;

    /**
     * <p>
     * 处理服务器信息包
     * </p>
     *
     * @param serverPackage 服务器信息包
     * @return boolean
     * @author 皮锋
     * @custom.date 2020/3/23 15:29
     */
    @Override
    public boolean dealServerPackage(ServerPackage serverPackage) {
        // IP地址
        String ip = serverPackage.getIp();
        // 服务器信息
        ServerDomain serverDomain = serverPackage.getServerDomain();
        // 操作系统信息
        OsDomain osDomain = serverDomain.getOsDomain();
        // 内存信息
        MemoryDomain memoryDomain = serverDomain.getMemoryDomain();
        // Cpu信息
        CpuDomain cpuDomain = serverDomain.getCpuDomain();
        // 网卡信息
        NetDomain netDomain = serverDomain.getNetDomain();
        // java虚拟机信息
        JvmDomain jvmDomain = serverDomain.getJvmDomain();
        // 磁盘信息
        DiskDomain diskDomain = serverDomain.getDiskDomain();
        // 存数据库

        // 刷新服务器信息
        this.refreshServerInfo(ip, memoryDomain, cpuDomain, diskDomain);

        return true;
    }

    /**
     * <p>
     * 刷新服务器信息
     * </p>
     *
     * @param ip           IP地址
     * @param memoryDomain 内存信息
     * @param cpuDomain    CPU信息
     * @param diskDomain   磁盘信息
     * @author 皮锋
     * @custom.date 2020/3/31 13:39
     */
    private void refreshServerInfo(String ip, MemoryDomain memoryDomain, CpuDomain cpuDomain, DiskDomain diskDomain) {
        // 刷新服务器内存信息
        this.refreshMemory(ip, memoryDomain);
        // 刷新服务器CPU信息
        this.refreshCpu(ip, cpuDomain);
        // 刷新服务器磁盘信息
        this.refreshDisk(ip, diskDomain);
        // 调用监听器回调接口
        this.serverMonitoringListeners.forEach(e -> e.wakeUp(ip));
    }

    /**
     * <p>
     * 刷新服务器磁盘信息
     * </p>
     *
     * @param ip         IP地址
     * @param diskDomain 磁盘信息
     * @author 皮锋
     * @custom.date 2020/3/31 13:44
     */
    private void refreshDisk(String ip, DiskDomain diskDomain) {
        Disk disk = new Disk();
        Map<String, Disk.Subregion> subregionMap = new HashMap<>();
        for (DiskDomain.DiskInfoDomain diskInfoDomain : diskDomain.getDiskInfoList()) {
            Disk.Subregion subregion = new Disk.Subregion();
            // 盘符名字
            String devName = diskInfoDomain.getDevName();
            // 盘符使用率
            double usePercent = Disk.calculateUsePercent(diskInfoDomain.getUsePercent());
            subregion.setUsePercent(usePercent);
            subregion.setDevName(devName);
            Disk.Subregion poolDiskSubregion = this.diskPool.get(ip) != null ? this.diskPool.get(ip).getSubregionMap().get(devName) : null;
            subregion.setAlarm(poolDiskSubregion != null && poolDiskSubregion.isAlarm());
            subregion.setOverLoad(poolDiskSubregion != null && poolDiskSubregion.isOverLoad());
            subregionMap.put(devName, subregion);
        }
        disk.setIp(ip);
        disk.setSubregionMap(subregionMap);
        this.diskPool.updateMemoryPool(ip, disk);
    }

    /**
     * <p>
     * 刷新服务器CPU信息
     * </p>
     *
     * @param ip        IP地址
     * @param cpuDomain CPU信息
     * @author 皮锋
     * @custom.date 2020/3/31 13:43
     */
    private void refreshCpu(String ip, CpuDomain cpuDomain) {
        Cpu cpu = new Cpu();
        cpu.setIp(ip);
        cpu.setCpuDomain(cpuDomain);
        cpu.setAvgCpuCombined(Cpu.calculateAvgCpuCombined(cpuDomain));
        Cpu poolCpu = this.cpuPool.get(ip);
        cpu.setNum90(poolCpu != null ? poolCpu.getNum90() : 0);
        cpu.setAlarm90(poolCpu != null && poolCpu.isAlarm90());
        cpu.setOverLoad90(poolCpu != null && poolCpu.isOverLoad90());
        cpu.setNum100(poolCpu != null ? poolCpu.getNum100() : 0);
        cpu.setAlarm100(poolCpu != null && poolCpu.isAlarm100());
        cpu.setOverLoad100(poolCpu != null && poolCpu.isOverLoad100());
        // 更新服务器CPU信息池
        this.cpuPool.updateMemoryPool(ip, cpu);
    }

    /**
     * <p>
     * 刷新服务器内存信息
     * </p>
     *
     * @param ip           IP地址
     * @param memoryDomain 内存信息
     * @author 皮锋
     * @custom.date 2020/3/31 13:41
     */
    private void refreshMemory(String ip, MemoryDomain memoryDomain) {
        Memory memory = new Memory();
        memory.setIp(ip);
        memory.setMemoryDomain(memoryDomain);
        memory.setUsedPercent(Memory.calculateUsePercent(memoryDomain.getMenUsedPercent()));
        Memory poolMemory = this.memoryPool.get(ip);
        memory.setNum(poolMemory != null ? poolMemory.getNum() : 0);
        memory.setAlarm(poolMemory != null && poolMemory.isAlarm());
        memory.setOverLoad(poolMemory != null && poolMemory.isOverLoad());
        // 更新服务器内存信息池
        this.memoryPool.updateMemoryPool(ip, memory);
    }

}
