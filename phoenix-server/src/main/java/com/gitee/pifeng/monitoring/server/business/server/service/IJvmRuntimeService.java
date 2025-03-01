package com.gitee.pifeng.monitoring.server.business.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.common.dto.JvmPackage;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorJvmRuntime;

/**
 * <p>
 * java虚拟机运行时信息服务接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2023/3/12 19:26
 */
public interface IJvmRuntimeService extends IService<MonitorJvmRuntime> {

    /**
     * <p>
     * 把java虚拟机运行时信息添加或更新到数据库
     * </p>
     *
     * @param jvmPackage java虚拟机信息包
     * @author 皮锋
     * @custom.date 2020/8/27 19:20
     */
    void operateMonitorJvmRuntime(JvmPackage jvmPackage);

}
