package com.imby.server.business.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.imby.server.business.server.dao.IMonitorNetDao;
import com.imby.server.business.server.entity.MonitorNet;
import com.imby.server.business.server.service.INetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 网络信息服务接口实现
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/31 17:21
 */
@Service
public class NetServiceImpl implements INetService {

    /**
     * 网络信息数据访问对象
     */
    @Autowired
    private IMonitorNetDao monitorNetDao;

    /**
     * <p>
     * 更新网络信息
     * </p>
     *
     * @param monitorNet          网络信息
     * @param lambdaUpdateWrapper 更新条件
     * @return 更新记录数
     * @author 皮锋
     * @custom.date 2020/8/31 17:33
     */
    @Override
    public int updateNet(MonitorNet monitorNet, LambdaUpdateWrapper<MonitorNet> lambdaUpdateWrapper) {
        return this.monitorNetDao.update(monitorNet, lambdaUpdateWrapper);
    }

}
