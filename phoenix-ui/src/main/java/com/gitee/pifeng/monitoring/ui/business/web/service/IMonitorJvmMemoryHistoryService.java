package com.gitee.pifeng.monitoring.ui.business.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorJvmMemoryHistory;
import com.gitee.pifeng.monitoring.ui.business.web.vo.InstanceDetailPageJvmMemoryChartVo;

import java.util.List;

/**
 * <p>
 * java虚拟机内存历史记录服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-01-24
 */
public interface IMonitorJvmMemoryHistoryService extends IService<MonitorJvmMemoryHistory> {

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

}
