package com.imby.common.util;

import com.google.common.collect.Lists;
import com.imby.common.domain.server.DiskDomain;
import com.imby.common.init.InitSigar;
import lombok.extern.slf4j.Slf4j;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.SigarException;

import java.util.List;

/**
 * <p>
 * 磁盘工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/13 12:32
 */
@Slf4j
public class DiskUtils extends InitSigar {

    /**
     * <p>
     * 获取磁盘信息
     * </p>
     *
     * @return {@link DiskDomain}
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020/3/8 21:46
     */
    public static DiskDomain getDiskInfo() throws SigarException {
        DiskDomain diskDomain = new DiskDomain();
        List<DiskDomain.DiskInfoDomain> diskInfoDomains = Lists.newArrayList();

        FileSystem[] fileSystemArray = SIGAR.getFileSystemList();
        for (FileSystem fileSystem : fileSystemArray) {
            DiskDomain.DiskInfoDomain diskInfoDomain = new DiskDomain.DiskInfoDomain();

            diskInfoDomain.setDevName(fileSystem.getDevName());
            diskInfoDomain.setDirName(fileSystem.getDirName());
            diskInfoDomain.setTypeName(fileSystem.getTypeName());
            diskInfoDomain.setSysTypeName(fileSystem.getSysTypeName());

            FileSystemUsage fileSystemUsage;
            // 磁盘类型
            int type = fileSystem.getType();
            try {
                fileSystemUsage = SIGAR.getFileSystemUsage(fileSystem.getDirName());
            }
            // 当fileSystem.getType()为5时会出现该异常——此时文件系统类型为光驱
            catch (SigarException e) {
                // 本地硬盘
                if (type == 2) {
                    throw e;
                }
                continue;
            }
            // TYPE_UNKNOWN ：未知
            if (type == 0) {
                // continue;
                log.debug("{}", "TYPE_UNKNOWN！");
            }
            // TYPE_NONE
            else if (type == 1) {
                // continue;
                log.debug("{}", "TYPE_NONE！");
            }
            // TYPE_LOCAL_DISK : 本地硬盘
            else if (type == 2) {
                log.debug("{}", "TYPE_LOCAL_DISK！");
            }
            // TYPE_NETWORK ：网络
            else if (type == 3) {
                // continue;
                log.debug("{}", "TYPE_NETWORK！");
            }
            // TYPE_RAM_DISK ：闪存
            else if (type == 4) {
                // continue;
                log.debug("{}", "TYPE_RAM_DISK！");
            }
            // TYPE_CDROM ：光驱
            else if (type == 5) {
                log.debug("{}", "TYPE_CDROM！");
                continue;
            }
            // TYPE_SWAP ：页面交换
            else if (type == 6) {
                // continue;
                log.debug("{}", "TYPE_SWAP！");
            } else {
                continue;
            }
            diskInfoDomain.setTotal(String.format("%.2f", fileSystemUsage.getTotal() / MB) + "M");
            diskInfoDomain.setFree(String.format("%.2f", fileSystemUsage.getFree() / MB) + "M");
            diskInfoDomain.setUsed(String.format("%.2f", fileSystemUsage.getUsed() / MB) + "M");
            diskInfoDomain.setAvail(String.format("%.2f", fileSystemUsage.getAvail() / MB) + "M");
            diskInfoDomain.setUsePercent(String.format("%.2f", fileSystemUsage.getUsePercent() * 100) + "%");
            diskInfoDomains.add(diskInfoDomain);
        }
        diskDomain.setDiskInfoList(diskInfoDomains);
        diskDomain.setDiskNum(diskInfoDomains.size());
        return diskDomain;
    }

}
