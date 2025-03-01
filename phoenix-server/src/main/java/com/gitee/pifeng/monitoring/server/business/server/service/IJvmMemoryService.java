package com.gitee.pifeng.monitoring.server.business.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.common.dto.JvmPackage;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorJvmMemory;

/**
 * <p>
 * java虚拟机内存信息服务接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2023/3/14 8:05
 */
public interface IJvmMemoryService extends IService<MonitorJvmMemory> {

    /**
     * <p>
     * 把java虚拟机内存信息添加或更新到数据库
     * </p>
     *
     * @param jvmPackage java虚拟机信息包
     * @author 皮锋
     * @custom.date 2021/1/24 21:51
     */
    void operateMonitorJvmMemory(JvmPackage jvmPackage);

}
