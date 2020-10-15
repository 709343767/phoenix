package com.imby.server.business.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imby.server.business.web.dao.IMonitorJvmClassLoadingDao;
import com.imby.server.business.web.entity.MonitorJvmClassLoading;
import com.imby.server.business.web.service.IMonitorJvmClassLoadingService;
import com.imby.server.business.web.vo.MonitorJvmClassLoadingVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * java虚拟机类加载信息服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/6 19:59
 */
@Service
public class MonitorJvmClassLoadingServiceImpl extends ServiceImpl<IMonitorJvmClassLoadingDao, MonitorJvmClassLoading> implements IMonitorJvmClassLoadingService {

    /**
     * java虚拟机类加载信息数据访问对象
     */
    @Autowired
    private IMonitorJvmClassLoadingDao monitorJvmClassLoadingDao;

    /**
     * <p>
     * 获取java虚拟机类加载信息
     * </p>
     *
     * @param instanceId 应用实例ID
     * @return java虚拟机类加载信息表现层对象
     * @author 皮锋
     * @custom.date 2020/10/15 13:49
     */
    @Override
    public MonitorJvmClassLoadingVo getJvmClassLoadingInfo(String instanceId) {
        LambdaQueryWrapper<MonitorJvmClassLoading> loadingLambdaQueryWrapper = new LambdaQueryWrapper<>();
        loadingLambdaQueryWrapper.eq(MonitorJvmClassLoading::getInstanceId, instanceId);
        MonitorJvmClassLoading monitorJvmClassLoading = this.monitorJvmClassLoadingDao.selectOne(loadingLambdaQueryWrapper);
        return MonitorJvmClassLoadingVo.builder().build().convertFor(monitorJvmClassLoading);
    }

}
