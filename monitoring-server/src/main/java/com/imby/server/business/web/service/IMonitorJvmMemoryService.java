package com.imby.server.business.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.imby.server.business.web.entity.MonitorJvmMemory;
import com.imby.server.business.web.vo.InstanceDetailPageJvmMemoryChartVo;

import java.util.List;

/**
 * <p>
 * java虚拟机内存信息服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/6 19:59
 */
public interface IMonitorJvmMemoryService extends IService<MonitorJvmMemory> {

    /**
     * <p>
     * 获取应用实例详情页面java虚拟机内存图表信息
     * </p>
     *
     * @param instanceId 应用实例ID
     * @param memoryType 内存类型
     * @param time       时间
     * @return 应用实例详情页面java虚拟机内存图表信息表现层对象
     * @author 皮锋
     * @custom.date 2020/10/14 12:02
     */
    List<InstanceDetailPageJvmMemoryChartVo> getInstanceDetailPageJvmMemoryChartInfo(String instanceId, String memoryType, String time);

    /**
     * <p>
     * 获取jvm内存类型
     * </p>
     *
     * @param instanceId 应用实例ID
     * @return jvm内存类型
     * @author 皮锋
     * @custom.date 2020/10/15 10:00
     */
    List<String> getJvmMemoryTypes(String instanceId);
}
