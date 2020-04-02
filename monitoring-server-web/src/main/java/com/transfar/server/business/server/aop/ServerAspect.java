package com.transfar.server.business.server.aop;

import com.alibaba.fastjson.JSON;
import com.transfar.common.domain.server.CpuDomain;
import com.transfar.common.domain.server.DiskDomain;
import com.transfar.common.domain.server.MemoryDomain;
import com.transfar.common.domain.server.ServerDomain;
import com.transfar.common.dto.ServerPackage;
import com.transfar.server.business.server.core.CpuPool;
import com.transfar.server.business.server.core.DiskPool;
import com.transfar.server.business.server.core.MemoryPool;
import com.transfar.server.business.server.domain.Cpu;
import com.transfar.server.business.server.domain.Disk;
import com.transfar.server.business.server.domain.Memory;
import com.transfar.server.inf.IServerMonitoringListener;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 在服务器信息包处理完成之后通过切面，刷新Spring容器中的服务器信息池，调用服务器信息监听器回调接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年4月1日 下午3:21:19
 */
@Aspect
@Component
public class ServerAspect {

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
     * 服务器信息监听器
     */
    @Autowired
    private List<IServerMonitoringListener> serverMonitoringListeners;

    /**
     * <p>
     * 定义切入点，切入点为com.transfar.server.business.server.controller.ServerController.acceptServerPackage这一个方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/2/22 17:56
     */
    @Pointcut("execution(public * com.transfar.server.business.server.controller.ServerController.acceptServerPackage(..))")
    public void tangentPoint() {
    }

    /**
     * <p>
     * 通过后置通知，在服务器信息包处理完成之后通过切面，刷新Spring容器中的服务器信息池，调用服务器信息监听器回调接口
     * </p>
     *
     * @param joinPoint 提供对连接点上可用状态和有关状态的静态信息的反射访问
     * @author 皮锋
     * @custom.date 2020年4月1日 下午3:34:06
     */
    @AfterReturning("tangentPoint()")
    public void wakeUp(JoinPoint joinPoint) {
        String args = String.valueOf(joinPoint.getArgs()[0]);
        ServerPackage serverPackage = JSON.parseObject(args, ServerPackage.class);
        // IP地址
        String ip = serverPackage.getIp();
        // 服务器信息
        ServerDomain serverDomain = serverPackage.getServerDomain();
        // 内存信息
        MemoryDomain memoryDomain = serverDomain.getMemoryDomain();
        // Cpu信息
        CpuDomain cpuDomain = serverDomain.getCpuDomain();
        // 磁盘信息
        DiskDomain diskDomain = serverDomain.getDiskDomain();
        // 刷新服务器信息
        this.refreshServerInfo(ip, memoryDomain, cpuDomain, diskDomain);
        // 调用监听器回调接口
        this.serverMonitoringListeners.forEach(e -> e.wakeUp(ip));
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
