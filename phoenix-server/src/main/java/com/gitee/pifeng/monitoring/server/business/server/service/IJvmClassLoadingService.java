package com.gitee.pifeng.monitoring.server.business.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.common.dto.JvmPackage;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorJvmClassLoading;

/**
 * <p>
 * java虚拟机类加载信息服务接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2023/3/12 19:32
 */
public interface IJvmClassLoadingService extends IService<MonitorJvmClassLoading> {

    /**
     * <p>
     * 把java虚拟机类加载信息添加或更新到数据库
     * </p>
     *
     * @param jvmPackage java虚拟机信息包
     * @author 皮锋
     * @custom.date 2020/8/28 8:54
     */
    void operateMonitorJvmClassLoading(JvmPackage jvmPackage);

}
