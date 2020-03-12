package com.transfar.business.server.service.impl;

import com.transfar.business.server.core.InstancePool;
import com.transfar.business.server.domain.Instance;
import com.transfar.business.server.service.IHeartbeatService;
import com.transfar.dto.HeartbeatPackage;
import com.transfar.property.MonitoringServerWebProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 心跳服务实现
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/12 10:05
 */
@Service
public class HeartbeatServiceImpl implements IHeartbeatService {

    /**
     * 应用实例池
     */
    @Autowired
    private InstancePool instancePool;

    /**
     * 监控配置属性
     */
    @Autowired
    private MonitoringServerWebProperties config;

    /**
     * <p>
     * 处理心跳包
     * </p>
     *
     * @param heartbeatPackage 心跳包
     * @return boolean
     * @author 皮锋
     * @custom.date 2020/3/12 10:18
     */
    @Override
    public boolean dealHeartbeatPackage(HeartbeatPackage heartbeatPackage) {
        // 把应用实例交给应用实例池，交给应用实例池管理
        Instance instance = new Instance();
        // 实例本身信息
        instance.setEndpoint(heartbeatPackage.getEndpoint());
        instance.setInstanceId(heartbeatPackage.getInstanceId());
        instance.setInstanceName(heartbeatPackage.getInstanceName());
        // 实例状态信息
        instance.setOnline(true);
        instance.setDateTime(heartbeatPackage.getDateTime());
        instance.setThresholdSecond((int) (heartbeatPackage.getRate() * config.getThreshold()));
        this.instancePool.put(heartbeatPackage.getInstanceId(), instance);
        return true;
    }
}
