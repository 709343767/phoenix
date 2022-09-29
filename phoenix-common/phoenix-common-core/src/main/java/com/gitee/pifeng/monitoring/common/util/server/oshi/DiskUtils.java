package com.gitee.pifeng.monitoring.common.util.server.oshi;

import cn.hutool.core.util.NumberUtil;
import com.gitee.pifeng.monitoring.common.domain.server.DiskDomain;
import com.gitee.pifeng.monitoring.common.init.InitOshi;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;

import java.util.List;

/**
 * <p>
 * 磁盘工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/5/12 10:39
 */
@Slf4j
public class DiskUtils extends InitOshi {

    /**
     * 文件系统类型名，比如本地硬盘、光驱、网络文件系统等
     */
    private static final String TYPE_NAME = "local";

    /**
     * <p>
     * 获取磁盘信息
     * </p>
     *
     * @return {@link DiskDomain}
     * @author 皮锋
     * @custom.date 2022/5/12 10:57
     */
    public static DiskDomain getDiskInfo() {
        try {
            DiskDomain diskDomain = new DiskDomain();
            List<DiskDomain.DiskInfoDomain> diskInfoDomains = Lists.newLinkedList();

            FileSystem fileSystem = SYSTEM_INFO.getOperatingSystem().getFileSystem();
            // 如果为 true，则将列表筛选为仅本地文件存储
            List<OSFileStore> fsArray = fileSystem.getFileStores(true);
            for (OSFileStore fs : fsArray) {
                DiskDomain.DiskInfoDomain diskInfoDomain = new DiskDomain.DiskInfoDomain();

                diskInfoDomain.setDevName(fs.getName());
                diskInfoDomain.setDirName(fs.getMount());
                diskInfoDomain.setTypeName(TYPE_NAME);
                diskInfoDomain.setSysTypeName(fs.getType());
                // 总空间/容量
                long total = fs.getTotalSpace();
                // 可用空间
                long usable = fs.getUsableSpace();
                // 剩余空间
                long free = fs.getFreeSpace();
                // 已使用空间
                long used = total - usable;
                diskInfoDomain.setTotal(total);
                diskInfoDomain.setFree(free);
                diskInfoDomain.setUsed(used);
                diskInfoDomain.setAvail(usable);
                diskInfoDomain.setUsePercent(total == 0 ? 0 : NumberUtil.round((double) used / (double) total, 4).doubleValue());
                diskInfoDomains.add(diskInfoDomain);
            }
            diskDomain.setDiskInfoList(diskInfoDomains);
            diskDomain.setDiskNum(diskInfoDomains.size());
            return diskDomain;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}