package com.imby.server.business.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.imby.server.business.server.dao.IMonitorInstanceDao;
import com.imby.server.business.server.entity.MonitorInstance;
import com.imby.server.business.server.service.IInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 应用实例服务接口实现
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/6/29 15:21
 */
@Service
public class InstanceServiceImpl implements IInstanceService {

    /**
     * 应用实例数据访问对象
     */
    @Autowired
    private IMonitorInstanceDao monitorInstanceDao;


    /**
     * <p>
     * 更新应用实例
     * </p>
     *
     * @param monitorInstance     应用实例
     * @param lambdaUpdateWrapper 更新条件
     * @return 更新记录数
     * @author 皮锋
     * @custom.date 2020/6/29 15:27
     */
    @Override
    public int updateInstance(MonitorInstance monitorInstance, LambdaUpdateWrapper<MonitorInstance> lambdaUpdateWrapper) {
        return this.monitorInstanceDao.update(monitorInstance, lambdaUpdateWrapper);
    }

}
