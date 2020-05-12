package com.imby.server.business.server.aop;

import com.alibaba.fastjson.JSON;
import com.imby.common.dto.HeartbeatPackage;
import com.imby.server.business.server.core.InstancePool;
import com.imby.server.business.server.domain.Instance;
import com.imby.server.property.MonitoringServerWebProperties;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 获取心跳信息的切面。
 * </p>
 * 从心跳包中获取应用实例信息，并把应用实例添加或者更新到应用实例池。
 *
 * @author 皮锋
 * @custom.date 2020/5/12 11:07
 */
@Aspect
@Component
public class HeartbeatAspect {

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
     * 定义切入点，切入点为com.imby.server.business.server.controller.HeartbeatController.acceptHeartbeatPackage这一个方法。
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/2/22 17:56
     */
    @Pointcut("execution(public * com.imby.server.business.server.controller.HeartbeatController.acceptHeartbeatPackage(..))")
    public void tangentPoint() {
    }

    /**
     * <p>
     * 通过前置通知，从心跳包中获取应用实例信息，把应用实例添加或者更新到应用实例池。
     * </p>
     *
     * @param joinPoint 提供对连接点上可用状态和有关状态的静态信息的反射访问。
     * @author 皮锋
     * @custom.date 2020/5/12 11:13
     */
    @Before("tangentPoint()")
    public void operateInstancePool(JoinPoint joinPoint) {
        String args = String.valueOf(joinPoint.getArgs()[0]);
        HeartbeatPackage heartbeatPackage = JSON.parseObject(args, HeartbeatPackage.class);
        // 把应用实例交给应用实例池管理
        String key = heartbeatPackage.getInstanceId();
        Instance instance = new Instance();
        // 实例本身信息
        instance.setEndpoint(heartbeatPackage.getEndpoint());
        instance.setInstanceId(heartbeatPackage.getInstanceId());
        instance.setInstanceName(heartbeatPackage.getInstanceName());
        instance.setIp(heartbeatPackage.getIp());
        instance.setComputerName(heartbeatPackage.getComputerName());
        // 实例状态信息
        instance.setOnline(true);
        instance.setOnConnect(true);
        instance.setDateTime(heartbeatPackage.getDateTime());
        instance.setLineAlarm(this.instancePool.get(key) != null && this.instancePool.get(key).isLineAlarm());
        instance.setConnectAlarm(this.instancePool.get(key) != null && this.instancePool.get(key).isConnectAlarm());
        instance.setThresholdSecond((int) (heartbeatPackage.getRate() * config.getThreshold()));
        // 更新应用实例池
        this.instancePool.updateInstancePool(key, instance);
    }

}
