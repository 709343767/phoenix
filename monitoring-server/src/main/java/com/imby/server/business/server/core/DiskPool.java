package com.imby.server.business.server.core;

import com.imby.common.inf.ISuperBean;
import com.imby.server.business.server.domain.Disk;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 服务器磁盘信息池
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/30 12:33
 */
@SuppressWarnings("serial")
@Component
public class DiskPool extends ConcurrentHashMap<String, Disk> implements ISuperBean {

    /**
     * <p>
     * 更新服务器磁盘信息池
     * </p>
     *
     * @param key  服务器磁盘信息键
     * @param disk 服务器磁盘信息
     * @author 皮锋
     * @custom.date 2020/3/26 15:53
     */
    public void updateDiskPool(String key, Disk disk) {
        Disk poolDisk = this.get(key);
        if (poolDisk == null) {
            this.put(key, disk);
        } else {
            this.replace(key, disk);
        }
    }

}
