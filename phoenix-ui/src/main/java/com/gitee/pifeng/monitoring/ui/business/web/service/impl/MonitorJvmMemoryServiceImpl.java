package com.gitee.pifeng.monitoring.ui.business.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorJvmMemoryDao;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorJvmMemory;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorJvmMemoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
