package com.gitee.pifeng.server.business.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.server.business.web.entity.MonitorJvmGarbageCollector;
import com.gitee.pifeng.server.business.web.vo.MonitorJvmGarbageCollectorVo;

import java.util.List;

/**
 * <p>
 * java虚拟机GC信息服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/6 19:59
 */
public interface IMonitorJvmGarbageCollectorService extends IService<MonitorJvmGarbageCollector> {

    /**
     * <p>
     * 获取java虚拟机GC信息
     * </p>
     *
     * @param instanceId 应用实例ID
     * @return java虚拟机GC信息表现层对象
     * @author 皮锋
     * @custom.date 2020/10/15 13:16
     */
    List<MonitorJvmGarbageCollectorVo> getJvmGcInfo(String instanceId);
}
