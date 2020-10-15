package com.imby.server.business.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imby.server.business.web.dao.IMonitorJvmThreadDao;
import com.imby.server.business.web.entity.MonitorJvmThread;
import com.imby.server.business.web.service.IMonitorJvmThreadService;
import com.imby.server.business.web.vo.MonitorJvmThreadVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * java虚拟机线程信息服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/6 19:59
 */
@Service
public class MonitorJvmThreadServiceImpl extends ServiceImpl<IMonitorJvmThreadDao, MonitorJvmThread> implements IMonitorJvmThreadService {

    /**
     * java虚拟机线程信息数据访问对象
     */
    @Autowired
    private IMonitorJvmThreadDao monitorJvmThreadDao;

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
    @Override
    public MonitorJvmThreadVo getJvmThreadInfo(String instanceId) {
        LambdaQueryWrapper<MonitorJvmThread> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MonitorJvmThread::getInstanceId, instanceId);
        MonitorJvmThread monitorJvmThread = this.monitorJvmThreadDao.selectOne(lambdaQueryWrapper);
        return MonitorJvmThreadVo.builder().build().convertFor(monitorJvmThread);
    }

}
