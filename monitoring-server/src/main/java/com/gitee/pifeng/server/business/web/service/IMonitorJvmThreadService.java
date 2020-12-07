package com.gitee.pifeng.server.business.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.server.business.web.entity.MonitorJvmThread;
import com.gitee.pifeng.server.business.web.vo.MonitorJvmThreadVo;

/**
 * <p>
 * java虚拟机线程信息服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/6 19:59
 */
public interface IMonitorJvmThreadService extends IService<MonitorJvmThread> {

    /**
     * <p>
     * 获取java虚拟机线程信息
     * </p>
     *
     * @param instanceId 应用实例ID
     * @return java虚拟机线程信息表现层对象
     * @author 皮锋
     * @custom.date 2020/10/15 12:50
     */
    MonitorJvmThreadVo getJvmThreadInfo(String instanceId);
}
