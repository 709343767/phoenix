package com.imby.server.business.server.aop;

import com.alibaba.fastjson.JSON;
import com.imby.common.dto.HeartbeatPackage;
import com.imby.server.business.server.controller.HeartbeatController;
import com.imby.server.business.server.domain.Instance;
import com.imby.server.business.server.pool.InstancePool;
import com.imby.server.business.server.property.MonitoringProperties;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * <p>
 * 处理应用实例信息的切面。
 * </p>
 * 获取应用实例信息，并把应用实例信息添加或者更新到应用实例池。
 *
 * @author 皮锋
 * @custom.date 2020/5/12 11:07
 */
@Aspect
@Component
public class InstanceAspect {

    /**
     * 应用实例池
     */
    @Autowired
    private InstancePool instancePool;

    /**
     * 监控配置属性
     */
    @Autowired
    private MonitoringProperties config;

    /**
     * <p>
     * 定义切入点，切入点为{@link HeartbeatController#acceptHeartbeatPackage(String)}这一个方法。
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
     * 处理前置通知。
     * </p>
     *
     * @param joinPoint 提供对连接点上可用状态和有关状态的静态信息的反射访问。
     * @author 皮锋
     * @custom.date 2020/9/27 12:45
     */
    @Before("tangentPoint()")
    public void deal(JoinPoint joinPoint) {
        String args = String.valueOf(joinPoint.getArgs()[0]);
        HeartbeatPackage heartbeatPackage = JSON.parseObject(args, HeartbeatPackage.class);
        this.operateInstancePool(heartbeatPackage);
    }

    /**
     * <p>
     * 获取应用实例信息，并把应用实例信息添加或者更新到应用实例池。
     * </p>
     *
     * @param heartbeatPackage 心跳包
     * @author 皮锋
     * @custom.date 2020/5/12 11:13
     */
    private void operateInstancePool(HeartbeatPackage heartbeatPackage) {
        // 把应用实例交给应用实例池管理
        String key = heartbeatPackage.getInstanceId();
        Instance instance = new Instance();
        // 实例本身信息
        instance.setEndpoint(heartbeatPackage.getEndpoint());
        instance.setInstanceId(key);
        instance.setInstanceName(heartbeatPackage.getInstanceName());
        instance.setIp(heartbeatPackage.getIp());
        instance.setComputerName(heartbeatPackage.getComputerName());
        instance.setInstanceDesc(heartbeatPackage.getInstanceDesc());
        // 实例状态信息
        instance.setOnline(true);
        instance.setDateTime(new Date());
        instance.setLineAlarm(this.instancePool.get(key) != null && this.instancePool.get(key).isLineAlarm());
        instance.setThresholdSecond((int) (heartbeatPackage.getRate() * this.config.getThreshold()));
        // 更新应用实例池
        this.instancePool.updateInstancePool(key, instance);
    }

}
