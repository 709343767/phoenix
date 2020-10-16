package com.imby.server.business.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imby.common.util.DataSizeUtils;
import com.imby.server.business.web.dao.IMonitorJvmMemoryDao;
import com.imby.server.business.web.entity.MonitorJvmMemory;
import com.imby.server.business.web.service.IMonitorJvmMemoryService;
import com.imby.server.business.web.vo.InstanceDetailPageJvmMemoryVo;
import com.imby.server.core.CalculateDateTime;
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
     * 获取应用实例详情页面java虚拟机内存信息
     * </p>
     *
     * @param instanceId 应用实例ID
     * @param memoryType 内存类型
     * @param time       时间
     * @return 应用实例详情页面java虚拟机内存信息表现层对象
     * @author 皮锋
     * @custom.date 2020/10/14 12:02
     */
    @Override
    public List<InstanceDetailPageJvmMemoryVo> getInstanceDetailPageJvmMemory(String instanceId, String memoryType, String time) {
        Map<String, Object> params = new HashMap<>(4);
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
        List<InstanceDetailPageJvmMemoryVo> instanceDetailPageJvmMemoryVos = this.monitorJvmMemoryDao.getInstanceDetailPageJvmMemory(params);
        for (InstanceDetailPageJvmMemoryVo instanceDetailPageJvmMemoryVo : instanceDetailPageJvmMemoryVos) {
            // 转MB
            instanceDetailPageJvmMemoryVo.setUsed(String.valueOf(DataSizeUtils.parse(instanceDetailPageJvmMemoryVo.getUsed()) / 1024 / 1024));
            // 转MB
            instanceDetailPageJvmMemoryVo.setCommitted(String.valueOf(DataSizeUtils.parse(instanceDetailPageJvmMemoryVo.getCommitted()) / 1024 / 1024));
        }
        return instanceDetailPageJvmMemoryVos;
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
