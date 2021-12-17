package com.gitee.pifeng.monitoring.ui.business.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorJvmClassLoading;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorJvmClassLoadingVo;

/**
 * <p>
 * java虚拟机类加载信息服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/6 19:59
 */
public interface IMonitorJvmClassLoadingService extends IService<MonitorJvmClassLoading> {

    /**
     * <p>
     * 获取java虚拟机类加载信息
     * </p>
     *
     * @param instanceId 应用实例ID
     * @return java虚拟机类加载信息表现层对象
     * @author 皮锋
     * @custom.date 2020/10/15 13:49
     */
    MonitorJvmClassLoadingVo getJvmClassLoadingInfo(String instanceId);
}
