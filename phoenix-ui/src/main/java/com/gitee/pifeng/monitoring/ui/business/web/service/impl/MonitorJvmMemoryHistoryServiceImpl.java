package com.gitee.pifeng.monitoring.ui.business.web.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorJvmMemoryHistoryDao;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorJvmMemoryHistory;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorJvmMemoryHistoryService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.InstanceDetailPageJvmMemoryChartVo;
import com.gitee.pifeng.monitoring.ui.core.CalculateDateTime;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * java虚拟机内存历史记录服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-01-24
 */
@Service
public class MonitorJvmMemoryHistoryServiceImpl extends ServiceImpl<IMonitorJvmMemoryHistoryDao, MonitorJvmMemoryHistory> implements IMonitorJvmMemoryHistoryService {

    /**
     * java虚拟机内存历史记录数据访问对象
     */
    @Autowired
    private IMonitorJvmMemoryHistoryDao monitorJvmMemoryHistoryDao;

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
    @Override
    public List<InstanceDetailPageJvmMemoryChartVo> getInstanceDetailPageJvmMemoryChartInfo(String instanceId, String memoryType, String time) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("instanceId", instanceId);
        params.put("memoryType", memoryType);
        // 计算时间
        CalculateDateTime calculateDateTime = new CalculateDateTime(time).invoke();
        // 开始时间
        Date startTime = calculateDateTime.getStartTime();
        // 结束时间
        Date endTime = calculateDateTime.getEndTime();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        List<InstanceDetailPageJvmMemoryChartVo> instanceDetailPageJvmMemoryChartVos = this.monitorJvmMemoryHistoryDao.getInstanceDetailPageJvmMemoryChartInfo(params);
        // 除数（1024 * 1024 = 1048576）
        String v2 = "1048576";
        for (InstanceDetailPageJvmMemoryChartVo instanceDetailPageJvmMemoryChartVo : instanceDetailPageJvmMemoryChartVos) {
            // 转MB
            instanceDetailPageJvmMemoryChartVo.setUsed(NumberUtil.div(instanceDetailPageJvmMemoryChartVo.getUsed(), v2, 2).toString());
            // 转MB
            instanceDetailPageJvmMemoryChartVo.setCommitted(NumberUtil.div(instanceDetailPageJvmMemoryChartVo.getCommitted(), v2, 2).toString());
            // 转MB
            instanceDetailPageJvmMemoryChartVo.setInit(NumberUtil.div(instanceDetailPageJvmMemoryChartVo.getInit(), v2, 2).toString());
            // 转MB
            if (!StringUtils.equals(instanceDetailPageJvmMemoryChartVo.getMax(), "未定义")) {
                instanceDetailPageJvmMemoryChartVo.setMax(NumberUtil.div(instanceDetailPageJvmMemoryChartVo.getMax(), v2, 2).toString());
            }
        }
        return instanceDetailPageJvmMemoryChartVos;
    }

}
