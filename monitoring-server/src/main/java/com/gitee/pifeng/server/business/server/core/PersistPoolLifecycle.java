package com.gitee.pifeng.server.business.server.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.gitee.pifeng.common.constant.MonitorTypeEnums;
import com.gitee.pifeng.server.business.server.domain.*;
import com.gitee.pifeng.server.business.server.entity.MonitorInstance;
import com.gitee.pifeng.server.business.server.entity.MonitorServer;
import com.gitee.pifeng.server.business.server.pool.*;
import com.gitee.pifeng.server.business.server.service.IInstanceService;
import com.gitee.pifeng.server.business.server.service.IServerService;
import com.gitee.pifeng.server.constant.FileNameConstants;
import com.gitee.pifeng.server.inf.IInstanceMonitoringListener;
import com.gitee.pifeng.server.inf.IServerMonitoringListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 持久化所有信息池。
 * </p>
 * 1.在Spring容器初始化时从文件系统中加载所有信息池内容到Spring容器；<br>
 * 2.定时每十分钟把Spring容器中的所有信息池内容持久化到文件系统；<br>
 * 3.在Spring容器销毁时持久化Spring容器中的所有信息池内容到文件系统；<br>
 * 4.数据库中删除了“应用实例”、“服务器”数据时，唤醒执行监控信息池回调方法。<br>
 *
 * @author 皮锋
 * @custom.date 2020/3/25 16:38
 */
@Component
@Slf4j
public class PersistPoolLifecycle implements InitializingBean, DisposableBean, IInstanceMonitoringListener, IServerMonitoringListener {

    /**
     * 应用实例池
     */
    @Autowired
    private InstancePool instancePool;

    /**
     * 服务器信息池
     */
    @Autowired
    private ServerPool serverPool;

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
     * 应用实例服务接口
     */
    @Autowired
    private IInstanceService instanceService;

    /**
     * 服务器信息服务层接口
     */
    @Autowired
    private IServerService serverService;

    /**
     * <p>
     * 定时每十分钟把Spring容器中的所有信息池内容持久化到文件系统，<br>
     * {@code @Scheduled(cron = "0 0/10 * * * ?")}
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/4/1 9:51
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    public void durable() {
        // 把Spring容器中的信息池内容存入文件系统
        this.storePools();
    }

    /**
     * <p>
     * 在Spring容器初始化时加载资源。
     * </p>
     * 把持久化到文件系统的信息池内容加载到Spring容器。
     *
     * @author 皮锋
     * @custom.date 2020/3/31 16:17
     */
    @Override
    public void afterPropertiesSet() {
        // 把持久化到文件系统的信息池内容加载到Spring容器
        this.loadPools();
    }

    /**
     * <p>
     * 在Spring容器销毁时处理资源
     * </p>
     * 1.把Spring容器中的信息池内容存入文件系统；<br>
     * 2.把Spring容器中的信息池清空<br>
     *
     * @author 皮锋
     * @custom.date 2020/3/25 16:43
     */
    @Override
    public void destroy() {
        // 把Spring容器中的信息池内容存入文件系统
        this.storePools();
        // 把Spring容器中的信息池清空
        this.clearPools();
    }

    /**
     * <p>
     * 把Spring容器中的信息池内容存入文件系统
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020年3月31日 下午3:40:05
     */
    private void storePools() {
        try {
            FileUtils.writeStringToFile(new File(FileNameConstants.INSTANCE_POOL), this.instancePool.toJsonString(),
                    StandardCharsets.UTF_8, false);
            log.info("把Spring容器中的应用实例池内容存入文件系统成功！");
        } catch (Exception e) {
            log.error("把Spring容器中的应用实例池内容存入文件系统异常！");
        }
        try {
            FileUtils.writeStringToFile(new File(FileNameConstants.SERVER_POOL), this.serverPool.toJsonString(),
                    StandardCharsets.UTF_8, false);
            log.info("把Spring容器中的服务器信息池内容存入文件系统成功！");
        } catch (Exception e) {
            log.info("把Spring容器中的服务器信息池内容存入文件系统异常！");
        }
        try {
            FileUtils.writeStringToFile(new File(FileNameConstants.MEMORY_POOL), this.memoryPool.toJsonString(),
                    StandardCharsets.UTF_8, false);
            log.info("把Spring容器中的服务器内存信息池内容存入文件系统成功！");
        } catch (Exception e) {
            log.error("把Spring容器中的服务器内存信息池内容存入文件系统异常！");
        }
        try {
            FileUtils.writeStringToFile(new File(FileNameConstants.CPU_POOL), this.cpuPool.toJsonString(),
                    StandardCharsets.UTF_8, false);
            log.info("把Spring容器中的服务器CPU信息池内容存入文件系统成功！");
        } catch (Exception e) {
            log.error("把Spring容器中的服务器CPU信息池内容存入文件系统异常！");
        }
        try {
            FileUtils.writeStringToFile(new File(FileNameConstants.DISK_POOL), this.diskPool.toJsonString(),
                    StandardCharsets.UTF_8, false);
            log.info("把Spring容器中的服务器磁盘信息池内容存入文件系统成功！");
        } catch (Exception e) {
            log.error("把Spring容器中的服务器磁盘信息池内容存入文件系统异常！");
        }
    }

    /**
     * <p>
     * 把持久化到文件系统的信息池内容加载到Spring容器
     * </p>
     * 先从数据库中加载，再从文件系统中加载，最后取数据库与文件系统的交集。
     *
     * @author 皮锋
     * @custom.date 2020/4/28 14:39
     */
    private void loadPools() {
        // 数据库中的应用实例
        List<MonitorInstance> monitorInstances = this.instanceService.list();
        // 数据库中的服务器
        List<MonitorServer> monitorServers = this.serverService.list();
        this.loadInstancePool(monitorInstances);
        this.loadServerPool(monitorServers);
        this.loadMemoryPool(monitorServers);
        this.loadCpuPool(monitorServers);
        this.loadDiskPool(monitorServers);
    }

    /**
     * <p>
     * 把持久化到文件系统的应用实例信息池内容加载到Spring容器
     * </p>
     *
     * @param monitorInstances 数据库中的应用实例列表
     * @author 皮锋
     * @custom.date 2021/1/23 10:59
     */
    private void loadInstancePool(List<MonitorInstance> monitorInstances) {
        try {
            String instancePoolStr = FileUtils.readFileToString(new File(FileNameConstants.INSTANCE_POOL), StandardCharsets.UTF_8);
            if (StringUtils.isNotBlank(instancePoolStr)) {
                Map<String, Instance> map = JSON.parseObject(instancePoolStr, new TypeReference<Map<String, Instance>>() {
                });
                for (MonitorInstance monitorInstance : monitorInstances) {
                    // 数据库中的应用实例ID
                    String instanceIdDb = monitorInstance.getInstanceId();
                    for (Map.Entry<String, Instance> entry : map.entrySet()) {
                        Instance instance = entry.getValue();
                        // 文件系统中的应用实例ID
                        String instanceIdDisk = instance.getInstanceId();
                        if (!StringUtils.equals(instanceIdDb, instanceIdDisk)) {
                            continue;
                        }
                        boolean isOnline = instance.isOnline();
                        // 如果之前是在线状态，把最后一次通过心跳包更新的时间更新到当前时间
                        if (isOnline) {
                            instance.setDateTime(new Date());
                        }
                        this.instancePool.put(instanceIdDisk, instance);
                    }
                }
            }
            log.info("把文件系统中的应用实例池内容加载到Spring容器成功！");
        } catch (Exception ignored) {
            log.info("把文件系统中的应用实例池内容加载到Spring容器异常！");
        }
    }

    /**
     * <p>
     * 把持久化到文件系统的服务器信息池内容加载到Spring容器
     * </p>
     *
     * @param monitorServers 数据库中的服务器信息列表
     * @author 皮锋
     * @custom.date 2021/1/23 10:58
     */
    private void loadServerPool(List<MonitorServer> monitorServers) {
        try {
            String serverPoolStr = FileUtils.readFileToString(new File(FileNameConstants.SERVER_POOL), StandardCharsets.UTF_8);
            if (StringUtils.isNotBlank(serverPoolStr)) {
                Map<String, Server> map = JSON.parseObject(serverPoolStr, new TypeReference<Map<String, Server>>() {
                });
                for (MonitorServer monitorServer : monitorServers) {
                    // 数据库中的服务器IP
                    String ipDb = monitorServer.getIp();
                    for (Map.Entry<String, Server> entry : map.entrySet()) {
                        Server server = entry.getValue();
                        // 文件系统中的服务器IP
                        String ipDisk = server.getIp();
                        if (!StringUtils.equals(ipDb, ipDisk)) {
                            continue;
                        }
                        this.serverPool.put(ipDisk, server);
                    }
                }
            }
            log.info("把文件系统中的服务器信息池内容加载到Spring容器成功！");
        } catch (Exception ignored) {
            log.info("把文件系统中的服务器信息池内容加载到Spring容器异常！");
        }
    }

    /**
     * <p>
     * 把持久化到文件系统的内存信息池内容加载到Spring容器
     * </p>
     *
     * @param monitorServers 数据库中的服务器信息列表
     * @author 皮锋
     * @custom.date 2021/1/23 10:57
     */
    private void loadMemoryPool(List<MonitorServer> monitorServers) {
        try {
            String memoryPoolStr = FileUtils.readFileToString(new File(FileNameConstants.MEMORY_POOL), StandardCharsets.UTF_8);
            if (StringUtils.isNotBlank(memoryPoolStr)) {
                Map<String, Memory> map = JSON.parseObject(memoryPoolStr, new TypeReference<Map<String, Memory>>() {
                });
                for (MonitorServer monitorServer : monitorServers) {
                    // 数据库中的服务器IP
                    String ipDb = monitorServer.getIp();
                    for (Map.Entry<String, Memory> entry : map.entrySet()) {
                        Memory memory = entry.getValue();
                        // 文件系统中的服务器内存IP
                        String ipDisk = memory.getIp();
                        if (!StringUtils.equals(ipDb, ipDisk)) {
                            continue;
                        }
                        this.memoryPool.put(ipDisk, memory);
                    }
                }
            }
            log.info("把文件系统中的服务器内存信息池内容加载到Spring容器成功！");
        } catch (Exception ignored) {
            log.info("把文件系统中的服务器内存信息池内容加载到Spring容器异常！");
        }
    }

    /**
     * <p>
     * 把持久化到文件系统的CPU信息池内容加载到Spring容器
     * </p>
     *
     * @param monitorServers 数据库中的服务器信息列表
     * @author 皮锋
     * @custom.date 2021/1/23 10:56
     */
    private void loadCpuPool(List<MonitorServer> monitorServers) {
        try {
            String cpuPoolStr = FileUtils.readFileToString(new File(FileNameConstants.CPU_POOL), StandardCharsets.UTF_8);
            if (StringUtils.isNotBlank(cpuPoolStr)) {
                Map<String, Cpu> map = JSON.parseObject(cpuPoolStr, new TypeReference<Map<String, Cpu>>() {
                });
                for (MonitorServer monitorServer : monitorServers) {
                    // 数据库中的服务器IP
                    String ipDb = monitorServer.getIp();
                    for (Map.Entry<String, Cpu> entry : map.entrySet()) {
                        Cpu cpu = entry.getValue();
                        // 文件系统中的服务器Cpu IP
                        String ipDisk = cpu.getIp();
                        if (!StringUtils.equals(ipDb, ipDisk)) {
                            continue;
                        }
                        this.cpuPool.put(ipDisk, cpu);
                    }
                }
            }
            log.info("把文件系统中的服务器CPU信息池内容加载到Spring容器成功！");
        } catch (Exception ignored) {
            log.info("把文件系统中的服务器CPU信息池内容加载到Spring容器异常！");
        }
    }

    /**
     * <p>
     * 把持久化到文件系统的磁盘信息池内容加载到Spring容器
     * </p>
     *
     * @param monitorServers 数据库中的服务器信息列表
     * @author 皮锋
     * @custom.date 2021/1/23 10:55
     */
    private void loadDiskPool(List<MonitorServer> monitorServers) {
        try {
            String diskPoolStr = FileUtils.readFileToString(new File(FileNameConstants.DISK_POOL), StandardCharsets.UTF_8);
            if (StringUtils.isNotBlank(diskPoolStr)) {
                Map<String, Disk> map = JSON.parseObject(diskPoolStr, new TypeReference<Map<String, Disk>>() {
                });
                for (MonitorServer monitorServer : monitorServers) {
                    // 数据库中的服务器IP
                    String ipDb = monitorServer.getIp();
                    for (Map.Entry<String, Disk> entry : map.entrySet()) {
                        Disk disk = entry.getValue();
                        // 文件系统中的磁盘IP
                        String ipDisk = disk.getIp();
                        if (!StringUtils.equals(ipDb, ipDisk)) {
                            continue;
                        }
                        this.diskPool.put(ipDisk, disk);
                    }
                }
            }
            log.info("把文件系统中的服务器磁盘信息池内容加载到Spring容器成功！");
        } catch (Exception ignored) {
            log.info("把文件系统中的服务器磁盘信息池内容加载到Spring容器异常！");
        }
    }

    /**
     * <p>
     * 把Spring容器中的信息池清空
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020年3月31日 下午3:40:22
     */
    private void clearPools() {
        this.instancePool.clear();
        this.serverPool.clear();
        this.memoryPool.clear();
        this.cpuPool.clear();
        this.diskPool.clear();
        log.info("Spring容器中的信息池已经清空！");
    }

    /**
     * <p>
     * 数据库中删除了“应用实例”、“服务器”数据时，唤醒执行监控信息池回调方法。
     * </p>
     *
     * @param monitorType 监控类型
     * @param params      回调参数
     * @author 皮锋
     * @custom.date 2020/3/30 20:18
     */
    @Override
    public void wakeUpMonitorPool(MonitorTypeEnums monitorType, List<String> params) {
        if (monitorType == MonitorTypeEnums.INSTANCE) {
            params.forEach(e ->
                    // 移除当前应用实例
                    this.instancePool.remove(e)
            );
        }
        if (monitorType == MonitorTypeEnums.SERVER) {
            params.forEach(e -> {
                //移除服务器信息
                this.serverPool.remove(e);
                //移除当前内存信息
                this.memoryPool.remove(e);
                // 移除当前CPU信息
                this.cpuPool.remove(e);
                // 移除当前磁盘信息
                this.diskPool.remove(e);
            });
        }
        // 把Spring容器中的信息池内容存入文件系统
        this.storePools();
    }
}
