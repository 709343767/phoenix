package com.transfar.server.business.server.service.impl;

import com.transfar.common.domain.server.*;
import com.transfar.common.dto.ServerPackage;
import com.transfar.common.inf.ICallback;
import com.transfar.server.business.server.core.CpuPool;
import com.transfar.server.business.server.core.DiskPool;
import com.transfar.server.business.server.core.MemoryPool;
import com.transfar.server.business.server.domain.Cpu;
import com.transfar.server.business.server.domain.Disk;
import com.transfar.server.business.server.domain.Memory;
import com.transfar.server.business.server.service.IServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

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
     * 回调接口
     */
    @Autowired
    private List<ICallback> callbacks;

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

        Memory memory = new Memory();
        memory.setIp(ip);
        memory.setRate(serverPackage.getRate());
        memory.setMemoryDomain(memoryDomain);
        Memory poolMemory = this.memoryPool.get(ip);
        memory.setNum(poolMemory != null ? poolMemory.getNum() : 0);
        memory.setAlarm(poolMemory != null && poolMemory.isAlarm());
        memory.setOverLoad(poolMemory != null && poolMemory.isOverLoad());
        // 更新服务器内存信息池
        this.memoryPool.updateMemoryPool(ip, memory);

        Cpu cpu = new Cpu();
        cpu.setIp(ip);
        cpu.setRate(serverPackage.getRate());
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

        Disk disk = new Disk();
        ConcurrentHashMap<String, Disk.Subregion> subregionConcurrentHashMap = new ConcurrentHashMap<>();
        for (DiskDomain.DiskInfoDomain diskInfoDomain : diskDomain.getDiskInfoList()) {
            Disk.Subregion subregion = new Disk.Subregion();
            // 盘符名字
            String devName = diskInfoDomain.getDevName();
            // 盘符使用率
            double usePercent = Disk.calculateUsePercent(diskInfoDomain.getUsePercent());
            subregion.setUsePercent(usePercent);
            subregion.setDevName(devName);
            Disk.Subregion poolDiskSubregion = this.diskPool.get(ip) != null ? this.diskPool.get(ip).getSubregionConcurrentHashMap().get(devName) : null;
            subregion.setAlarm(poolDiskSubregion != null && poolDiskSubregion.isAlarm());
            subregion.setOverLoad(poolDiskSubregion != null && poolDiskSubregion.isOverLoad());
            subregionConcurrentHashMap.put(devName, subregion);
        }
        disk.setIp(ip);
        disk.setSubregionConcurrentHashMap(subregionConcurrentHashMap);
        this.diskPool.updateMemoryPool(ip, disk);
        // 调用监控回调接口
        this.callbacks.forEach(e -> e.event(ip));

        return true;
    }

}
