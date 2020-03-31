package com.transfar.server.durable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Charsets;
import com.transfar.server.business.server.core.*;
import com.transfar.server.business.server.domain.*;
import com.transfar.server.constant.FileNameConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * <p>
 * 生命周期管理
 * </p>
 * 1.在Spring容器初始化时加载一些资源；<br>
 * 2.在Spring容器销毁时清空一些资源<br>
 *
 * @author 皮锋
 * @custom.date 2020/3/25 16:38
 */
@Component
@Slf4j
public class Lifecycle implements InitializingBean, DisposableBean {

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
     * 在Spring容器初始化时加载资源
     * </p>
     * 1.把持久化到文件系统的信息池内容加载到Spring容器
     *
     * @author 皮锋
     * @custom.date 2020/3/31 16:17
     */
    @Override
    public void afterPropertiesSet() {
        try {
            String instancePoolStr = FileUtils.readFileToString(new File(FileNameConstants.INSTANCE_POOL), Charsets.UTF_8);
            if (StringUtils.isNotBlank(instancePoolStr)) {
                Map<String, Instance> map = JSON.parseObject(instancePoolStr, new TypeReference<Map<String, Instance>>() {
                });
                this.instancePool.putAll(map);
            }
            log.info("把文件系统中的应用实例池内容加载到Spring容器成功！");
        } catch (IOException ignored) {
            log.info("把文件系统中的应用实例池内容加载到Spring容器异常！");
        }
        try {
            String netPoolStr = FileUtils.readFileToString(new File(FileNameConstants.NET_POOL), Charsets.UTF_8);
            if (StringUtils.isNotBlank(netPoolStr)) {
                Map<String, Net> map = JSON.parseObject(netPoolStr, new TypeReference<Map<String, Net>>() {
                });
                this.netPool.putAll(map);
            }
            log.info("把文件系统中的网络信息池内容加载到Spring容器成功！");
        } catch (IOException ignored) {
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
        } catch (IOException ignored) {
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
        } catch (IOException ignored) {
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
        } catch (IOException ignored) {
            log.info("把文件系统中的服务器磁盘信息池内容加载到Spring容器异常！");
        }
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
    public void storePools() {
        try {
            FileUtils.writeStringToFile(new File(FileNameConstants.INSTANCE_POOL), this.instancePool.toJsonString(),
                    Charsets.UTF_8, false);
            log.info("把Spring容器中的应用实例池内容存入文件系统成功！");
        } catch (IOException e) {
            log.error("把Spring容器中的应用实例池内容存入文件系统异常！");
        }
        try {
            FileUtils.writeStringToFile(new File(FileNameConstants.NET_POOL), this.netPool.toJsonString(),
                    Charsets.UTF_8, false);
            log.info("把Spring容器中的网络信息池内容存入文件系统成功！");
        } catch (IOException e) {
            log.error("把Spring容器中的网络信息池内容存入文件系统异常！");
        }
        try {
            FileUtils.writeStringToFile(new File(FileNameConstants.MEMORY_POOL), this.memoryPool.toJsonString(),
                    Charsets.UTF_8, false);
            log.info("把Spring容器中的服务器内存信息池内容存入文件系统成功！");
        } catch (IOException e) {
            log.error("把Spring容器中的服务器内存信息池内容存入文件系统异常！");
        }
        try {
            FileUtils.writeStringToFile(new File(FileNameConstants.CPU_POOL), this.cpuPool.toJsonString(),
                    Charsets.UTF_8, false);
            log.info("把Spring容器中的服务器CPU信息池内容存入文件系统成功！");
        } catch (IOException e) {
            log.error("把Spring容器中的服务器CPU信息池内容存入文件系统异常！");
        }
        try {
            FileUtils.writeStringToFile(new File(FileNameConstants.DISK_POOL), this.diskPool.toJsonString(),
                    Charsets.UTF_8, false);
            log.info("把Spring容器中的服务器磁盘信息池内容存入文件系统成功！");
        } catch (IOException e) {
            log.error("把Spring容器中的服务器磁盘信息池内容存入文件系统异常！");
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
