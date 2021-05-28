package com.gitee.pifeng.monitoring.ui.business.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorJvmRuntime;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorJvmRuntimeVo;

/**
 * <p>
 * java虚拟机运行时信息服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/6 19:59
 */
public interface IMonitorJvmRuntimeService extends IService<MonitorJvmRuntime> {

    /**
     * <p>
     * 获取java虚拟机运行时信息
     * </p>
     *
     * @param instanceId 应用实例ID
     * @return java虚拟机运行时信息表现层对象
     * @author 皮锋
     * @custom.date 2020/10/15 19:53
     */
    MonitorJvmRuntimeVo getJvmRuntimeInfo(String instanceId);
}
