package com.gitee.pifeng.monitoring.ui.business.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorJvmRuntimeDao;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorJvmRuntime;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorJvmRuntimeService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorJvmRuntimeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * java虚拟机运行时信息服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/6 19:59
 */
@Service
public class MonitorJvmRuntimeServiceImpl extends ServiceImpl<IMonitorJvmRuntimeDao, MonitorJvmRuntime> implements IMonitorJvmRuntimeService {

    /**
     * java虚拟机运行时信息数据访问对象
     */
    @Autowired
    private IMonitorJvmRuntimeDao monitorJvmRuntimeDao;

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
    @Override
    public MonitorJvmRuntimeVo getJvmRuntimeInfo(String instanceId) {
        LambdaQueryWrapper<MonitorJvmRuntime> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MonitorJvmRuntime::getInstanceId, instanceId);
        MonitorJvmRuntime monitorJvmRuntime = this.monitorJvmRuntimeDao.selectOne(lambdaQueryWrapper);
        return MonitorJvmRuntimeVo.builder().build().convertFor(monitorJvmRuntime);
    }

}
