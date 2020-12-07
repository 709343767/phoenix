package com.gitee.pifeng.server.business.web.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.server.business.web.dao.IMonitorJvmMemoryDao;
import com.gitee.pifeng.server.business.web.entity.MonitorJvmMemory;
import com.gitee.pifeng.server.business.web.service.IMonitorJvmMemoryService;
import com.gitee.pifeng.server.business.web.vo.InstanceDetailPageJvmMemoryChartVo;
import com.gitee.pifeng.server.core.CalculateDateTime;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * java虚拟机内存信息服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/6 19:59
 */
@Service
public class MonitorJvmMemoryServiceImpl extends ServiceImpl<IMonitorJvmMemoryDao, MonitorJvmMemory> implements IMonitorJvmMemoryService {

    /**
     * java虚拟机内存信息数据访问对象
     */
    @Autowired
    private IMonitorJvmMemoryDao monitorJvmMemoryDao;

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
        List<InstanceDetailPageJvmMemoryChartVo> instanceDetailPageJvmMemoryChartVos = this.monitorJvmMemoryDao.getInstanceDetailPageJvmMemoryChartInfo(params);
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
    @Override
    public List<String> getJvmMemoryTypes(String instanceId) {
        return this.monitorJvmMemoryDao.getJvmMemoryTypes(instanceId);
    }

}
