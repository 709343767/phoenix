package com.gitee.pifeng.monitoring.server.business.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.common.dto.JvmPackage;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorJvmGarbageCollector;

/**
 * <p>
 * java虚拟机GC信息服务接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2023/3/14 8:16
 */
public interface IJvmGarbageCollectorService extends IService<MonitorJvmGarbageCollector> {

    /**
     * <p>
     * 把java虚拟机GC信息添加或更新到数据库
     * </p>
     *
     * @param jvmPackage java虚拟机信息包
     * @author 皮锋
     * @custom.date 2020/8/28 10:38
     */
    void operateMonitorJvmGarbageCollector(JvmPackage jvmPackage);

}
