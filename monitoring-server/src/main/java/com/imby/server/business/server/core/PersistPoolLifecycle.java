package com.imby.server.business.server.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Charsets;
import com.imby.server.business.server.domain.*;
import com.imby.server.constant.FileNameConstants;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 持久化所有信息池
 * </p>
 * 如果是生成环境：<br>
 * 1.在Spring容器初始化时从文件系统中加载所有信息池内容到Spring容器；<br>
 * 2.定时每十分钟把所有Spring容器中的信息池内容持久化到文件系统;<br>
 * 3.在Spring容器销毁时持久化所有Spring容器中的信息池内容到文件系统。<br>
 *
 * @author 皮锋
 * @custom.date 2020/3/25 16:38
 */
@Component
@Slf4j
@Profile("prod")
public class PersistPoolLifecycle implements InitializingBean, DisposableBean {

    /**
     * 应用实例池
     */
    @Autowired
    private InstancePool instancePool;

    /**
     * 网络信息池
     */
    @Autowired
    private NetPool netPool;

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
     * 定时每十分钟把所有Spring容器中的信息池内容持久化到文件系统，<br>
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
     * 在Spring容器初始化时加载资源
     * </p>
     * 把持久化到文件系统的信息池内容加载到Spring容器
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
        this.clear();
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
                    Charsets.UTF_8, false);
            log.info("把Spring容器中的应用实例池内容存入文件系统成功！");
        } catch (Exception e) {
            log.error("把Spring容器中的应用实例池内容存入文件系统异常！");
        }
        try {
            FileUtils.writeStringToFile(new File(FileNameConstants.NET_POOL), this.netPool.toJsonString(),
                    Charsets.UTF_8, false);
            log.info("把Spring容器中的网络信息池内容存入文件系统成功！");
        } catch (Exception e) {
            log.error("把Spring容器中的网络信息池内容存入文件系统异常！");
        }
        try {
            FileUtils.writeStringToFile(new File(FileNameConstants.MEMORY_POOL), this.memoryPool.toJsonString(),
                    Charsets.UTF_8, false);
            log.info("把Spring容器中的服务器内存信息池内容存入文件系统成功！");
        } catch (Exception e) {
            log.error("把Spring容器中的服务器内存信息池内容存入文件系统异常！");
        }
        try {
            FileUtils.writeStringToFile(new File(FileNameConstants.CPU_POOL), this.cpuPool.toJsonString(),
                    Charsets.UTF_8, false);
            log.info("把Spring容器中的服务器CPU信息池内容存入文件系统成功！");
        } catch (Exception e) {
            log.error("把Spring容器中的服务器CPU信息池内容存入文件系统异常！");
        }
        try {
            FileUtils.writeStringToFile(new File(FileNameConstants.DISK_POOL), this.diskPool.toJsonString(),
                    Charsets.UTF_8, false);
            log.info("把Spring容器中的服务器磁盘信息池内容存入文件系统成功！");
        } catch (Exception e) {
            log.error("把Spring容器中的服务器磁盘信息池内容存入文件系统异常！");
        }
    }

    /**
     * <p>
     * 把持久化到文件系统的信息池内容加载到Spring容器
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/4/28 14:39
     */
    private void loadPools() {
        try {
            String instancePoolStr = FileUtils.readFileToString(new File(FileNameConstants.INSTANCE_POOL), Charsets.UTF_8);
            if (StringUtils.isNotBlank(instancePoolStr)) {
                Map<String, Instance> map = JSON.parseObject(instancePoolStr, new TypeReference<Map<String, Instance>>() {
                });
                for (Map.Entry<String, Instance> entry : map.entrySet()) {
                    Instance instance = entry.getValue();
                    boolean isOnline = instance.isOnline();
                    boolean isOnConnect = instance.isOnConnect();
                    // 如果之前是在线状态且网络正常，把最后一次通过心跳包更新的时间更新到当前时间
                    if (isOnline && isOnConnect) {
                        instance.setDateTime(new Date());
                    }
                }
                this.instancePool.putAll(map);
            }
            log.info("把文件系统中的应用实例池内容加载到Spring容器成功！");
        } catch (Exception ignored) {
            log.info("把文件系统中的应用实例池内容加载到Spring容器异常！");
        }
        try {
            String netPoolStr = FileUtils.readFileToString(new File(FileNameConstants.NET_POOL), Charsets.UTF_8);
            if (StringUtils.isNotBlank(netPoolStr)) {
                Map<String, Net> map = JSON.parseObject(netPoolStr, new TypeReference<Map<String, Net>>() {
                });
                for (Map.Entry<String, Net> entry : map.entrySet()) {
                    Net net = entry.getValue();
                    boolean isOnConnect = net.isOnConnect();
                    // 如果之前是网络可连接状态，把最后一次通过心跳包更新的时间更新到当前时间
                    if (isOnConnect) {
                        net.setDateTime(new Date());
                    }
                }
                this.netPool.putAll(map);
            }
            log.info("把文件系统中的网络信息池内容加载到Spring容器成功！");
        } catch (Exception ignored) {
            log.info("把文件系统中的网络信息池内容加载到Spring容器异常！");
        }
        try {
            String memoryPoolStr = FileUtils.readFileToString(new File(FileNameConstants.MEMORY_POOL), Charsets.UTF_8);
            if (StringUtils.isNotBlank(memoryPoolStr)) {
                Map<String, Memory> map = JSON.parseObject(memoryPoolStr, new TypeReference<Map<String, Memory>>() {
                });
                this.memoryPool.putAll(map);
            }
            log.info("把文件系统中的服务器内存信息池内容加载到Spring容器成功！");
        } catch (Exception ignored) {
            log.info("把文件系统中的服务器内存信息池内容加载到Spring容器异常！");
        }
        try {
            String cpuPoolStr = FileUtils.readFileToString(new File(FileNameConstants.CPU_POOL), Charsets.UTF_8);
            if (StringUtils.isNotBlank(cpuPoolStr)) {
                Map<String, Cpu> map = JSON.parseObject(cpuPoolStr, new TypeReference<Map<String, Cpu>>() {
                });
                this.cpuPool.putAll(map);
            }
            log.info("把文件系统中的服务器CPU信息池内容加载到Spring容器成功！");
        } catch (Exception ignored) {
            log.info("把文件系统中的服务器CPU信息池内容加载到Spring容器异常！");
        }
        try {
            String diskPoolStr = FileUtils.readFileToString(new File(FileNameConstants.DISK_POOL), Charsets.UTF_8);
            if (StringUtils.isNotBlank(diskPoolStr)) {
                Map<String, Disk> map = JSON.parseObject(diskPoolStr, new TypeReference<Map<String, Disk>>() {
                });
                this.diskPool.putAll(map);
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
    private void clear() {
        this.instancePool.clear();
        this.netPool.clear();
        this.memoryPool.clear();
        this.cpuPool.clear();
        this.diskPool.clear();
        log.info("Spring容器中的信息池已经清空！");
    }

}